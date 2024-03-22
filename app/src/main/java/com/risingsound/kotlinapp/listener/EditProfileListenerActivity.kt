package com.risingsound.kotlinapp.listener

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.risingsound.kotlinapp.R

class EditProfileListenerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_listener)

        val backButton = findViewById<ImageView>(R.id.iv_ic_back)

        backButton.setOnClickListener {
            finish()
        }
    }
}