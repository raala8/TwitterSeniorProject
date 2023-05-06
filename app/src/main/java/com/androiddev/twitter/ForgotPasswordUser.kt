package com.androiddev.twitter

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.widget.FrameLayout
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import android.view.View
import com.androiddev.twitter.R
import com.google.firebase.database.FirebaseDatabase
import android.widget.Toast
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseError

class ForgotPasswordUser : AppCompatActivity() {
    lateinit  var phone_box: TextInputEditText
    lateinit  var pwd_box: TextInputEditText
    lateinit var cPwd_box: TextInputEditText
    lateinit  var okay_btn: FrameLayout
    lateinit  var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_user)
        phone_box = findViewById(R.id.phone_box)
        pwd_box = findViewById(R.id.pwd_box)
        cPwd_box = findViewById(R.id.cpwd_box)
        okay_btn = findViewById(R.id.okay_btn)
        databaseReference = FirebaseDatabase.getInstance().reference
        okay_btn.setOnClickListener(View.OnClickListener {
            val phone = phone_box.getText().toString()
            val pwd = pwd_box.getText().toString()
            val cpwd = cPwd_box.getText().toString()
            if (phone.isEmpty()) {
                phone_box.setError("Valid number is required")
                phone_box.requestFocus()
                return@OnClickListener
            }
            if (pwd.isEmpty()) {
                Toast.makeText(
                    this@ForgotPasswordUser,
                    "Please Enter Passwords",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            if (pwd != cpwd) {
                Toast.makeText(
                    this@ForgotPasswordUser,
                    "Please confirm Passwords",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            if (pwd.length < 8) {
                Toast.makeText(
                    this@ForgotPasswordUser,
                    "Password must be of 8 chars",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            databaseReference!!.child("Users")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(phone)) {
                            databaseReference!!.child("Users").child(phone).child("password")
                                .setValue(pwd).addOnCompleteListener {
                                Toast.makeText(
                                    this@ForgotPasswordUser,
                                    "Password changed",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                        } else {
                            Toast.makeText(
                                this@ForgotPasswordUser,
                                "No user Registered with this Phone",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        })
    }
}