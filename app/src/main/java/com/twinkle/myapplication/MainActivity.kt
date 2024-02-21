package com.twinkle.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.twinkle.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val userType = intent.getStringExtra("USER_TYPE")
        val graphInflater = navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_graph)

        val destinationId = when (userType) {
            "Listener" -> R.id.fragmentListenerLandingPage
            "Musician" -> R.id.fragmentMusicianLandingPage
            else -> throw IllegalStateException("Unknown UserType")
        }
        navGraph.setStartDestination(destinationId)
        navController.graph = navGraph

        binding.bottomNavigation.setupWithNavController(navController)
    }
}
