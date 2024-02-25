package com.twinkle.myapplication.listener

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.twinkle.myapplication.R
import com.twinkle.myapplication.adapters.VideosPagerAdapter

class LPListenerFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private val videosList = listOf(R.raw.example, R.raw.example, R.raw.example) // same video for now


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_land_page_listener, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager = view.findViewById(R.id.videosViewPager)
        viewPager.adapter = VideosPagerAdapter(this)
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
    }
}

