package com.risingsound.kotlinapp.musician

import android.R
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import com.github.mikephil.charting.data.Entry
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.risingsound.kotlinapp.databinding.ActivityStatisticsBinding

class StatisticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStatisticsBinding
    private var chart: LineChart? = null
    private var spinnerGraphSelection: Spinner? = null
    val array = arrayOf("Listener Growth", "Connections Growth", "Donation Income")

    private val listenerGrowthData = listOf(
        Entry(1f, 39f),
        Entry(2f, 66f),
        Entry(3f, 89f),
        Entry(4f, 211f),
        Entry(5f, 290f)
    )

    private val connectionsGrowthData = listOf(
        Entry(1f, 10f),
        Entry(2f, 22f),
        Entry(3f, 28f),
        Entry(4f, 45f),
        Entry(5f, 62f)
    )

    private val donationIncomeData = listOf(
        Entry(1f, 58f),
        Entry(2f, 99f),
        Entry(3f, 32f),
        Entry(4f, 66f),
        Entry(5f, 122f)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chart = binding.chart
        spinnerGraphSelection = binding.spinnerGraphSelection
        setupSpinner()
        setupChart(listenerGrowthData, "Listener Growth") // Initial chart setup

        val backButton = findViewById<ImageView>(com.risingsound.kotlinapp.R.id.iv_ic_back)

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupSpinner() {
        // Instead of using createFromResource, directly create an ArrayAdapter with the array
        val adapter = ArrayAdapter(
            this, // Use 'this' for context
            R.layout.simple_spinner_item, // Correctly refer to Android's layout resource
            array // Directly use the array you defined
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item) // Use Android's layout resource
        spinnerGraphSelection?.adapter = adapter
        spinnerGraphSelection?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Based on the position, change the chart and title accordingly
                when (position) {
                    0 -> {
                        setupChart(listenerGrowthData, "Listener Growth")
                        binding.tvGraph.text = "Listener Growth" // Change the graph title
                        binding.tvGraph.setTextColor(Color.WHITE)
                    }
                    1 -> {
                        setupChart(connectionsGrowthData, "Connections Growth")
                        binding.tvGraph.text = "Connections Growth" // Change the graph title
                        binding.tvGraph.setTextColor(Color.WHITE)
                    }
                    2 -> {
                        setupChart(donationIncomeData, "Donation Income")
                        binding.tvGraph.text = "Donation Income" // Change the graph title
                        binding.tvGraph.setTextColor(Color.WHITE)
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupChart(dataPoints: List<Entry>, chartTitle: String) {
        val dataSet = LineDataSet(dataPoints, chartTitle).apply {
            lineWidth = 5f // Make the line thicker
            color = Color.parseColor("#7B2CBF") // Set the line color
            setDrawValues(false) // Optionally, if you don't want to show values on each point
        }

        val lineData = LineData(dataSet)
        chart?.data = lineData

        // Customize chart appearance
        chart?.apply {
            description.isEnabled = false // Optionally hide the description
            setDrawGridBackground(false) // Do not draw the grid background
            setDrawBorders(false) // Optionally, if you don't want to draw borders

            // Customize X-axis
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM // Set the X-axis to the bottom
                setDrawGridLines(false) // Do not draw grid lines for X-axis
                granularity = 1f // Show integer values
                textColor = Color.BLACK // Set the text color to black directly
                axisLineColor = Color.BLACK // Set the axis line color to black directly
            }

            // Customize Y-axis (Left)
            axisLeft.apply {
                setDrawGridLines(false) // Do not draw grid lines for Y-axis
                textColor = Color.BLACK // Set the text color to black directly
                axisLineColor = Color.BLACK // Set the axis line color to black directly
            }

            // Hide Y-axis (Right)
            axisRight.isEnabled = false

            legend.isEnabled = false // Optionally, hide the legend

            invalidate() // Refresh the chart
        }
    }


}