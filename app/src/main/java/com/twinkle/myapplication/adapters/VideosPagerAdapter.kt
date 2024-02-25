package com.twinkle.myapplication.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.twinkle.myapplication.R
import com.twinkle.myapplication.listener.VideoFragment

// Adapter for ViewPager2
class VideosPagerAdapter(private val fragment: Fragment) : FragmentStateAdapter(fragment) {

    // Assuming you always want to use the same video for now, as an example.
    // Replace R.raw.example with the actual ID of your video in the raw folder.
    private val videoResourceIds = listOf(
        R.raw.example,
        R.raw.example1,
        R.raw.example2,
        R.raw.example3,
        R.raw.example,
        R.raw.example1,
        R.raw.example2,
        R.raw.example3,
        // The ID of your video in the raw folder
        // You can add more video IDs here if needed
    )

    override fun getItemCount(): Int = videoResourceIds.size

    override fun createFragment(position: Int): Fragment {
        // Convert the video resource ID to a String URI
        val videoUri = "android.resource://${fragment.requireContext().packageName}/${videoResourceIds[position]}"
        // Return a new instance of VideoFragment with the URI
        return VideoFragment.newInstance(videoUri)
    }
}
