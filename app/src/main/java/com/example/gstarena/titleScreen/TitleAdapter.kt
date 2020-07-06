package com.example.gstarena.titleScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.gstarena.R
import com.example.gstarena.databinding.ListViewTitleBinding

class TitleAdapter : RecyclerView.Adapter<TitleAdapter.TitleViewHolder>() {

    private var categories = listOf<String>(
        "Submission Statistics",
        "Rating Dynamics", "Contests", "Problem Set", "Battle", "Top performers"
    )
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var categoryInfo = listOf<String>(
        "Check out how you have fared all along",
        "Check out how your ratings have changed with every contest",
        "Check out exciting new contests to take part in",
        "Check out fun problem statements and exercise your brain",
        "Compare stats with your friends to find who has got the edge",
        "Check out our top performers in various programming languages"
    )

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {

        val item = categories[position]
        val itemInfo = categoryInfo[position]
        holder.bind(item, itemInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        return TitleViewHolder.from(
            parent
        )
    }

    class TitleViewHolder private constructor(val binding: ListViewTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val description: TextView = binding.description
        private val info: TextView = binding.info
        private val img: ImageView = binding.img

        fun bind(item: String, itemInfo: String) {
            description.text = item
            info.text = itemInfo
            binding.layout.setOnClickListener {
                when(item) {
                    "Submission Statistics" -> it.findNavController().navigate(R.id.action_titleFragment_to_statsFragment)
                    "Top performers" -> it.findNavController().navigate(R.id.action_titleFragment_to_statisticsFragment)
                    "Contests" -> it.findNavController().navigate(R.id.action_titleFragment_to_contestsFragment2)
                    "Problem Set" -> it.findNavController().navigate(R.id.action_titleFragment_to_problemsFragment)
                    "Rating Dynamics" -> it.findNavController().navigate(R.id.action_titleFragment_to_ratingsFragment)
                }
            }
            img.setImageResource(
                when (item) {
                    "Submission Statistics" -> R.drawable.ic_graphic
                    "Rating Dynamics" -> R.drawable.ic_star
                    "Contests" -> R.drawable.ic_trophy
                    "Problem Set" -> R.drawable.ic_question
                    "Battle" -> R.drawable.ic_war
                    "Top performers" -> R.drawable.ic_success
                    else -> R.drawable.fui_ic_check_circle_black_128dp
                }
            )
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TitleViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListViewTitleBinding.inflate(layoutInflater, parent, false)
                return TitleViewHolder(binding)
            }
        }
    }
}
