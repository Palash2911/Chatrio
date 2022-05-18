package com.godspeed.chatrio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.godspeed.chatrio.adapter.Useradapter
import com.godspeed.chatrio.databinding.ActivityHomepageBinding
import com.godspeed.chatrio.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

var binding: ActivityHomepageBinding?=null
var db: FirebaseDatabase?=null
var users: ArrayList<User>?=null
var useradapter: Useradapter?=null

class Homepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        db = FirebaseDatabase.getInstance()
        users = ArrayList<User>()
        useradapter = Useradapter(this, users!!)
        db!!.reference.child("Chatters")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        setContentView(R.layout.activity_homepage)
    }
}