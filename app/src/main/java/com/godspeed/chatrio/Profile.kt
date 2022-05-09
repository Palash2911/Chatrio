package com.godspeed.chatrio

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import com.godspeed.chatrio.databinding.ActivityMainBinding
import com.godspeed.chatrio.databinding.ActivityProfileBinding
import com.godspeed.chatrio.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.HashMap

class Profile : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val auth = FirebaseAuth.getInstance()
    var setupimg: Uri ?= null
    var dialog: ProgressDialog? = null
    var storage: FirebaseStorage? = null
    var db: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var usern = binding.Name
        binding.number.text = auth.currentUser!!.phoneNumber.toString()
        var cntbtn = binding.cntbtn
        storage = FirebaseStorage.getInstance()
        db = FirebaseDatabase.getInstance()


        val profile:HashMap<String, String> = HashMap()

        binding.profileimg.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 45)
        }
        cntbtn.setOnClickListener {
            var fl1=0;
            var fl2=0;
            if(setupimg == null)
            {
                Toast.makeText(this, "Please Add Profile Image !!", Toast.LENGTH_SHORT).show()
                fl1=1;
            }
            if(usern.text.isEmpty() || usern.text.length<3)
            {
                fl2=1;
                Toast.makeText(this, "Invalid Username !!", Toast.LENGTH_SHORT).show()
            }
            else if(fl1!=1 && fl2!=1)
            {
                val ref = storage!!.reference.child("Profile")
                    .child(auth.uid!!)
                ref.putFile(setupimg!!).addOnCompleteListener{ task ->
                    if(task.isSuccessful)
                    {
                        ref.downloadUrl.addOnCompleteListener{ uri->
                            val imgurl = uri.toString()
                            val uid = auth.uid;
                            val phone = auth.currentUser!!.phoneNumber
                            val name: String = binding.Name.text.toString()
                            val user = User(uid, name, phone, imgurl);
                            db!!.reference
                                .child("Chatters")
                                .child(uid!!)
                                .setValue(user)
                                .addOnCompleteListener {
                                    val intent = Intent(this, Homepage::class.java)
                                    startActivity(intent);
                                    Toast.makeText(this, "Account Created :)", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    else
                    {
                        Log.d("Error", task.toString());
                        Toast.makeText(this, "Something Went Wrong !!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data!=null)
        {
            if(data.data != null)
            {
                val uri = data.data
                val storage = FirebaseStorage.getInstance()
                val time = Date().time
                val reference = storage.reference
                reference.putFile(uri!!).addOnCompleteListener{ task->
                    if(task.isSuccessful)
                    {
                        reference.downloadUrl.addOnCompleteListener { uri->
                            val fp = uri.toString()
                            val obj = HashMap<String, Any>()
                            obj["image"] = fp
                            db!!.reference
                                .child("Chatters")
                                .child(FirebaseAuth.getInstance().uid!!)
                                .updateChildren(obj).addOnSuccessListener { }
                        }
                    }
                }
            binding.profileimg.setImageURI(data?.data);
            setupimg = data?.data
            }
        }
    }
}