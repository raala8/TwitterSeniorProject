package com.androiddev.twitter

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import android.graphics.Bitmap
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.FirebaseStorage
import android.os.Bundle
import com.androiddev.twitter.R
import com.androiddev.twitter.RegisterActivity
import android.content.Intent
import com.androiddev.twitter.SigninActivity
import android.text.TextWatcher
import android.text.Editable
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.android.gms.tasks.OnSuccessListener
import com.androiddev.twitter.UserModule
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.storage.OnProgressListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.database.DatabaseError
import android.content.DialogInterface
import android.app.Activity
import android.provider.MediaStore
import android.content.ContentResolver
import android.net.Uri
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.Exception

class RegisterActivity() : AppCompatActivity() {
    lateinit  var editTextPhone: TextInputEditText
    lateinit  var email_box: TextInputEditText
    lateinit   var buttonContinue: FrameLayout
    lateinit   var name_box: TextInputEditText
    lateinit  var pwd_box: TextInputEditText
    lateinit  var pwd_confrim_box: TextInputEditText
    lateinit   var databaseReference: DatabaseReference
    lateinit   var loginhere_tv: TextView
    lateinit  var email: String
    lateinit   var chooseImage_btn: FrameLayout
    lateinit   var imageFilePath: Uri
    lateinit   var imageToStore: Bitmap
    private lateinit var byteArrayOutputStream: ByteArrayOutputStream
    private lateinit var imageInBytes: ByteArray
    private lateinit var progressBar: ProgressBar
    private val reference = FirebaseStorage.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        editTextPhone = findViewById(R.id.phone_box)
        buttonContinue = findViewById(R.id.signup_btn)
        name_box = findViewById(R.id.name_box)
        pwd_box = findViewById(R.id.pwd_box)
        pwd_confrim_box = findViewById(R.id.cpwd_box)
        email_box = findViewById(R.id.email_box)
        loginhere_tv = findViewById(R.id.loginhere_tv)
        progressBar = findViewById(R.id.progressbar)
        chooseImage_btn = findViewById(R.id.addImage_layout)
        imageView = findViewById(R.id.crtImage_iv)
        loginhere_tv.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@RegisterActivity, SigninActivity::class.java))
            finish()
        })
        email_box.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // other stuffs
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // other stuffs
            }
        })
        buttonContinue.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val phoneNumber = editTextPhone.getText().toString().trim { it <= ' ' }
                val name = name_box.getText().toString().trim { it <= ' ' }
                val pwd = pwd_box.getText().toString().trim { it <= ' ' }
                val pwd_cnfrm = pwd_confrim_box.getText().toString().trim { it <= ' ' }
                val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
                email = email_box.getText().toString().trim()
                if (email.matches(emailPattern)) {
                } else {
                    email_box.setError("Valid email is required")
                    email_box.requestFocus()
                }
                if (name.isEmpty()) {
                    name_box.setError("Name is required")
                    name_box.requestFocus()
                    return
                }
                if (pwd.isEmpty() || pwd_cnfrm.isEmpty()) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please Enter Passwords",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                if (pwd != pwd_cnfrm) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please confirm Passwords",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                if (pwd.length < 8) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Password must be of 8 chars",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                if (phoneNumber.isEmpty()) {
                    editTextPhone.setError("Valid number is required")
                    editTextPhone.requestFocus()
                    return
                }
                if (email!!.isEmpty()) {
                    email_box.setError("Valid email is required")
                    email_box.requestFocus()
                    return
                }
                if (imageView.getDrawable() == null) {
                    Toast.makeText(this@RegisterActivity, "Please Select Image", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                val fileRef = reference.child(
                    System.currentTimeMillis().toString() + "." + getFileExtension(imageFilePath)
                )
                databaseReference = FirebaseDatabase.getInstance().reference.child("Users")


                //code to register user
                databaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(phoneNumber)) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "User Already Exists",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }
                        fileRef.putFile((imageFilePath)!!).addOnSuccessListener(object :
                            OnSuccessListener<UploadTask.TaskSnapshot?> {
                            override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot?) {
                                fileRef.downloadUrl.addOnSuccessListener(object :
                                    OnSuccessListener<Uri> {
                                    override fun onSuccess(uri: Uri) {
                                        val module = UserModule(
                                            name,
                                            phoneNumber,
                                            email,
                                            pwd,
                                            uri.toString()
                                        )
                                        databaseReference!!.child(phoneNumber).setValue(module)
                                            .addOnCompleteListener(
                                                OnCompleteListener {
                                                    val intent = Intent(
                                                        this@RegisterActivity,
                                                        SigninActivity::class.java
                                                    )
                                                    intent.flags =
                                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    startActivity(intent)
                                                })
                                    }
                                })
                            }
                        }).addOnProgressListener(object :
                            OnProgressListener<UploadTask.TaskSnapshot> {
                            override fun onProgress(snapshot: UploadTask.TaskSnapshot) {
                                progressBar.setVisibility(View.VISIBLE)
                                buttonContinue.setVisibility(View.GONE)
                            }
                        }).addOnFailureListener(object : OnFailureListener {
                            override fun onFailure(e: Exception) {
                                progressBar.setVisibility(View.GONE)
                                buttonContinue.setVisibility(View.VISIBLE)
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Uploading Failed !!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })


//
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            }
        })
        progressBar.setVisibility(View.GONE)
        databaseReference = FirebaseDatabase.getInstance().reference
        chooseImage_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val builder = AlertDialog.Builder(this@RegisterActivity)
                builder.setTitle("Choose")
                builder.setMessage("From Where do you want that picture?")
                builder.setPositiveButton("Cancel", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        dialog.dismiss()
                    }
                })
                builder.setNegativeButton("Choose Image", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        val intent = Intent()
                        intent.type = "image/*"
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(intent, GET_IMAGE_REQUEST_CODE)
                    }
                })
                builder.show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == GET_IMAGE_REQUEST_CODE) && (resultCode == RESULT_OK) && (data != null) && (data.data != null)) {
            imageFilePath = data.data!!
            try {
                imageToStore = MediaStore.Images.Media.getBitmap(contentResolver, imageFilePath)
                imageView!!.setImageBitmap(imageToStore)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun getFileExtension(mUri: Uri?): String? {
        val cr = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType((mUri)!!))
    }

    companion object {
       lateinit var imageView: ImageView
        private val GET_IMAGE_REQUEST_CODE = 100
    }
}