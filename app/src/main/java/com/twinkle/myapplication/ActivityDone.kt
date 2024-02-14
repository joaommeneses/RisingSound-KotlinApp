package com.twinkle.myapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.twinkle.myapplication.databinding.ActivityDoneBinding

class ActivityDone : AppCompatActivity() {

    private lateinit var binding: ActivityDoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get the color passed from the previous activity
        val colorStr = intent.getStringExtra("BUTTON_COLOR") ?: "#000000" // Default to black if no color provided

        // get the circle and checkmark drawables
        val circleDrawable = ContextCompat.getDrawable(this, R.drawable.btn_circle_done)?.mutate() // Call mutate to not share its state with any other drawable
        val checkDrawable = ContextCompat.getDrawable(this, R.drawable.ic_check)

        // wrap and tint the circle drawable
        if (circleDrawable != null) {
            DrawableCompat.setTint(DrawableCompat.wrap(circleDrawable), Color.parseColor(colorStr))
        }

        // create a new LayerDrawable with the circle and checkmark
        val layers = if (circleDrawable != null && checkDrawable != null) {
            arrayOf<Drawable>(circleDrawable, checkDrawable)
        } else {
            arrayOf<Drawable>() // This should not happen, just a fallback
        }
        val layerDrawable = LayerDrawable(layers)

        // set the LayerDrawable as the source for the ImageView
        binding.ivCircleWithCheck.setImageDrawable(layerDrawable)

        // post a delayed Runnable to start the LandingPageActivity after 3 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            // create an intent to start LandingPageActivity
            val intent = Intent(this, LandingPage::class.java)
            // set flags to clear the activity stack and start a new task
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            // optionally add a transition animation
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            // finish the current activity
            finish()
        }, 3000) // Delay in milliseconds
    }
}







