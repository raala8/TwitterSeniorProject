package com.androiddev.twitter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androiddev.twitter.R
import android.content.SharedPreferences
import android.content.Intent
import com.androiddev.twitter.MainActivity
import java.lang.Exception

class suplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suplash)
        val background: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)

                    // After 5 seconds redirect to another intent
                    val preferences = getSharedPreferences("intro", MODE_PRIVATE)
                    if (preferences.contains("status")) {
                        val i = Intent(baseContext, SigninActivity::class.java)
                        startActivity(i)
                        //                    Remove activity
                        finish()
                    } else {
                        val i = Intent(baseContext, introActivity::class.java)
                        startActivity(i)
                        //                    Remove activity
                        finish()
                    }
                } catch (e: Exception) {
//                    Toast.makeText(SuplashActivity.this, "exception test", Toast.LENGTH_SHORT).show();
                }
            }
        }
        // start thread
        background.start()
    }
}