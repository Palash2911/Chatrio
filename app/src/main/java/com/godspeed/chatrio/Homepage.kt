package com.godspeed.chatrio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
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
var user: User?=null

class Homepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        db = FirebaseDatabase.getInstance()
        users = ArrayList<User>()
        useradapter = Useradapter(this, users!!)
        binding!!.chatrec.layoutManager = LinearLayoutManager(this)
        db!!.reference.child("Chatters")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(User::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        binding!!.chatrec.adapter = useradapter
        db!!.reference.child("Chatters").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users!!.clear();
                for(ss in snapshot.children){
                    val user:User?=ss.getValue(User::class.java)
                    if(!user!!.uid.equals(FirebaseAuth.getInstance().uid))
                    {
                        users!!.add(user)
                    }
                }
                useradapter!!.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}

        })
    }

    override fun onResume() {
        super.onResume()
        val currenId = FirebaseAuth.getInstance().uid
        db!!.reference.child("presence")
            .child(currenId!!).setValue("Online")
    }

    override fun onPause() {
        super.onPause()
        val currenId = FirebaseAuth.getInstance().uid
        db!!.reference.child("presence")
            .child(currenId!!).setValue("Offline")
    }
}