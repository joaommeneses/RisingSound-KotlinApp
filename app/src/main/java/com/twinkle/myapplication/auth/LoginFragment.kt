package com.twinkle.myapplication.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.twinkle.myapplication.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //click listener for the "Continue with Email" button
        binding.emailButton.setOnClickListener {

            // start the UserTypeActivity
            val intent = Intent(activity, UserTypeActivity::class.java)
            startActivity(intent)

            // finish the current activity
            requireActivity().finish()
        }

        setupTermsAndPrivacyText()
    }

    //underlined "Terms & Conditions" and "Privacy Policy"
    private fun setupTermsAndPrivacyText() {
        val text = "If you are creating a new account,\nTerms & Conditions and Privacy Policy will apply."
        val spannable = SpannableString(text)

        // define clickable spans for "Terms & Conditions" and "Privacy Policy"
        val termsClickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // handle click action for "Terms & Conditions"
                // TODO--------
            }
        }
        val privacyClickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // handle click action for "Privacy Policy"
                // TODO-------
            }
        }

        // find the indices of "Terms & Conditions" and "Privacy Policy" in the text
        val termsIndex = text.indexOf("Terms & Conditions")
        val privacyIndex = text.indexOf("Privacy Policy")

        // apply underlines to "Terms & Conditions" and "Privacy Policy"
        spannable.setSpan(UnderlineSpan(), termsIndex, termsIndex + 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(UnderlineSpan(), privacyIndex, privacyIndex + 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // apply clickable spans to "Terms & Conditions" and "Privacy Policy"
        spannable.setSpan(termsClickSpan, termsIndex, termsIndex + 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(privacyClickSpan, privacyIndex, privacyIndex + 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // set text color for the whole text
        spannable.setSpan(ForegroundColorSpan(Color.parseColor("#797979")), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // set the modified text to the TextView
        binding.tvTermsCond.text = spannable

        // make TextView clickable
        binding.tvTermsCond.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
