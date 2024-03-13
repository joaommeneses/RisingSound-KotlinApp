package com.risingsound.kotlinapp.listener

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.risingsound.kotlinapp.R
import com.risingsound.kotlinapp.adapters.VideosPagerAdapter

class LPListenerFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showNotificationConsentDialog()
        return inflater.inflate(R.layout.fragment_land_page_listener, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.videosViewPager)
        viewPager.adapter = VideosPagerAdapter(this)
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }

    private fun showNotificationConsentDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Enable Notifications")
            .setMessage("Would you like to receive notifications about new live events and updates?")
            .setPositiveButton("Yes") { dialog, which ->
                // User consented to receive notifications
                // This is where you would either trigger FCM registration directly
                // or flag the user's preference to enable it elsewhere in your app's flow
            }
            .setNegativeButton("No", null)
            .show()
    }
}

