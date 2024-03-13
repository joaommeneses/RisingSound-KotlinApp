package com.risingsound.kotlinapp.listener

import android.R
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private val dataList = arrayListOf("João Meneses", "João Silva", "Miguel Isidoro", "Artists X")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.risingsound.kotlinapp.R.layout.fragment_search_listener, container, false)
        searchView = view.findViewById(com.risingsound.kotlinapp.R.id.search_bar)
        listView = view.findViewById(com.risingsound.kotlinapp.R.id.lv_search_results)
        listView.visibility = View.GONE // Initially set the ListView to GONE

        val cancelTextView: TextView = view.findViewById(com.risingsound.kotlinapp.R.id.tv_cancel)
        cancelTextView.setOnClickListener {
            searchView.setQuery("", false)
            searchView.clearFocus()
        }
        setupSearchView()
        return view
    }

    private fun setupSearchView() {
        adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_list_item_1,
            dataList
        )
        listView.adapter = adapter

        val searchEditText: EditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(Color.BLACK) // Set the text color to black
        searchEditText.setHintTextColor(Color.GRAY) // Set the hint text color to gray

        // To change the color of the search icon
        val searchIcon: ImageView = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.BLACK) // Set the search icon color to black

        // To change the color of the close icon
        val closeIcon: ImageView = searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        closeIcon.setColorFilter(Color.BLACK) // Set the close icon color to black

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // No action on submit for this basic example
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        // Set the onFocusChangeListener to the SearchView
        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            listView.visibility = if (hasFocus) View.VISIBLE else View.GONE
        }
    }
}
