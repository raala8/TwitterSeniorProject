package com.androiddev.twitter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.androiddev.twitter.R
import de.hdodenhof.circleimageview.CircleImageView

class MyViewHolder_Account(view: View) : RecyclerView.ViewHolder(view) {
    var textView: TextView
    var profile: CircleImageView
    var likes_tv: TextView
    var edit_btn: ImageView

    init {
        textView = view.findViewById(R.id.hm_tv)
        profile = view.findViewById(R.id.profile_image)
        likes_tv = view.findViewById(R.id.likes_tv)
        edit_btn = view.findViewById(R.id.edit_btn)  ?: throw IllegalStateException("edit_btn not found")
    }
}