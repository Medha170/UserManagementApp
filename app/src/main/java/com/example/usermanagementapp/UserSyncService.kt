package com.example.usermanagementapp

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserSyncService : Service() {
    private val databaseRef = FirebaseDatabase.getInstance().getReference("users")
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate() {
        super.onCreate()
        Log.d("UserSyncService", "Service created. Starting data fetch...")
        fetchUserData()
    }
    private fun fetchUserData() {
        val sharedPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val username = sharedPrefs.getString("username", "") ?: ""
        val userType = sharedPrefs.getString("userType", "normal") ?: "normal"
        Log.d("UserSyncService", "Fetched from SharedPrefs → username: $username, userType: $userType")
        if (userType == "admin") {
            Log.d("UserSyncService", "Fetching ALL user data (admin mode)")
            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userDataList = ArrayList<String>()
                    for (userSnap in snapshot.children) {
                        val uname = userSnap.child("username").getValue(String::class.java) ?: "N/A"
                        val name = userSnap.child("name").getValue(String::class.java) ?: "N/A"
                        val email = userSnap.child("email").getValue(String::class.java) ?: "N/A"
                        val type = userSnap.child("role").getValue(String::class.java) ?: "N/A"
                        userDataList.add("Username: $uname\nName: $name\nEmail: $email\nRole: $type")
                    }

                    Log.d("UserSyncService", "Broadcasting ${userDataList.size} users to UI")
                    val intent = Intent("com.example.ACTION_USER_DATA")
                    intent.setPackage(packageName)
                    intent.putStringArrayListExtra("userList", userDataList)
                    sendBroadcast(intent)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("UserSyncService", "Error fetching all users: ${error.message}")
                }
            })
        } else {
            Log.d("UserSyncService", "Fetching single user data (normal mode)")
            val uid = auth.currentUser?.uid
            if (uid == null) {
                Log.e("UserSyncService", "Current user UID is null")
                return
            }
            databaseRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val uname = snapshot.child("username").getValue(String::class.java) ?: "N/A"
                    val name = snapshot.child("name").getValue(String::class.java) ?: "N/A"
                    val email = snapshot.child("email").getValue(String::class.java) ?: "N/A"
                    val type = snapshot.child("role").getValue(String::class.java) ?: "N/A"

                    val details = "Username: $uname\nName: $name\nEmail: $email\nRole: $type"
                    Log.d("UserSyncService", "Broadcasting single user details")
                    val intent = Intent("com.example.ACTION_USER_DATA")
                    intent.setPackage(packageName)
                    intent.putExtra("userDetails", details)
                    sendBroadcast(intent)
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("UserSyncService", "Error fetching user: ${error.message}")
                }
            })
        }
    }
    override fun onBind(intent: Intent?): IBinder? = null
}
