package com.example.gstarena.problemsScreen

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gstarena.database.getDatabase
import com.example.gstarena.domain.Problem
import com.example.gstarena.repository.ProblemsRepository
import com.example.gstarena.repository.StatisticsRepository
import com.example.gstarena.statisticsScreen.StatisticsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ProblemsViewModel(application: Application,activity: FragmentActivity) : ViewModel() {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val problemsRepository = ProblemsRepository(database,activity)


    init {
        viewModelScope.launch {
            problemsRepository.refreshProblems()
        }
    }

    val problemsList = problemsRepository.problems

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application, private val activity: FragmentActivity) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProblemsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProblemsViewModel(app,activity) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}