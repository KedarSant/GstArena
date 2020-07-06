package com.example.gstarena.statisticsScreen

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gstarena.contestsScreen.ContestsViewModel
import com.example.gstarena.database.getDatabase
import com.example.gstarena.domain.Statistics
import com.example.gstarena.repository.ContestsRepository
import com.example.gstarena.repository.StatisticsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class StatisticsViewModel(application: Application,activity: FragmentActivity) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val statisticsRepository = StatisticsRepository(database,activity)


    init {
        viewModelScope.launch {
            statisticsRepository.refreshStatistics()
        }
    }

    val statisticsList = statisticsRepository.statistics

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application, private val activity: FragmentActivity) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return StatisticsViewModel(app,activity) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}