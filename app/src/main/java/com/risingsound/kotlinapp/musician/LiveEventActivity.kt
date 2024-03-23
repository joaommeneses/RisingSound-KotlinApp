package com.risingsound.kotlinapp.musician

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import android.Manifest
import android.content.Context
import android.graphics.PorterDuff
import android.hardware.camera2.*
import android.view.Surface
import android.view.SurfaceHolder
import android.view.TextureView
import com.risingsound.kotlinapp.R
import com.risingsound.kotlinapp.databinding.ActivityLiveEventBinding
import java.util.*

class LiveEventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLiveEventBinding
    private var donationListenerRegistration: ListenerRegistration? = null
    private lateinit var cameraManager: CameraManager
    private var cameraDevice: CameraDevice? = null
    private var captureSession: CameraCaptureSession? = null

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiveEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissionsIfNeeded()
        setupListeners()

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_start_broadcast)?.mutate()
        drawable?.setColorFilter(ContextCompat.getColor(applicationContext, android.R.color.white), PorterDuff.Mode.SRC_IN)
        binding.btnStartBroadcast.setImageDrawable(drawable)
    }

    private fun requestPermissionsIfNeeded() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        } else {
            // Permissions already granted, you can start camera preview here if needed immediately
            // or wait for user action such as button click
        }
    }

    private fun setupListeners() {
        binding.ivIcBack.setOnClickListener { finish() }
        binding.btnStartBroadcast.setOnClickListener {
            if (allPermissionsGranted()) {
                toggleLiveBroadcast()
            } else {
                Toast.makeText(this, "Camera and microphone permissions are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleLiveBroadcast() {
        if (binding.tvLiveBanner.visibility == View.VISIBLE) {
            stopCameraPreview()
        } else {
            startCameraPreview()
        }
    }

    private fun startCameraPreview() {
        try {
            startListeningForDonations()
            val cameraId = cameraManager.cameraIdList[0] // Choose the right camera ID as per your logic
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    createPreviewSession()
                }

                override fun onDisconnected(camera: CameraDevice) {
                    camera.close()
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    camera.close()
                    cameraDevice = null
                }
            }, null)
        } catch (e: Exception) {
            Toast.makeText(this, "Camera initialization failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createPreviewSession() {
        val surfaceHolder: SurfaceHolder = binding.surfaceView.holder
        val surface: Surface = surfaceHolder.surface

        cameraDevice?.let { device ->
            val previewRequestBuilder = device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
                addTarget(surface)
            }
            device.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    if (cameraDevice == null) return
                    captureSession = session
                    try {
                        session.setRepeatingRequest(previewRequestBuilder.build(), null, null)
                        runOnUiThread {
                            binding.tvLiveBanner.visibility = View.VISIBLE
                            val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.baseline_pause_24)?.mutate()
                            drawable?.setColorFilter(ContextCompat.getColor(applicationContext, android.R.color.white), PorterDuff.Mode.SRC_IN)
                            binding.btnStartBroadcast.setImageDrawable(drawable)
                        }
                    } catch (e: CameraAccessException) {
                        Toast.makeText(this@LiveEventActivity, "Failed to start camera preview: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    Toast.makeText(this@LiveEventActivity, "Failed to configure camera.", Toast.LENGTH_SHORT).show()
                }
            }, null)
        }
    }

    private fun stopCameraPreview() {
        stopListeningForDonations()

        captureSession?.close()
        captureSession = null
        cameraDevice?.close()
        cameraDevice = null
        binding.tvLiveBanner.visibility = View.GONE
        val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_start_broadcast)?.mutate()
        drawable?.setColorFilter(ContextCompat.getColor(applicationContext, android.R.color.white), PorterDuff.Mode.SRC_IN)
        binding.btnStartBroadcast.setImageDrawable(drawable)

    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                toggleLiveBroadcast()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

private fun startListeningForDonations() {
        val musicianId = 1  // Assuming the musician's ID is 1 for demo purposes
        val tenSecondsAgo = Timestamp.now().toDate().time - 10000  // Current time minus 10 seconds in milliseconds
        val tenSecondsAgoTimestamp = Timestamp(Date(tenSecondsAgo))

        val donationsRef = FirebaseFirestore.getInstance().collection("donations")
        donationListenerRegistration = donationsRef
            .whereEqualTo("musicianId", musicianId)
            .whereGreaterThan("timestamp", tenSecondsAgoTimestamp)  // Query for donations in the last 10 seconds
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, "Error listening to donation updates.", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                for (dc in snapshots?.documentChanges ?: emptyList()) {
                    if (dc.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                        val listenerId = dc.document.getDouble("listenerId") ?: 0
                        val amount = dc.document.getDouble("amount") ?: 0.0
                        // Ensure to convert to a more readable identifier or name for the listenerId if necessary
                        Toast.makeText(this, "New donation from $listenerId: $amount€", Toast.LENGTH_LONG).show()
                    }
                }
            }
    }
    private fun stopListeningForDonations() {
        donationListenerRegistration?.remove()
    }
}

