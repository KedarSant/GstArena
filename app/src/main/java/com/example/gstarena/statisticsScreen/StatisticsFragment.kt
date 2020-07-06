package com.example.gstarena.statisticsScreen

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gstarena.R
import com.example.gstarena.contestsScreen.ContestsViewModel
import com.example.gstarena.databinding.StatisticsFragmentBinding
import kotlinx.coroutines.awaitAll

class StatisticsFragment : Fragment() {

    companion object {
        fun newInstance() = StatisticsFragment()
    }

    private lateinit var binding: StatisticsFragmentBinding
    private lateinit var adapter: StatisticsAdapter

    private val viewModel: StatisticsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, StatisticsViewModel.Factory(activity.application,activity))
            .get(StatisticsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.statistics_fragment,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = StatisticsAdapter()
        binding.recycler.adapter = adapter
        binding.recycler.addItemDecoration(
            DividerItemDecoration(context,
                LinearLayoutManager.VERTICAL)
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.statisticsList.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.addHeaderAndSubmitList(it)
            }
        })
    }

}