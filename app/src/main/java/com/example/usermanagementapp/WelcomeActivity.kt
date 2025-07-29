package com.example.usermanagementapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
class WelcomeActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var btnLogout: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btnFillDetails: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        tvWelcome = findViewById(R.id.tvWelcome)
        btnLogout = findViewById(R.id.btnLogout)
        firebaseAuth = FirebaseAuth.getInstance()

        // Fetch username from SharedPreferences
        val sharedPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPrefs.getString("username", "User")

        tvWelcome.text = "Welcome, $username!"

        btnFillDetails = findViewById(R.id.btnFillDetails)

        btnFillDetails.setOnClickListener {
            val intent = Intent(this, UserDetailsActivity::class.java)
            startActivity(intent)
        }

        val btnShowUsers = findViewById<Button>(R.id.btnShowUsers)
        btnShowUsers.setOnClickListener {
            startActivity(Intent(this, UserListActivity::class.java))
        }

        btnLogout.setOnClickListener {
            // Clear SharedPreferences
            sharedPrefs.edit().clear().apply()

            // Sign out from Firebase
            firebaseAuth.signOut()

            // Navigate back to RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
