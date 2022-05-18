package com.godspeed.chatrio.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.godspeed.chatrio.R
import com.godspeed.chatrio.model.User
import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.FirebaseDatabase




class Useradapter(var cnt: Context, var userlist: ArrayList<User>):RecyclerView.Adapter<Useradapter.UserViewHolder>() {

    var db: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null

    inner class UserViewHolder(val v: View):RecyclerView.ViewHolder(v){
        var profileimg = v.findViewById<ImageView>(R.id.cardimg)
        var name = v.findViewById<TextView>(R.id.Name)
        var lc = v.findViewById<TextView>(R.id.lastchat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val v=inflater.inflate(R.layout.activity_homepage,parent,false)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val chatter = userlist[position];
        holder.name.text = chatter.name;
        holder.lc.text = chatter.number;
        Glide.with(cnt).load(chatter.image)
            .placeholder(R.drawable.ic_baseline_avatar)
            .into(holder.profileimg)

    }

    override fun getItemCount(): Int = userlist.size;
}