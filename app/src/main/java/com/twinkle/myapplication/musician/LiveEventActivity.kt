package com.twinkle.myapplication.musician

import android.Manifest
import android.R
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.twinkle.myapplication.databinding.ActivityLiveEventBinding

class LiveEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLiveEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if the permissions are already granted


        if (!hasPermissionsGranted(PERMISSIONS_REQUIRED)) {
            requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)
        } else {
            // Permissions are already granted, set up the live broadcast
            setupLiveBroadcast()
        }
    }

    private fun hasPermissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.size == PERMISSIONS_REQUIRED.size) {
                var allGranted = true
                for (grantResult in grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        allGranted = false
                        break
                    }
                }
                if (allGranted) {
                    setupLiveBroadcast()
                } else {
                    // Permission request was denied.
                    // Handle the case when permissions are not granted
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setupLiveBroadcast() {
        // TODO: Set up the user interface and the MediaStream for broadcasting
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 1
        private val PERMISSIONS_REQUIRED = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }
}
