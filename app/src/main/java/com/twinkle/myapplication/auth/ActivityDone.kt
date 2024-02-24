package com.twinkle.myapplication.auth

import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.twinkle.myapplication.AppContext
import com.twinkle.myapplication.MainActivity
import com.twinkle.myapplication.R
import com.twinkle.myapplication.databinding.ActivityDoneBinding

class ActivityDone : AppCompatActivity() {

    private lateinit var binding: ActivityDoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userType = intent.getStringExtra("USER_TYPE") ?: "Listener" // Default to Listener if no user type is provided
        val colorRes = if (userType == "Listener") R.color.listener_color else R.color.musician_color
        val color = ContextCompat.getColor(this, colorRes)

        // Get the circle and checkmark drawables
        val circleDrawable = ContextCompat.getDrawable(this, R.drawable.btn_circle_done)?.mutate() // Call mutate to not share its state with any other drawable
        val checkDrawable = ContextCompat.getDrawable(this, R.drawable.ic_check)

        // Wrap and tint the circle drawable
        if (circleDrawable != null) {
            DrawableCompat.setTint(DrawableCompat.wrap(circleDrawable), color)
        }

        // Create a new LayerDrawable with the circle and checkmark
        val layers = if (circleDrawable != null && checkDrawable != null) {
            arrayOf<Drawable>(circleDrawable, checkDrawable)
        } else {
            arrayOf<Drawable>() // This should not happen, just a fallback
        }
        val layerDrawable = LayerDrawable(layers)

        // Set the LayerDrawable as the source for the ImageView
        binding.ivCircleWithCheck.setImageDrawable(layerDrawable)

        // Post a delayed Runnable to start the LandingPageActivity after 1.5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            startMainActivity(userType)
        }, 1500) // Delay in milliseconds
    }

    private fun startMainActivity(userType: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("USER_TYPE", userType)
        }
        print(userType)
        // Store user type in the global application context
        val app = application as AppContext
        app.userType = userType // "Listener" or "Musician"
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}








