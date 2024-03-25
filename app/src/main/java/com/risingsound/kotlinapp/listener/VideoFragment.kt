package com.risingsound.kotlinapp.listener

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.risingsound.kotlinapp.R

class VideoFragment : Fragment() {
    private lateinit var videoView: VideoView

    companion object {
        private const val ARG_VIDEO_URI = "video_uri"

        fun newInstance(videoUri: String): VideoFragment {
            val fragment = VideoFragment()
            val args = Bundle()
            args.putString(ARG_VIDEO_URI, videoUri)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoView = view.findViewById(R.id.videoView)
        val videoUri: String = arguments?.getString(ARG_VIDEO_URI) ?: return
        videoView.setVideoURI(null)
        videoView.setVideoURI(Uri.parse(videoUri))

        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true // Loop the video
            mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
            videoView.start()
        }

        val likeButton: ImageView = view.findViewById(R.id.likeButton)
        likeButton.setOnClickListener {
            // Toggle between liked and not liked states
            // This is just an example, adjust as per your logic and drawable resources
            val isLiked = it.tag as? Boolean ?: false
            if (isLiked) {
                likeButton.setImageResource(R.drawable.baseline_favorite_border_24) // Not liked state
            } else {
                likeButton.setImageResource(R.drawable.baseline_favorite_24) // Liked state
            }
            it.tag = !isLiked
        }
    }
}

