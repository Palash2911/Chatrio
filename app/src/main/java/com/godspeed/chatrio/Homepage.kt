package com.godspeed.chatrio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.godspeed.chatrio.adapter.Useradapter
import com.godspeed.chatrio.databinding.ActivityHomepageBinding
import com.godspeed.chatrio.model.User
import com.google.firebase.database.FirebaseDatabase

var binding: ActivityHomepageBinding?=null
var db: FirebaseDatabase?=null
var users: ArrayList<User>?=null
var useradapter: Useradapter?=null

class Homepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setContentView(R.layout.activity_homepage)
    }
}