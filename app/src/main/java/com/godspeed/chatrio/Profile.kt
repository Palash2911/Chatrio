package com.godspeed.chatrio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.godspeed.chatrio.databinding.ActivityMainBinding
import com.godspeed.chatrio.databinding.ActivityProfileBinding

class Profile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var usern = binding.Name
        var num = binding.number
        var cntbtn = binding.cntbtn

        cntbtn.setOnClickListener {
            var fl1=0;
            var fl2=0;
            if(usern.text.isEmpty() || usern.text.length<3)
            {
                fl1=1;
                Toast.makeText(this, "Invalid Username !!", Toast.LENGTH_SHORT).show()
            }
            else if(num.text.isEmpty() || num.text.length<10)
            {
                fl2=1;
                Toast.makeText(this, "Invalid Phone Number !!", Toast.LENGTH_SHORT).show()
            }
            else if(fl1!=1 && fl2!=1)
            {
                Toast.makeText(this, "Account Created !!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Homepage::class.java)
                startActivity(intent)
            }
        }
    }
}