package com.risingsound.kotlinapp.musician

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.risingsound.kotlinapp.R
import com.risingsound.kotlinapp.databinding.ActivityLiveEventBinding

class LiveEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLiveEventBinding
    private var donationListenerRegistration: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartBroadcast.setOnClickListener {
            toggleLiveBroadcast()
        }

        val backButton = findViewById<ImageView>(R.id.iv_ic_back)

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun toggleLiveBroadcast() {
        val isBroadcasting = binding.tvLiveBanner.visibility != View.VISIBLE
        binding.tvLiveBanner.visibility = if (isBroadcasting) View.VISIBLE else View.GONE
        if (isBroadcasting) {
            startListeningForDonations()
        } else {
            stopListeningForDonations()
        }
    }

    private fun startListeningForDonations() {
        val musicianId = 1  // Assuming the musician's ID is 1 for demo purposes
        val donationsRef = FirebaseFirestore.getInstance().collection("donations")
        donationListenerRegistration = donationsRef.whereEqualTo("musicianId", musicianId)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, "Error listening to donation updates.", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                for (dc in snapshots?.documentChanges ?: emptyList()) {
                    if (dc.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                        val listenerId = dc.document.getDouble("listenerId") ?: 0
                        val amount = dc.document.getDouble("amount") ?: 0.0
                        Toast.makeText(this, "New donation from $listenerId: $amount€", Toast.LENGTH_LONG).show()
                    }
                }
            }
    }

    private fun stopListeningForDonations() {
        donationListenerRegistration?.remove()
    }
}
