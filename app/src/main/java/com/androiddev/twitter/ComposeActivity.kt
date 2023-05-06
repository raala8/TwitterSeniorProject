package com.androiddev.twitter

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import com.androiddev.twitter.R
import android.content.SharedPreferences
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import com.androiddev.twitter.PostModule
import com.google.android.gms.tasks.OnCompleteListener
import java.text.SimpleDateFormat
import java.util.*

class ComposeActivity() : AppCompatActivity() {
    lateinit var submit_box: EditText
    lateinit  var submit_btn: Button
    lateinit  var Suggessions_back: ImageView
    lateinit  var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)
        submit_box = findViewById(R.id.compose_box)
        submit_btn = findViewById(R.id.submit_btn)
        Suggessions_back = findViewById(R.id.Suggessions_back)
        val preferences = getSharedPreferences("USER", MODE_PRIVATE)
        val uname = preferences.getString("uname", "")
        val image = preferences.getString("image", "")
        databaseReference = FirebaseDatabase.getInstance().reference
        Suggessions_back.setOnClickListener(View.OnClickListener { finish() })
        submit_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val text = submit_box.getText().toString()
                if ((text == "")) {
                    Toast.makeText(
                        this@ComposeActivity,
                        "Please enter some text",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val df = SimpleDateFormat("dd-MM-yyyy HH:mm")
                val currentTime = df.format(Calendar.getInstance().time)
                val random = System.currentTimeMillis()
                val id = "Post$random"
                val model = PostModule(
                    uname,
                    image,
                    text, 0, currentTime, id
                )
                databaseReference!!.child("Posts").child(id).setValue(model).addOnCompleteListener(
                    OnCompleteListener {
                        Toast.makeText(this@ComposeActivity, "Tweet Posted", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    })
            }
        })
    }
}