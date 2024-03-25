package com.risingsound.kotlinapp.listener

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.risingsound.kotlinapp.R


class DonationFragment : DialogFragment() {

    var db = FirebaseFirestore.getInstance()
    val donationsRef = db.collection("donations")

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
            val donationAmountString = view.findViewById<TextInputEditText>(R.id.et_value).text.toString()

            // Check if the donation amount is not empty and is a valid number
            val donationAmount = donationAmountString.toFloatOrNull()
            if (!donationAmountString.isBlank() && donationAmount != null && donationAmount >= 1f) {
                val donation = hashMapOf(
                    "musicianId" to 1,  // Replace with actual musician's ID
                    "listenerId" to "miguelisidoro__",  // Replace with actual listener's ID
                    "amount" to donationAmount,  // Use the actual donated amount
                    "timestamp" to FieldValue.serverTimestamp()  // Timestamp of the donation
                )
                // Add the donation to Firestore
                donationsRef.add(donation)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot added with ID: ${donationsRef.id}")
                        // Inform the user of a successful donation
                        showCustomToast("Thank you for your donation!")

                        dismiss()  // Close the donation dialog
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                        // Inform the user that the donation failed
                        showCustomToast("Donation failed, try again!")
                        dismiss()
                    }
            }
            else {
                    // Inform the user that the entered amount is not valid
                showCustomToast("Please enter a valid amount (minimum 1€).")
                }
            }
    }

    private fun showCustomToast(message: String) {
        val inflater = layoutInflater
        val layout =
            inflater.inflate(R.layout.toast_donation, view!!.findViewById(R.id.toast_donation_custom))

        val text = layout.findViewById<TextView>(R.id.toast_text)
        text.text = message

        with(Toast(context)) {
            duration = Toast.LENGTH_LONG
            view = layout
            show()
        }
    }
    }
