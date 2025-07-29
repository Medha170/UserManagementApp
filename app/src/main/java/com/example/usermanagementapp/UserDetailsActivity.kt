package com.example.usermanagementapp

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserDetailsActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSave: Button
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        btnSave = findViewById(R.id.btnSave)
        val sharedPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPrefs.getString("username", null)
        val role = sharedPrefs.getString("userType", "normal")
        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val uid = firebaseAuth.currentUser?.uid ?: return@setOnClickListener
            val userMap = mapOf(
                "username" to username,
                "name" to name,
                "email" to email,
                "role" to role
            )
            database.child("users").child(uid).setValue(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Details saved!", Toast.LENGTH_SHORT).show()
                    etName.text.clear()
                    etEmail.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
