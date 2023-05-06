package com.androiddev.twitter

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import com.androiddev.twitter.R
import android.content.Intent
import android.view.View
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener

class EditTweetActivity : AppCompatActivity() {
    lateinit  var submit_box: EditText
    lateinit  var submit_btn: Button
    lateinit  var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_tweet)
        submit_box = findViewById(R.id.compose_box)
        submit_btn = findViewById(R.id.submit_btn)
        val intent = intent
        val id = intent.getStringExtra("id")
        val text = intent.getStringExtra("text")
        submit_box.setText(text)
        databaseReference = FirebaseDatabase.getInstance().reference
        submit_btn.setOnClickListener(View.OnClickListener {
            val text = submit_box.getText().toString()
            if (text == "") {
                Toast.makeText(this@EditTweetActivity, "Tweet can't be empty", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            databaseReference!!.child("Posts").child(id!!).child("text").setValue(text)
                .addOnCompleteListener {
                    Toast.makeText(this@EditTweetActivity, "Tweet Updateed", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
        })
    }
}