package com.example.gstarena.problemsScreen

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
import com.example.gstarena.databinding.ProblemsFragmentBinding
import com.example.gstarena.statisticsScreen.StatisticsAdapter
import com.example.gstarena.statisticsScreen.StatisticsViewModel

class ProblemsFragment : Fragment() {

    companion object {
        fun newInstance() = ProblemsFragment()
    }

    private lateinit var binding : ProblemsFragmentBinding
    private lateinit var adapter: ProblemsAdapter

    private val viewModel: ProblemsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, ProblemsViewModel.Factory(activity.application,activity))
            .get(ProblemsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.problems_fragment,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = ProblemsAdapter()
        binding.recycler1.adapter = adapter
        binding.recycler1.addItemDecoration(
            DividerItemDecoration(context,
                LinearLayoutManager.VERTICAL)
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.problemsList.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })
    }

}