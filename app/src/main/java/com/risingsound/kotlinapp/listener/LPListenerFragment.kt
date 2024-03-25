package com.risingsound.kotlinapp.listener

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.risingsound.kotlinapp.AppContext
import com.risingsound.kotlinapp.R
import com.risingsound.kotlinapp.adapters.VideosPagerAdapter

class LPListenerFragment : Fragment() {
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        checkNotificationConsent()
        return inflater.inflate(R.layout.fragment_land_page_listener, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.videosViewPager)
        viewPager.adapter = VideosPagerAdapter(this)
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL


        view.findViewById<AppCompatButton>(R.id.btn_donate).setOnClickListener {
            openDonationPopup()
        }

        val appContext = activity?.application as AppContext
        if (appContext.userType == "Musician") {
            val tvUsername: TextView = view.findViewById(R.id.tv_username)
            val ivUserProfile: ImageView = view.findViewById(R.id.iv_user_pfp)

            tvUsername.text = "joaommeneses"
            ivUserProfile.setImageResource(R.drawable.joaomeneses) // replace with actual drawable resource ID for joaomeneses
        }
    }

    private fun checkNotificationConsent() {
        // Access the AppContext's instance and its consentNotification variable
        val appContext = requireActivity().application as AppContext

        if (appContext.consentNotification == false) {
            showNotificationConsentDialog()
        }
    }

    private fun showNotificationConsentDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Enable Notifications")
            .setMessage("Would you like to receive notifications about new live events and updates?")
            .setPositiveButton("Yes") { _, _ ->
                // Set the consentNotification variable in AppContext to true
                val appContext = requireActivity().application as AppContext
                appContext.consentNotification = true

                // Here, trigger FCM registration or flag user's preference
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun openDonationPopup() {
        // Here, we're opening the DonationFragment as a dialog.
        val donationFragment = DonationFragment() // Replace with your actual DonationFragment class name if different
        donationFragment.show(parentFragmentManager, donationFragment.tag)
    }
}
