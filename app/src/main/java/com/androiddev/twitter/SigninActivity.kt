package com.androiddev.twitter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.widget.FrameLayout
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import android.os.Bundle
import android.content.SharedPreferences
import android.content.Intent
import android.view.View
import com.androiddev.twitter.MainActivity
import com.androiddev.twitter.R
import com.androiddev.twitter.ForgotPasswordUser
import com.google.firebase.database.FirebaseDatabase
import com.androiddev.twitter.RegisterActivity
import android.widget.Toast
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.androiddev.twitter.SigninActivity
import com.google.firebase.database.DatabaseError

class SigninActivity : AppCompatActivity() {
    lateinit  var phone_box: TextInputEditText
    lateinit var pwd_box: TextInputEditText
    lateinit var login_btn: FrameLayout
    lateinit var newAccount_tv: TextView
    lateinit var for_tv: TextView
    var databaseReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences = getSharedPreferences("USER", MODE_PRIVATE)
        if (preferences.contains("phone")) {
            startActivity(Intent(this@SigninActivity, MainActivity::class.java))
            finish()
        }
        setContentView(R.layout.activity_signin)
        phone_box = findViewById(R.id.phone_box)
        pwd_box = findViewById(R.id.pwd_box)
        login_btn = findViewById(R.id.login_btn)
        newAccount_tv = findViewById(R.id.sighuphere)
        for_tv = findViewById(R.id.for_tv)
        for_tv.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@SigninActivity,
                    ForgotPasswordUser::class.java
                )
            )
        })
        databaseReference = FirebaseDatabase.getInstance().reference
        newAccount_tv.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@SigninActivity, RegisterActivity::class.java))
            finish()
        })
        login_btn.setOnClickListener(View.OnClickListener {
            val phone = phone_box.getText().toString().trim { it <= ' ' }
            val pwd = pwd_box.getText().toString().trim { it <= ' ' }
            if (phone == "") {
                phone_box.setError("Valid number is required")
                phone_box.requestFocus()
                return@OnClickListener
            }
            if (pwd.isEmpty()) {
                Toast.makeText(this@SigninActivity, "Enter Password", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (pwd.length < 8) {
                Toast.makeText(
                    this@SigninActivity,
                    "Password should have atleast 8 chars",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            databaseReference!!.child("Users")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(phone)) {
                            if (pwd == snapshot.child(phone).child("password").getValue(
                                    String::class.java
                                )
                            ) {
                                Toast.makeText(
                                    this@SigninActivity,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                LoginUserToApp(
                                    this@SigninActivity,
                                    snapshot.child(phone).child("name").getValue(
                                        String::class.java
                                    ),
                                    phone,
                                    snapshot.child(phone).child("email").getValue(
                                        String::class.java
                                    ),
                                    snapshot.child(phone).child("image").getValue(
                                        String::class.java
                                    )
                                )
                                startActivity(Intent(this@SigninActivity, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(
                                    this@SigninActivity,
                                    "Check your Password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@SigninActivity,
                                "No user Registered with this Phone",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        })
    }

    companion object {
        fun LoginUserToApp(
            context: Context,
            uname: String?,
            phone: String?,
            email: String?,
            image: String?
        ) {
            val editor = context.getSharedPreferences("USER", MODE_PRIVATE).edit()
            editor.putString("phone", phone)
            editor.putString("uname", uname)
            editor.putString("email", email)
            editor.putString("image", image)
            editor.apply()
        }
    }
}