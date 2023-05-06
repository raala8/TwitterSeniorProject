package com.androiddev.twitter

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import com.androiddev.twitter.R
import android.content.SharedPreferences
import android.content.Intent
import android.view.View
import android.widget.Button
import com.androiddev.twitter.RegisterActivity
import com.androiddev.twitter.SigninActivity

class introActivity : AppCompatActivity() {
    lateinit var next_btn: Button
    lateinit var tv_login: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        next_btn = findViewById(R.id.next_btn)
        tv_login = findViewById(R.id.tv_login)
        next_btn.setOnClickListener(View.OnClickListener {
            val editor = getSharedPreferences("intro", MODE_PRIVATE).edit()
            editor.putString("status", "true")
            editor.apply()
            startActivity(Intent(this@introActivity, RegisterActivity::class.java))
            finish()
        })
        tv_login.setOnClickListener(View.OnClickListener {
            val editor = getSharedPreferences("intro", MODE_PRIVATE).edit()
            editor.putString("status", "true")
            editor.apply()
            startActivity(Intent(this@introActivity, SigninActivity::class.java))
            finish()
        })
    }
}