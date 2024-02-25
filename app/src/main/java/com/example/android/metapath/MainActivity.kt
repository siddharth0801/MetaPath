package com.example.android.metapath

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        Handler().postDelayed({
            val arrow1 = findViewById<ImageView>(R.id.arrow1)
            arrow1.visibility=VISIBLE
            Handler().postDelayed({
                val arrow2 = findViewById<ImageView>(R.id.arrow2)
                arrow2.visibility=VISIBLE
                Handler().postDelayed({
                    val arrow3 = findViewById<ImageView>(R.id.arrow3)
                    arrow3.visibility=VISIBLE
                    Handler().postDelayed({
                        val intent = Intent(this@MainActivity, SigninActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 1000)
                }, 700)
            }, 700)
        }, 700)

    }
}