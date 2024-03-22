package com.risingsound.kotlinapp.listener

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.risingsound.kotlinapp.R

class FavArtistsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_artists_listeners)

        val backButton = findViewById<ImageView>(R.id.iv_ic_back)

        backButton.setOnClickListener {
            finish()
        }
    }
}