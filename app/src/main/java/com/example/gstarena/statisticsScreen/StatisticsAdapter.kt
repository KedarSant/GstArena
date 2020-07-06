package com.example.gstarena.statisticsScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.gstarena.R
import com.example.gstarena.databinding.StatisticsViewBinding
import com.example.gstarena.domain.Statistics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class StatisticsAdapter : androidx.recyclerview.widget.ListAdapter<DataObj,RecyclerView.ViewHolder>(
    StatisticsDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataObj.Header ->   ITEM_VIEW_TYPE_HEADER
            is DataObj.StatisticsItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(list: List<Statistics>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataObj.Header)
                else -> listOf(DataObj.Header) + list.map{
                    DataObj.StatisticsItem(it)
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
                val statisticItem = getItem(position) as DataObj.StatisticsItem
                holder.bind(statisticItem.statistic)
            }
        }
    }

    class ViewHolder private constructor(binding : StatisticsViewBinding) : RecyclerView.ViewHolder(binding.root) {

        private val img = binding.languageView
        private val name = binding.userName
        private val userDescription = binding.userDescription

        fun bind(statistic : Statistics) {
            var str = "Language : " + statistic.language + "\n" +
                    "Accepted count : " + statistic.acceptedCount
            name.text = statistic.name
            userDescription.text = str
            img.setImageResource(when(statistic.language){
                "Python" -> R.drawable.ic_python
                "Python3" -> R.drawable.ic_python
                "Java" -> R.drawable.ic_java
                "Javascript" -> R.drawable.ic_javascript
                "C" -> R.drawable.ic_c99
                "C++" -> R.drawable.ic_c
                "C++14" -> R.drawable.ic_c
                "Go" -> R.drawable.ic_golang_icon
                else -> R.drawable.ic_code
            })
        }
        companion object {
            fun from(parent: ViewGroup) : StatisticsAdapter.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StatisticsViewBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.statistics_layout_header, parent, false)
                return TextViewHolder(view)
            }
        }
    }
}

class StatisticsDiffCallback : DiffUtil.ItemCallback<DataObj>() {

    override fun areItemsTheSame(oldItem: DataObj, newItem: DataObj): Boolean {
        return oldItem.language == newItem.language
    }

    override fun areContentsTheSame(oldItem: DataObj, newItem: DataObj): Boolean {
        return oldItem == newItem
    }
}

sealed class DataObj {
    data class StatisticsItem(val statistic: Statistics): DataObj() {
        override val language = statistic.language
    }

    object Header: DataObj() {
        override val language = "header"
    }

    abstract val language: String
}

