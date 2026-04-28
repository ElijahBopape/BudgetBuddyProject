package com.budgetbuddy.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.budgetbuddy.R
import com.budgetbuddy.data.DBHelper
import com.budgetbuddy.utils.SessionManager
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var sessionManager: SessionManager
    private lateinit var tvGreeting: TextView
    private lateinit var tvBudgetStatus: TextView
    private lateinit var tvCurrentTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("BudgetBuddy", "📊 Dashboard opened")

        dbHelper = DBHelper(this)
        sessionManager = SessionManager(this)

        val userId = sessionManager.getUserId()
        if (userId == -1) {
            Log.e("BudgetBuddy", "No user session found, logging out")
            logout()
            return
        }

        tvGreeting = findViewById(R.id.tvGreeting)
        tvBudgetStatus = findViewById(R.id.tvBudgetStatus)
        tvCurrentTotal = findViewById(R.id.tvCurrentTotal)

        // All clickable cards are now MaterialCardViews, so use View type
        val btnAddExpense = findViewById<View>(R.id.btnAddExpense)
        val btnViewExpenses = findViewById<View>(R.id.btnViewExpenses)
        val btnManageCategories = findViewById<View>(R.id.btnManageCategories)
        val btnCategoryReport = findViewById<View>(R.id.btnCategoryReport)
        val btnSetBudget = findViewById<View>(R.id.btnSetBudget)
        val btnLogout = findViewById<View>(R.id.btnLogout)

        btnAddExpense.setOnClickListener {
            Log.i("BudgetBuddy", "➕ Add Expense clicked")
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }
        btnViewExpenses.setOnClickListener {
            Log.i("BudgetBuddy", "📋 View Expenses clicked")
            startActivity(Intent(this, ExpenseListActivity::class.java))
        }
        btnManageCategories.setOnClickListener {
            Log.i("BudgetBuddy", "🏷️ Manage Categories clicked")
            startActivity(Intent(this, ManageCategoriesActivity::class.java))
        }
        btnCategoryReport.setOnClickListener {
            Log.i("BudgetBuddy", "📊 Category Totals clicked")
            startActivity(Intent(this, CategoryReportActivity::class.java))
        }
        btnSetBudget.setOnClickListener {
            Log.i("BudgetBuddy", "🎯 Set Budget Goals clicked")
            showSetBudgetDialog()
        }
        btnLogout.setOnClickListener {
            Log.i("BudgetBuddy", "🚪 Logout clicked")
            logout()
        }

        updateDashboard()
    }

    override fun onResume() {
        super.onResume()
        Log.d("BudgetBuddy", "🔄 Dashboard resumed, updating totals")
        updateDashboard()
    }

    private fun updateDashboard() {
        val userId = sessionManager.getUserId()
        val cal = Calendar.getInstance()
        val yearMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(cal.time)
        val total = dbHelper.getTotalExpensesForCurrentMonth(userId, yearMonth)
        tvCurrentTotal.text = "This month: R$total"
        Log.d("BudgetBuddy", "Updated total for month $yearMonth: R$total")

        val minGoal = sessionManager.getMonthlyMinGoal(userId)
        val maxGoal = sessionManager.getMonthlyMaxGoal(userId)

        val status = if (minGoal > 0 && maxGoal > 0) {
            when {
                total < minGoal -> "⚠️ Below minimum goal (₹$minGoal)"
                total > maxGoal -> "⚠️ Exceeded max budget (₹$maxGoal)"
                else -> "✅ Within budget (₹$minGoal - ₹$maxGoal)"
            }
        } else {
            "Set budget goals (Min & Max)"
        }
        tvBudgetStatus.text = status

        val username = getUsernameFromDb(userId)
        tvGreeting.text = "Hello, $username"
    }

    private fun getUsernameFromDb(userId: Int): String {
        // In a real app, fetch from DB. Here we return a friendly name.
        return "Budgeter"
    }

    private fun showSetBudgetDialog() {
        val userId = sessionManager.getUserId()
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_budget, null)
        val etMin = view.findViewById<EditText>(R.id.etMinGoal)
        val etMax = view.findViewById<EditText>(R.id.etMaxGoal)
        etMin.setText(sessionManager.getMonthlyMinGoal(userId).toString())
        etMax.setText(sessionManager.getMonthlyMaxGoal(userId).toString())

        builder.setTitle("Set Monthly Budget Goals")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                val min = etMin.text.toString().toDoubleOrNull() ?: 0.0
                val max = etMax.text.toString().toDoubleOrNull() ?: 0.0
                if (min >= 0 && max >= min) {
                    sessionManager.setMonthlyMinGoal(userId, min)
                    sessionManager.setMonthlyMaxGoal(userId, max)
                    Log.i("BudgetBuddy", "Budget goals saved: Min=$min, Max=$max")
                    updateDashboard()
                    Toast.makeText(this, "Budget goals saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Min ≥ 0 and Max ≥ Min", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logout() {
        Log.d("BudgetBuddy", "Logging out, clearing session")
        sessionManager.clearSession()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}