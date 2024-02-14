package com.twinkle.myapplication
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.twinkle.myapplication.databinding.ActivityDoneBinding
import com.twinkle.myapplication.databinding.ActivityUserTypeBinding

class UserTypeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //start new activity
        binding.buttonListener.setOnClickListener {
            //listener
            startDoneActivity("#7B2CBF")
        }

        binding.buttonMusician.setOnClickListener {
            //musician
            startDoneActivity("#3A1F62")
        }
    }

    private fun startDoneActivity(color: String) {
        val intent = Intent(this, ActivityDone::class.java)
        intent.putExtra("BUTTON_COLOR", color)
        startActivity(intent)
    }
}
