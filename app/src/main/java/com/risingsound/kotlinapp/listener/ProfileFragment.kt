package com.risingsound.kotlinapp.listener

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.risingsound.kotlinapp.MainActivity
import com.risingsound.kotlinapp.R
import com.risingsound.kotlinapp.musician.EditProfileActivity
import com.risingsound.kotlinapp.musician.LiveEventActivity
import com.risingsound.kotlinapp.musician.PremiumActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_listener, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clAccount: ViewGroup = view.findViewById(R.id.cl_account_listener)
        clAccount.setOnClickListener {
            val intent = Intent(activity, AccountActivity::class.java)
            startActivity(intent)

            //requireActivity().finish()
        }

        val clFavArtist: ViewGroup = view.findViewById(R.id.cl_fav_artists_listener)
        clFavArtist.setOnClickListener {
            val intent = Intent(activity, FavArtistsActivity::class.java)
            startActivity(intent)

            //requireActivity().finish()
        }

        val clLiveEvents: ViewGroup = view.findViewById(R.id.cl_live_events)
        clLiveEvents.setOnClickListener {
            val intent = Intent(activity, LPListenerFragment::class.java)
            startActivity(intent)

            //requireActivity().finish()
        }

        val btnEditProfile: Button = view.findViewById(R.id.btn_edit_profile)
        btnEditProfile.setOnClickListener {
            val intent = Intent(activity, EditProfileListenerActivity::class.java)
            startActivity(intent)

            //requireActivity().finish()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}