package com.example.gstarena.contestsScreen

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gstarena.R
import com.example.gstarena.databinding.ContestViewBinding
import com.example.gstarena.domain.Contest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random


private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class ContestsAdapter : ListAdapter<DataItem,RecyclerView.ViewHolder>(ContestDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    fun addHeaderAndSubmitList(list: List<Contest>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map{
                    DataItem.ContestItem(it)
                }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val contestItem = getItem(position) as DataItem.ContestItem
                holder.bind(contestItem.contest)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.ContestItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class ViewHolder private constructor(var binding : ContestViewBinding) : RecyclerView.ViewHolder(binding.root) {

        private val contestName = binding.contestName
        private val description = binding.description
        private val img = binding.imageView
        private val baseUrl = "http://arena.siesgst.ac.in/contest/"

        companion object {
            fun from(parent: ViewGroup): ContestsAdapter.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ContestViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(contest : Contest) {
            contestName.text = contest.name
            var str = "Description : " + contest.description + "\n" + "Contest Code :" + contest.code
            description.text = str
            img.setImageResource(when(Random(System.currentTimeMillis()).nextInt(5)){
                0 -> R.drawable.ic_code
                1 -> R.drawable.ic_competitor
                2 -> R.drawable.ic_computer
                3 -> R.drawable.ic_development
                4 -> R.drawable.ic_question
                else ->R.drawable.ic_code
            })
            binding.contestLayout.setOnClickListener {view->
                val completeUrl = baseUrl +  contest.code
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(completeUrl))
                view.context.startActivity(browserIntent)
            }
        }
    }

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.contest_layout_header, parent, false)
                return TextViewHolder(view)
            }
        }
    }
}

class ContestDiffCallback : DiffUtil.ItemCallback<DataItem>() {

    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem {
    data class ContestItem(val contest: Contest): DataItem() {
        override val id = contest._id
    }

    object Header: DataItem() {
        override val id = "header"
    }

    abstract val id: String
}


