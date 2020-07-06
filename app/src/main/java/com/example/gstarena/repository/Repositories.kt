package com.example.gstarena.repository

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.gstarena.database.ContestsDatabase
import com.example.gstarena.database.asDomainContests
import com.example.gstarena.database.asDomainProblem
import com.example.gstarena.database.asDomainStatistics
import com.example.gstarena.domain.Contest
import com.example.gstarena.domain.Problem
import com.example.gstarena.domain.Statistics
import com.example.gstarena.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class ContestsRepository(private val database: ContestsDatabase, private val activity : FragmentActivity) {

    private lateinit var contestsWithAdmin : List<NetworkContest>

    val contests: LiveData<List<Contest>> =
        Transformations.map(database.contestsDao.getContests()) {
            it?.asDomainContests()
        }

    suspend fun refreshContests() {
        try {
            withContext(Dispatchers.IO) {
                contestsWithAdmin = Network.info.getContestsAsync().await()
                for (networkContest in contestsWithAdmin) {
                    database.contestsDao.insertAdmins(
                        *networkContest.contestAdmin.asDatabaseAdmin(
                            networkContest._id
                        )
                    )
                }
                database.contestsDao.insertContests(*contestsWithAdmin.asDatabaseContest())
            }
        } catch (e : Exception) {
            var builder = AlertDialog.Builder(activity)
            builder.setMessage(e.message)
            builder.setCancelable(true)
            builder.setPositiveButton("Ok", DialogInterface.OnClickListener {
                    dialog: DialogInterface?, _: Int ->
                dialog?.cancel()
            })
            var alert = builder.create()
            alert.show()
        }
    }
}

class StatisticsRepository(private val database: ContestsDatabase, private val activity : FragmentActivity) {

    private lateinit var networkStatistics : List<NetworkStatistics>

    val statistics : LiveData<List<Statistics>> = Transformations.map(database.statisticsDao.getStatistics()) {
        it?.asDomainStatistics()
    }

    suspend fun refreshStatistics() {

        try {
            withContext(Dispatchers.IO) {
                networkStatistics = Network.info.getStatisticsAsync().await()
                database.statisticsDao.insertStatistics(*networkStatistics.asDatabaseStatistics())
            }
        } catch (e : Exception) {
            var builder = AlertDialog.Builder(activity)
            builder.setMessage(e.message)
            builder.setCancelable(true)
            builder.setPositiveButton("Ok", DialogInterface.OnClickListener {
                    dialog: DialogInterface?, _: Int ->
                dialog?.cancel()
            })
            var alert = builder.create()
            alert.show()
        }

    }

}

class ProblemsRepository(private val database: ContestsDatabase, private val activity : FragmentActivity) {

    private lateinit var networkProblems : List<NetworkProblem>

    val problems : LiveData<List<Problem>> = Transformations.map(database.problemsDao.getProblems()) {
        it?.asDomainProblem()
    }

    suspend fun refreshProblems() {

        try {
            withContext(Dispatchers.IO) {
                networkProblems = Network.info.getProblemsAsync().await()
                database.problemsDao.insertProblems(*networkProblems.asDatabaseProblem())
            }
        } catch (e : Exception) {
            var builder = AlertDialog.Builder(activity)
            builder.setMessage(e.message)
            builder.setCancelable(true)
            builder.setPositiveButton("Ok", DialogInterface.OnClickListener {
                    dialog: DialogInterface?, _: Int ->
                dialog?.cancel()
            })
            var alert = builder.create()
            alert.show()
        }

    }

}
