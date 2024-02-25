package com.twinkle.myapplication.listener

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.twinkle.myapplication.R

class DonationFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.TransparentDialog)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_donation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the CardView from the layout
        val donationCardView: CardView = view.findViewById(R.id.donationCardView)

        // Set the background color to white
        donationCardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        view.findViewById<Button>(R.id.btn_donate_now).setOnClickListener {
            // Here you can add the logic for donation
            // For now, we'll just dismiss the fragment
            dismiss()
        }
    }
}
