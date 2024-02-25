package com.example.android.metapath

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signin.*
import java.lang.Exception

class SigninActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        auth = Firebase.auth

        signup_switch.setOnClickListener {
            signup_switch.background = resources.getDrawable(R.drawable.switch_trcks,null)
            signup_switch.setTextColor(resources.getColor(R.color.textColor,null))
            login_switch.background = null
            singUpLayout.visibility = View.VISIBLE
            logInLayout.visibility = View.GONE
            login_switch.setTextColor(resources.getColor(R.color.black,null))
            logIn.visibility = View.GONE
            signUp.visibility = View.VISIBLE
        }
        login_switch.setOnClickListener {
            signup_switch.background = null
            signup_switch.setTextColor(resources.getColor(R.color.black,null))
            login_switch.background = resources.getDrawable(R.drawable.switch_trcks,null)
            singUpLayout.visibility = View.GONE
            logInLayout.visibility = View.VISIBLE
            login_switch.setTextColor(resources.getColor(R.color.textColor,null))
            logIn.visibility = View.VISIBLE
            signUp.visibility = View.GONE
        }

        logIn.setOnClickListener {
            logInUser()
        }

        signUp.setOnClickListener {
            signUpUser()
        }
    }

    fun signUpUser(){
        val emailET = findViewById<EditText>(R.id.email_su)
        val passET = findViewById<EditText>(R.id.pswd_su)
        val confirmPassET = findViewById<EditText>(R.id.cnf_pswd_su)

        if(emailET.text.toString().isEmpty()){
            emailET.error = "Please Enter email"
            emailET.requestFocus()
            return
        }
        if(passET.text.toString().isEmpty()){
            passET.error = "Please Enter password"
            passET.requestFocus()
            return
        }
        if(confirmPassET.text.toString().isEmpty()){
            confirmPassET.error = "Please confirm password"
            confirmPassET.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailET.text.toString()).matches()){
            emailET.error = "Please Enter valid email"
            emailET.requestFocus()
            return
        }
        if(passET.text.toString() != confirmPassET.text.toString()){
            Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(emailET.text.toString(), passET.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser

                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Success! A mail has been sent, Please verify your email.", Toast.LENGTH_LONG).show()
                                login_switch.performClick()
                            }
                        }
                } else {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthUserCollisionException) {
                        // email already in use
                        Toast.makeText(this, "User already exist. Please, Login to continue.", Toast.LENGTH_SHORT).show()
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        Toast.makeText(this, "Please enter a strong password!", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this, "Sign Up failed. Please, try again after some time.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    fun logInUser() {
        val emailET = findViewById<EditText>(R.id.email_si)
        val passET = findViewById<EditText>(R.id.pswd_si)

        if(emailET.text.toString().isEmpty()){
            emailET.error = "Please Enter email"
            emailET.requestFocus()
            return
        }
        if(passET.text.toString().isEmpty()){
            passET.error = "Please Enter password"
            passET.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailET.text.toString()).matches()){
            emailET.error = "Please Enter valid email"
            emailET.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(emailET.text.toString(), passET.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        if(user.isEmailVerified){
                            reload()
                        }else{
                            Toast.makeText(baseContext, "Please verify your email address.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(baseContext, "SignIn failed!", Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun reload() {
        startActivity(Intent(this,DashboardActivity::class.java))
        finish()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser!=null && currentUser.isEmailVerified){
            reload()
        }
    }
}