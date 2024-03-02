package com.twinkle.myapplication.musician

import android.Manifest
import android.R
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.*
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.twinkle.myapplication.databinding.ActivityLiveEventBinding

class LiveEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLiveEventBinding
    private lateinit var cameraManager: CameraManager
    private var cameraDevice: CameraDevice? = null
    private var cameraCaptureSession: CameraCaptureSession? = null
    private var cameraId: String? = null

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

        binding.btnStartBroadcast.setOnClickListener {
            toggleLiveBroadcast()
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            // Check if all requested permissions were granted
            val allPermissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allPermissionsGranted) {
                // Permissions were granted, proceed with setting up the live broadcast
                setupLiveBroadcast()
            } else {
                // Handle the case when permissions are denied
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleLiveBroadcast() {
        // Toggle the visibility of the LIVE banner
        if (binding.tvLiveBanner.visibility == View.GONE) {
            binding.tvLiveBanner.visibility = View.VISIBLE
            // Start the broadcast here
        } else {
            binding.tvLiveBanner.visibility = View.GONE
            // Stop the broadcast here
        }
    }
    private fun setupLiveBroadcast() {
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        // Find the back camera, assume it's the first camera
        cameraId = cameraManager.cameraIdList.find { id ->
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val cameraDirection = characteristics.get(CameraCharacteristics.LENS_FACING)
            cameraDirection == CameraCharacteristics.LENS_FACING_BACK
        }

        // Proceed to open the camera
        openCamera()
    }

    private fun openCamera() {
        cameraId?.let { id ->
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return // Check permissions again
            }
            cameraManager.openCamera(id, cameraStateCallback, null)
        }
    }

    // Define callbacks for camera state changes
    private val cameraStateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            Log.d("LiveEventActivity", "Camera opened")
            cameraDevice = camera
            startPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            Log.d("LiveEventActivity", "Camera disconnected")
            camera.close()
            cameraDevice = null
        }

        override fun onError(camera: CameraDevice, error: Int) {
            Log.e("LiveEventActivity", "Camera error: $error")
            camera.close()
            cameraDevice = null
        }
    }

    private fun startPreview() {
        val surfaceView = findViewById<SurfaceView>(binding.surfaceView.id)
        val surfaceHolder = surfaceView.holder

        surfaceHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                // The Surface is ready, set up the preview session
                setupCameraSession(holder)
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                // Ideally, you'd adjust the camera settings here for the new surface size.
                // For simplicity, just re-setup the session which will also handle orientation changes.
                setupCameraSession(holder)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraCaptureSession?.close()
                cameraCaptureSession = null
            }
        })
    }

    private fun setupCameraSession(holder: SurfaceHolder) {
        cameraDevice?.let { device ->
            // Close the previous session before creating a new one.
            cameraCaptureSession?.close()
            cameraCaptureSession = null

            val previewSurface = holder.surface
            device.createCaptureSession(listOf(previewSurface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    cameraCaptureSession = session
                    val previewRequestBuilder = device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
                        addTarget(previewSurface)
                    }
                    try {
                        session.setRepeatingRequest(previewRequestBuilder.build(), null, null)
                    } catch (e: CameraAccessException) {
                        Log.e("LiveEventActivity", "Failed to start camera preview because it couldn't access camera", e)
                    } catch (e: IllegalStateException) {
                        Log.e("LiveEventActivity", "Failed to start camera preview.", e)
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    Log.e("LiveEventActivity", "Failed to configure camera.")
                }
            }, null)
        }
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 1
        private val PERMISSIONS_REQUIRED = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }
}

