package com.androiddev.twitter

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.androiddev.twitter.PostModule
import com.androiddev.twitter.MyViewHolder_Account
import com.firebase.ui.database.FirebaseRecyclerOptions
import android.os.Bundle
import com.androiddev.twitter.R
import androidx.recyclerview.widget.LinearLayoutManager
import android.content.SharedPreferences
import com.google.firebase.database.FirebaseDatabase
import android.text.TextUtils
import com.squareup.picasso.Picasso
import android.content.Intent
import com.androiddev.twitter.EditTweetActivity
import android.view.ViewGroup
import android.view.LayoutInflater

class AccountActivity : AppCompatActivity() {
  lateinit var rv: RecyclerView
    lateinit  var databaseReference: DatabaseReference
   lateinit var order_Adapter: FirebaseRecyclerAdapter<PostModule, MyViewHolder_Account>
    lateinit var orders_options: FirebaseRecyclerOptions<PostModule>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        rv = findViewById(R.id.rv)
        rv.setLayoutManager(LinearLayoutManager(this))
        rv.setHasFixedSize(true)
        val preferences = getSharedPreferences("USER", MODE_PRIVATE)
        val uname = preferences.getString("uname", "")
        load_orderAdapter(uname)
    }

    fun load_orderAdapter(uname: String?) {
        databaseReference = FirebaseDatabase.getInstance().reference.child("Posts")
        val query = databaseReference!!.orderByChild("uname").equalTo(uname)
        orders_options =
            FirebaseRecyclerOptions.Builder<PostModule>().setQuery(query, PostModule::class.java)
                .build()

//        orders_options = new FirebaseRecyclerOptions.Builder<PostModule>().setQuery(databaseReference, PostModule.class).build();
        order_Adapter = object : FirebaseRecyclerAdapter<PostModule, MyViewHolder_Account>(
            orders_options!!
        ) {
            /* access modifiers changed from: protected */
            public override fun onBindViewHolder(
                holder: MyViewHolder_Account,
                i: Int,
                module: PostModule
            ) {
                if (!TextUtils.isEmpty(module.image)) {
                    Picasso.with(this@AccountActivity).load(module.image)
                        .placeholder(R.drawable.baseline_waving_hand_24).into(holder.profile)
                }
                holder.textView.text = module.text
                holder.likes_tv.text = module.likes.toString() + ""
                holder.edit_btn.setOnClickListener {
                    val intent = Intent(this@AccountActivity, EditTweetActivity::class.java)
                    intent.putExtra("id", module.id)
                    intent.putExtra("text", module.text)
                    startActivity(intent)
                }
            }

            override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder_Account {
                return MyViewHolder_Account(
                    LayoutInflater.from(viewGroup.context)
                        .inflate(R.layout.account_adapter_layout, viewGroup, false)
                )
            }
        }
        //        this.order_Adapter = order_Adapter;
        order_Adapter.startListening()
        rv.adapter = order_Adapter
    }
}