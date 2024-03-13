package com.risingsound.kotlinapp.auth
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.risingsound.kotlinapp.R
import com.risingsound.kotlinapp.databinding.ActivityLaunchBinding

class LaunchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaunchBinding
    private lateinit var welcomeFragmentContainer: View
    private lateinit var loginFragmentContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d(TAG, "FCM Token: $token")
            Toast.makeText(baseContext, "FCM Token: $token", Toast.LENGTH_SHORT).show()
        }


        // Initialize fragment containers
        welcomeFragmentContainer = findViewById(R.id.welcomeFragmentContainer)
        loginFragmentContainer = findViewById(R.id.loginFragmentContainer)

        Handler(Looper.getMainLooper()).postDelayed({
            // Start logo animation
            if(isDestroyed || isFinishing) {
                return@postDelayed
            }
            // Slide up the Welcome Fragment
            val welcomeFragment = WelcomeFragment()
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_up_login, // Enter animation for the LoginFragment
                    0 // No exit animation for the previous fragment
                )
                .replace(R.id.welcomeFragmentContainer, welcomeFragment)
                .commit()

            // Make the fragment container visible
            welcomeFragmentContainer.visibility = View.VISIBLE

        }, 2000)
    }

    fun switchToLoginFragment() {
        val loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_up_login, // Enter animation for the LoginFragment
                0 // No exit animation for the previous fragment
            )
            .replace(R.id.loginFragmentContainer, loginFragment)
            .commit()

        // Hide WelcomeFragment container and show LoginFragment container
        welcomeFragmentContainer.visibility = View.GONE
        loginFragmentContainer.visibility = View.VISIBLE
        binding.ivLogotype.visibility = View.GONE
    }
}
