package com.twinkle.myapplication.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.twinkle.myapplication.AppContext
import com.twinkle.myapplication.databinding.ActivityUserTypeBinding

class UserTypeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the listeners for the buttons
        binding.buttonListener.setOnClickListener {
            startMainActivity("Listener")
        }

        binding.buttonMusician.setOnClickListener {
            startMainActivity("Musician")
        }
    }

    private fun startMainActivity(userType: String) {
        val intent = Intent(this, ActivityDone::class.java).apply {
            putExtra("USER_TYPE", userType)
        }
        //store user type (can be accessed in all application)
        val app = application as AppContext
        app.userType = userType // "Listener" or "Musician"
        startActivity(intent)
    }
}
