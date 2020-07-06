package com.example.gstarena.contestsScreen

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.gstarena.database.getDatabase
import com.example.gstarena.repository.ContestsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ContestsViewModel(application: Application,activity : FragmentActivity) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val contestsRepository = ContestsRepository(database,activity)


    init {
        refreshContests(null)
    }

    val contestsList = contestsRepository.contests

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun refreshContests(swipeRefreshLayout: SwipeRefreshLayout?) {
        viewModelScope.launch {
            contestsRepository.refreshContests()
        }.invokeOnCompletion {
            swipeRefreshLayout?.isRefreshing = false
        }
    }


    class Factory(val app: Application, private val activity: FragmentActivity) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ContestsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ContestsViewModel(app,activity) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}