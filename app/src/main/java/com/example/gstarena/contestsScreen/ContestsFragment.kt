package com.example.gstarena.contestsScreen

import android.app.Application
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.gstarena.R
import com.example.gstarena.databinding.ContestsFragmentBinding
import com.example.gstarena.titleScreen.TitleViewModel
import kotlinx.coroutines.delay
import java.lang.Thread.sleep

class ContestsFragment : Fragment() {

    companion object {
        fun newInstance() = ContestsFragment()
    }

    private val viewModel: ContestsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, ContestsViewModel.Factory(activity.application,activity))
            .get(ContestsViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.contestsList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it.reversed())
            }
        })
        swipeLayout.setOnRefreshListener {
            viewModel.refreshContests(swipeLayout)
        }
    }

    private lateinit var binding : ContestsFragmentBinding
    private lateinit var adapter : ContestsAdapter
    private lateinit var swipeLayout : SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<ContestsFragmentBinding>(inflater,R.layout.contests_fragment,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = ContestsAdapter()
        binding.recyclerView1.adapter = adapter
        binding.recyclerView1.addItemDecoration(
            DividerItemDecoration(context,
                LinearLayoutManager.VERTICAL)
        )
        swipeLayout = binding.swipeLayout
        return binding.root
    }
}