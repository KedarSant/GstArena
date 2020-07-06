package com.example.gstarena.statsScreen

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.gstarena.R
import com.example.gstarena.databinding.StatsFragmentBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class statsFragment : Fragment() {

    companion object {
        fun newInstance() = statsFragment()
    }

    private lateinit var viewModel: StatsViewModel
    private lateinit var binding : StatsFragmentBinding
    private lateinit var pieChart: PieChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.stats_fragment, container, false)
        pieChart = binding.pieChart

        // Initialize and set the pie Data set, its values and colors
        val pieDataSet = PieDataSet(dataValues(),"")
        pieDataSet.valueTextSize = 24F
        pieDataSet.setColors(*ColorTemplate.PASTEL_COLORS)
        pieDataSet.sliceSpace = 3F
        pieDataSet.selectionShift = 5F

        // Create a pie data object out of the Pie data set
        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(24F)
        pieChart.data = pieData

        // Set other attributes of the Pie chart
        pieChart.isDrawHoleEnabled = false
        pieChart.setDrawEntryLabels(false)
        pieChart.description.text = ""
        pieChart.animateY(1000,Easing.EaseInOutCubic)

        // Set the legend text Size
        var legend = pieChart.legend
        legend.textSize = 14F
        legend.isWordWrapEnabled = true

        pieChart.invalidate()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StatsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun dataValues() : List<PieEntry> {
        val pref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        return listOf(PieEntry(pref.getInt("accepted",0).toFloat(),"accepted"),
        PieEntry(pref.getInt("wrongAnswer",0).toFloat(),"Wrong"),
        PieEntry(pref.getInt("compilationError",0).toFloat(),"Compile Error"),
        PieEntry(pref.getInt("runtimeError",0).toFloat(),"Runtime Error"),
        PieEntry(pref.getInt("timeLimitExceeded",0).toFloat(),"Time limit Exceeded")
        )
    }

}