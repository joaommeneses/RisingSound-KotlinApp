package com.risingsound.kotlinapp.musician

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
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
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.util.Size
import android.view.Surface
import android.view.SurfaceHolder
import android.widget.TextView
import com.risingsound.kotlinapp.R
import com.risingsound.kotlinapp.databinding.ActivityLiveEventBinding
import java.lang.Integer.signum
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
            stopListeningForDonations()
            stopCameraPreview()
        } else {
            startCameraPreview()
            startListeningForDonations()
        }
    }

    private fun startCameraPreview() {
        try {
            val cameraId = cameraManager.cameraIdList[0]
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return
            }

            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                ?: throw RuntimeException("Cannot get available preview/video sizes")

            val outputSizes = map.getOutputSizes(SurfaceTexture::class.java)
            if (outputSizes.isNullOrEmpty()) {
                throw RuntimeException("Cannot get available preview/video sizes")
            }

            // Select the largest available preview size
            val previewSize = Collections.max(outputSizes.asList(), CompareSizesByArea())
            val surface = binding.surfaceView.holder.setFixedSize(previewSize.width, previewSize.height)


            cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    createPreviewSession(surface)
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
            Toast.makeText(this, "Camera initialization failed: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }

    private class CompareSizesByArea : Comparator<Size> {
        override fun compare(lhs: Size, rhs: Size) = signum((lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height).toInt())
    }

    private fun chooseOptimalSize(choices: Array<Size>?, aspectRatio: Double, screenSize: Size): Size {
        val bigEnough = choices?.filter {
            val previewAspectRatio = it.height.toDouble() / it.width.toDouble()
            val isDesiredAspectRatio = Math.abs(previewAspectRatio - aspectRatio) < 0.1
            val isDesiredResolution = it.height <= screenSize.height && it.width <= screenSize.width
            isDesiredAspectRatio && isDesiredResolution
        }
        return bigEnough?.first() ?: choices?.first() ?: Size(1920, 1080)
    }

    private fun createPreviewSession(surfaceTexture: Unit) {
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
        val musicianId = 1
        val tenSecondsAgo = Timestamp.now().toDate().time - 10000
        val tenSecondsAgoTimestamp = Timestamp(Date(tenSecondsAgo))

        val donationsRef = FirebaseFirestore.getInstance().collection("donations")
        donationListenerRegistration = donationsRef
            .whereEqualTo("musicianId", musicianId)
            .whereGreaterThan("timestamp", tenSecondsAgoTimestamp)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Toast.makeText(this, "Error listening to donation updates.", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                for (dc in snapshots?.documentChanges ?: emptyList()) {
                    if (dc.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                        val listenerId = dc.document.getString("listenerId") ?: "Unknown"
                        val amount = dc.document.getDouble("amount") ?: 0.0

                        val inflater = layoutInflater
                        val layout = inflater.inflate(R.layout.toast_donation, findViewById(R.id.toast_donation_custom))

                        val text = layout.findViewById(R.id.toast_text) as TextView
                        text.text = "New donation from $listenerId: $amount€"

                        with (Toast(applicationContext)) {
                            duration = Toast.LENGTH_LONG
                            view = layout
                            show()
                        }
                    }
                }
            }
    }
    private fun stopListeningForDonations() {
        donationListenerRegistration?.remove()
    }
}

