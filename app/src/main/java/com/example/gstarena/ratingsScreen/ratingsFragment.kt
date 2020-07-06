package com.example.gstarena.ratingsScreen

import android.graphics.Color
import android.graphics.Paint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.gstarena.R
import com.example.gstarena.databinding.RatingsFragmentBinding
import com.example.gstarena.domain.Rating
import com.example.gstarena.titleScreen.TitleViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class ratingsFragment : Fragment() {

    companion object {
        fun newInstance() = ratingsFragment()
    }

    private lateinit var binding : RatingsFragmentBinding
    private lateinit var barChart: BarChart
    private var barEntryList = arrayListOf<BarEntry>()
    private var labels = arrayListOf<String>()

    private val viewModel: RatingsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, RatingsViewModel.Factory(activity))
            .get(RatingsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.ratings_fragment,container,false)
        barChart = binding.barChart
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.data.observe(viewLifecycleOwner, Observer {
            it?.let {
                fillLists(it)
                drawGraph()
            }
        })
    }

    private fun fillLists(list : List<Rating>) {
        if(list.isEmpty()) return

        if(barEntryList.isNotEmpty()) barEntryList.clear()
        if(labels.isNotEmpty()) labels.clear()

       for (i in list.indices) {
           barEntryList.add(BarEntry(i.toFloat(),list[i].ratings.toFloat()))
           labels.add(list[i].contestCode)
       }
    }

    private fun drawGraph() {
        if(barEntryList.isEmpty()) {
            barChart.setNoDataText("No Data. Increase your Ratings!!");
            val paint:Paint =  barChart.getPaint(Chart.PAINT_INFO)
            paint.textSize = 40f
            paint.color = Color.BLACK
            barChart.setPaint(paint,0)
            barChart.invalidate()
            return
        }
        val barDataSet = BarDataSet(barEntryList,"")
        barDataSet.setColors(*ColorTemplate.PASTEL_COLORS)
        val description = Description()
        description.text = ""
        barChart.description = description
        val barData = BarData(barDataSet)
        barChart.data = barData
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1F
        xAxis.labelCount = labels.size
        xAxis.labelRotationAngle = 270F
        barChart.animateY(2000)
        barChart.invalidate()

    }



}