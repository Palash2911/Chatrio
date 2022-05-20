package com.godspeed.chatrio.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.godspeed.chatrio.Homepage
import com.godspeed.chatrio.R
import com.godspeed.chatrio.databinding.ActivityHomepageBinding
import com.godspeed.chatrio.databinding.ChatcardBinding
import com.godspeed.chatrio.model.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Useradapter(var cnt: Context, var userlist: ArrayList<User>):RecyclerView.Adapter<Useradapter.UserViewHolder>() {

    var db: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null

    inner class UserViewHolder(val view: View):RecyclerView.ViewHolder(view){
        val binding: ChatcardBinding = ChatcardBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val v=LayoutInflater.from(cnt).inflate(R.layout.chatcard,parent,false)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val chatter = userlist[position];
        Log.d("Tag", chatter.image.toString())
        holder.binding.nameperson.text = chatter.name
        holder.binding.lastchat.text = chatter.number
        Glide.with(cnt).load(chatter.image)
            .placeholder(R.drawable.ic_baseline_avatar)
            .dontAnimate()
            .into(holder.binding.cardimg)
    }

    override fun getItemCount(): Int = userlist.size;
}