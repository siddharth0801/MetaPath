package com.example.android.metapath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DashboardActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        auth = Firebase.auth
        val currentUser = auth.currentUser

        val welcomeTV = findViewById<TextView>(R.id.welcomeTV)
        if (currentUser != null) {
            welcomeTV.text = "Welcome!..." + currentUser.email.toString()
        }

        val logoutBtn = findViewById<Button>(R.id.logoutButton)
        logoutBtn.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser(){
        Firebase.auth.signOut()
        startActivity(Intent(this, SigninActivity::class.java))
        finish()
    }
}