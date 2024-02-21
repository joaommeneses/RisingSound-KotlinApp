package com.twinkle.myapplication.auth
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.twinkle.myapplication.R
import com.twinkle.myapplication.databinding.ActivityLaunchBinding

class LaunchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaunchBinding
    private lateinit var welcomeFragmentContainer: View
    private lateinit var loginFragmentContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
