package com.example.gstarena.problemsScreen

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gstarena.R
import com.example.gstarena.databinding.ProblemViewBinding
import com.example.gstarena.databinding.StatisticsViewBinding
import com.example.gstarena.domain.Problem
import com.example.gstarena.statisticsScreen.StatisticsAdapter

class ProblemsAdapter : ListAdapter<Problem,ProblemsAdapter.ViewHolder>(ProblemsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemsAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ProblemsAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(val binding: ProblemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        private val prob  = binding.problem
        private val problemInfo = binding.probleminfo
        private val img = binding.imageView3
        private val baseUrl = "http://arena.siesgst.ac.in/contest/"

        fun bind(problem: Problem) {
            prob.text = problem.name
            var str = "points : " + problem.points + "\nContest Code : " + problem.contestCode
            problemInfo.text = str
            img.setImageResource(R.drawable.ic_question)
            binding.problemLayout.setOnClickListener {view->
                val completeUrl = baseUrl + problem.contestCode + "/problem/" + problem.code
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(completeUrl))
                view.context.startActivity(browserIntent)
            }
        }

        companion object {
            fun from(parent: ViewGroup) : ProblemsAdapter.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProblemViewBinding.inflate(layoutInflater, parent, false)
                return ProblemsAdapter.ViewHolder(binding)
            }
        }
    }
}


class ProblemsDiffCallback : DiffUtil.ItemCallback<Problem>() {

    override fun areItemsTheSame(oldItem: Problem, newItem: Problem): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: Problem, newItem: Problem): Boolean {
        return oldItem == newItem
    }
}