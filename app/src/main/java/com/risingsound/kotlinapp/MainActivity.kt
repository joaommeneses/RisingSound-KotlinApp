package com.risingsound.kotlinapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.risingsound.kotlinapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var userType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Retrieve the user type passed from the previous activity
        userType = intent.getStringExtra("USER_TYPE") ?: "Listener" // Default to "Listener" if not provided
        print("MAIN ACTIVITY $userType")
        // Set the bottom navigation view's selected listener
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_live_events -> {
                    // Navigate to the Live Events Fragment
                    val liveEventsDestId = if (userType == "Listener") {
                        R.id.fragmentListenerLandingPage
                    } else {
                        //R.id.fragmentMusicianLandingPage
                        R.id.fragmentListenerLandingPage
                    }
                    navController.navigate(liveEventsDestId)
                    true
                }
                R.id.navigation_search -> {
                    // Navigate to the Search Fragment
                    // You might have separate fragments for Listener and Musician search
                    val searchDestId = if (userType == "Listener") {
                        R.id.fragmentListenerSearch
                    } else {
                        R.id.fragmentListenerSearch //Right now use same search page. no need to duplicate pages
                    }
                    navController.navigate(searchDestId)
                    true // Return true to display the item as the selected item
                }
                R.id.navigation_messages -> {
                    // Navigate to the Messages Fragment
                    val messagesDestId = if (userType == "Listener") {
                        R.id.fragmentListenerPrivateMessages
                    } else {
                        R.id.fragmentMusicianPrivateMessages
                    }
                    navController.navigate(messagesDestId)
                    true
                }
                R.id.navigation_profile -> {
                    // Navigate to the Profile Fragment
                    val profileDestId = if (userType == "Listener") {
                        R.id.fragmentListenerProfile
                    } else {
                        R.id.fragmentMusicianProfile
                    }
                    navController.navigate(profileDestId)
                    true
                }
                else -> false
            }
        }
        if(userType == "Listener"){
            navController.navigate(R.id.fragmentListenerLandingPage)
        }else{
            navController.navigate(R.id.fragmentListenerLandingPage)
        }
    }
}
