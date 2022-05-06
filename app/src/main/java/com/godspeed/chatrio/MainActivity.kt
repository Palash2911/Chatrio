package com.godspeed.chatrio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.Toast
import com.godspeed.chatrio.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import java.util.concurrent.TimeUnit
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private val TAG = "Login"
    private val auth = FirebaseAuth.getInstance()
    private var storedVerificationId: String = ""
    private lateinit var binding: ActivityMainBinding
    private val db = Firebase.firestore
    private lateinit var  messaging:FirebaseMessaging

    override fun onStart() {
        super.onStart()
        messaging = FirebaseMessaging.getInstance();
        if(auth.currentUser != null){
            binding.layoutLoadingProfile.visibility = View.VISIBLE
            binding.authCardView.visibility = View.GONE
//            db.collection("Profiles").document(auth.currentUser!!.uid).get()
//                .addOnCompleteListener{task2->
//                    if(task2.result?.exists() == true){
//                        val intent = Intent(this, Homepage::class.java)
//                        startActivity(intent)
//                        finish()
//                        Snackbar.make(binding.root, "Welcome !!", Snackbar.LENGTH_SHORT).show();
//                    } else {
//                        val intent = Intent(this, Profile::class.java)
//                        startActivity(intent)
//                        finish()
//                    }
//                }
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this, "Welcome !!", Toast.LENGTH_SHORT).show()
        } else {
            binding.layoutLoadingProfile.visibility = View.GONE
            binding.authCardView.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.godspeed.chatrio.R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.verify.isEnabled = false;
        binding.verify.isClickable = false;
        binding.getotp.setOnClickListener{
            if(sendOtp())
            {
                binding.verify.isEnabled = true;
                binding.verify.isClickable = true;
                binding.authProgress.visibility = View.GONE
            }
        }
        binding.verify.setOnClickListener{
            verifyOtp()
        }
    }

    private fun sendOtp(): Boolean {
        if (binding.phone.text.isEmpty() || binding.phone.text.length < 10){
            Toast.makeText(this, "Please enter valid phone number !!", Toast.LENGTH_SHORT).show()
            return false;
        }
        binding.authProgress.visibility = View.VISIBLE
        val phoneNumber = "+91 " + binding.phone.text.toString()

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        return true;
    }
    private fun verifyOtp(){
        if (binding.otp.text.isEmpty() || binding.otp.text.length < 6){
            Toast.makeText(this, "Please enter valid 6 digit OTP !!", Toast.LENGTH_SHORT).show()
            return
        }
        val otpNumber = binding.otp.text.toString()
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, otpNumber)
        signInWithPhoneAuthCredential(credential)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted: $credential")
            binding.authProgress.visibility = View.GONE
            Firebase.messaging.subscribeToTopic("all").addOnSuccessListener {
                Log.e("","Added to notification list");
            }
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(applicationContext, "Invalid Request! Contact Developer!", Toast.LENGTH_SHORT).show()
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Toast.makeText(applicationContext, "SMS Quota Reached! Contact Developer!", Toast.LENGTH_SHORT).show()
            }
            reset()
            binding.authProgress.visibility = View.GONE
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:$verificationId")
            Toast.makeText(this@MainActivity, "OTP Sent Successfully :)", Toast.LENGTH_SHORT).show()
            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
//            resendToken = token
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    db.collection("Profiles").document(auth.currentUser!!.uid).get()
//                        .addOnCompleteListener{task2->
//                            if(task2.result?.exists() == true){
//                                val intent = Intent(this, Homepage::class.java)
//                                startActivity(intent)
//                                finish()
//                                Toast.makeText(this, "Welcome Champion !! ", Toast.LENGTH_SHORT).show()
//                            } else {
//                                val intent = Intent(this, Profile::class.java)
//                                startActivity(intent)
//                                finish()
//                            }
//                        }
                        Toast.makeText(this, "Welcome Champion !! ", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Profile::class.java)
                        startActivity(intent)
                        finish()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this@MainActivity, "Invalid Code Try Again !!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Something went wrong !!", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.authProgress.visibility = View.GONE
            }
    }

    private fun reset (){
        binding.phone.text.clear()
        binding.otp.text.clear()
        auth.signOut()
    }
}