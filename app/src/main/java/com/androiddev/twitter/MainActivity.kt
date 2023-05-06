package com.androiddev.twitter

import android.content.ActivityNotFoundException
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.androiddev.twitter.PostModule
import com.androiddev.twitter.MyViewHolder_Home
import com.firebase.ui.database.FirebaseRecyclerOptions
import android.os.Bundle
import com.androiddev.twitter.R
import android.content.Intent
import com.androiddev.twitter.ComposeActivity
import com.androiddev.twitter.AccountActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import android.text.TextUtils
import com.squareup.picasso.Picasso
import android.view.ViewGroup
import android.view.LayoutInflater
import android.content.DialogInterface
import android.net.Uri
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var toggle : ActionBarDrawerToggle
lateinit var imageButton : ImageView
    lateinit var rv: RecyclerView
    lateinit  var profile_btn: ImageView
    lateinit  var databaseReference: DatabaseReference
    lateinit  var order_Adapter: FirebaseRecyclerAdapter<PostModule, MyViewHolder_Home>
    lateinit  var orders_options: FirebaseRecyclerOptions<PostModule>
    lateinit  var compose_btn: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         imageButton = findViewById(R.id.drw_img)

        val drawerlyout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        val preferences = getSharedPreferences("USER", MODE_PRIVATE)
        val image = preferences.getString("image", "")
        Picasso.with(this@MainActivity).load(image)
            .placeholder(R.drawable.baseline_waving_hand_24).into(imageButton)
        imageButton.setOnClickListener {
            // Open the navigation drawer when the image button is clicked
            drawerlyout.openDrawer(GravityCompat.START)
        }

toggle= ActionBarDrawerToggle(this,drawerlyout,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawerlyout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_terms_conditions -> launchMarket()
                R.id.nav_share -> shareApps()
                R.id.nav_rate_us -> launchMarket()
                R.id.nav_logout -> logout()
            }
            true
        }

        rv = findViewById(R.id.rv)
        profile_btn = findViewById(R.id.profile_btn)
        compose_btn = findViewById(R.id.compose_btn)
        compose_btn.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    ComposeActivity::class.java
                )
            )
        })
        profile_btn.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    AccountActivity::class.java
                )
            )
        })
        rv.setLayoutManager(LinearLayoutManager(this))
        rv.setHasFixedSize(true)
        load_orderAdapter()
    }

    fun load_orderAdapter() {
        databaseReference = FirebaseDatabase.getInstance().reference.child("Posts")
        orders_options = FirebaseRecyclerOptions.Builder<PostModule>()
            .setQuery(databaseReference!!, PostModule::class.java).build()
        order_Adapter = object : FirebaseRecyclerAdapter<PostModule, MyViewHolder_Home>(
            orders_options!!
        ) {
            /* access modifiers changed from: protected */
            public override fun onBindViewHolder(
                holder: MyViewHolder_Home,
                i: Int,
                module: PostModule
            ) {
                if (!TextUtils.isEmpty(module.image)) {
                    Picasso.with(this@MainActivity).load(module.image)
                        .placeholder(R.drawable.baseline_waving_hand_24).into(holder.profile)
                }
                // Picasso.with(MainActivity.this).load(module.image).placeholder(R.drawable.ic_twitter_compose).into(holder.profile);
                holder.textView.text = module.text
                holder.uname.text = module.uname
                //                holder.likes_tv.setText(module.likes+"");
            }

            override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyViewHolder_Home {
                return MyViewHolder_Home(
                    LayoutInflater.from(viewGroup.context)
                        .inflate(R.layout.home_adapter_layout, viewGroup, false)
                )
            }
        }
        //        this.order_Adapter = order_Adapter;
        order_Adapter.startListening()
        rv!!.adapter = order_Adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Confirm" as CharSequence)
        builder.setMessage("Are you sure you want to Exit?" as CharSequence)
        builder.setPositiveButton(
            "No" as CharSequence,
            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
        builder.setNegativeButton(
            "Exit" as CharSequence,
            DialogInterface.OnClickListener { dialogInterface, i -> super@MainActivity.onBackPressed() })
        builder.show()
    }
     fun logout(){


         val sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE)
         sharedPreferences.edit().clear().commit()
         startActivity(Intent(applicationContext, SigninActivity::class.java))
         finish()

     }


    private fun shareApps() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage =
                """ ${shareMessage}https://play.google.com/store/apps/details?id=${applicationContext.packageName}
                """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {

        }
    }
    private fun launchMarket() {
        val uri: Uri = Uri.parse("market://details?id=${applicationContext.packageName}")
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(myAppLinkToMarket)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(applicationContext,
                getString(R.string.not_able_to_open),
                Toast.LENGTH_SHORT).show()
        }
    }



}