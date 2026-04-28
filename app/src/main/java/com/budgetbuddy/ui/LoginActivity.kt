package com.budgetbuddy.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.budgetbuddy.R
import com.budgetbuddy.data.DBHelper
import com.budgetbuddy.utils.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.d("BudgetBuddy", "🚪 Login screen created")

        dbHelper = DBHelper(this)
        sessionManager = SessionManager(this)

        // Auto-login check
        if (sessionManager.isLoggedIn()) {
            Log.i("BudgetBuddy", "User already logged in, skipping to dashboard")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            Log.d("BudgetBuddy", "Login attempt with username: $username")

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill both fields, buddy!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val userId = dbHelper.loginUser(username, password)
            if (userId != -1) {
                Log.i("BudgetBuddy", "Login successful for user ID: $userId")
                sessionManager.saveLoginSession(userId)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Log.w("BudgetBuddy", "Login failed for username: $username")
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            Log.d("BudgetBuddy", "Register attempt with username: $username")

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill both fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (dbHelper.registerUser(username, password)) {
                Log.i("BudgetBuddy", "Registration successful, please login")
                Toast.makeText(this, "Registered! Please login.", Toast.LENGTH_SHORT).show()
                etUsername.text.clear()
                etPassword.text.clear()
            } else {
                Log.w("BudgetBuddy", "Registration failed – username exists")
                Toast.makeText(this, "Username exists", Toast.LENGTH_SHORT).show()
            }
        }
    }
}