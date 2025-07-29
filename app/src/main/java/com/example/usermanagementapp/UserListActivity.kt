package com.example.usermanagementapp

import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class UserListActivity : AppCompatActivity() {
    private lateinit var tvUserDetails: TextView
    private lateinit var lvAllUsers: ListView
    private lateinit var userListAdapter: ArrayAdapter<String>
    private val userList = ArrayList<String>()
    private lateinit var userDataReceiver: BroadcastReceiver
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        tvUserDetails = findViewById(R.id.tvUserDetails)
        lvAllUsers = findViewById(R.id.lvAllUsers)
        userListAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userList)
        lvAllUsers.adapter = userListAdapter
        // Start the background service
        val serviceIntent = Intent(this, UserSyncService::class.java)
        Handler(Looper.getMainLooper()).postDelayed({
            startService(serviceIntent)
        }, 300)
        // Register broadcast receiver
        userDataReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    if (it.hasExtra("userList")) {
                        val list = it.getStringArrayListExtra("userList") ?: return
                        Log.d("UserListActivity", "Received user list of size: ${list.size}")
                        lvAllUsers.visibility = ListView.VISIBLE
                        tvUserDetails.visibility = TextView.GONE
                        userList.clear()
                        userList.addAll(list)
                        userListAdapter.notifyDataSetChanged()
                    }

                    if (it.hasExtra("userDetails")) {
                        val details = it.getStringExtra("userDetails") ?: return
                        Log.d("UserListActivity", "Received single user details")
                        lvAllUsers.visibility = ListView.GONE
                        tvUserDetails.visibility = TextView.VISIBLE
                        tvUserDetails.text = details
                    }
                }
            }
        }

        registerReceiver(
            userDataReceiver,
            IntentFilter("com.example.ACTION_USER_DATA"),
            Context.RECEIVER_NOT_EXPORTED
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(userDataReceiver)
    }
}
