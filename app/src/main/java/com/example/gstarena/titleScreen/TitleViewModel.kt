package com.example.gstarena.titleScreen

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gstarena.FirebaseAuthLiveData
import com.example.gstarena.network.Network
import com.example.gstarena.network.User
import com.example.gstarena.network.asDomainRating
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class TitleViewModel(val activity : FragmentActivity) : ViewModel() {

    var user = FirebaseAuthLiveData()
    private lateinit var _user : User

    suspend fun saveUserDetails() {
        try {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
            if (sharedPref.getString("username","")!= "") return
            withContext(Dispatchers.IO) {
                _user = Network.info.getUserAsync(user.value?.email.toString()).await()
                val ratings = _user.rantingChange.asDomainRating()
                val json = Gson().toJson(ratings)
                with(sharedPref.edit()) {
                    putString("_id", _user._id)
                    putString("name", _user.name)
                    putString("username", _user.username)
                    putString("email", _user.email)
                    putString("about", _user.about)
                    putInt("ratings", _user.ratings)
                    putString("codeforcesLink", _user.codeforcesLink)
                    putString("githubLink", _user.githubLink)
                    putString("codechefLink", _user.codechefLink)
                    putInt("accepted", _user.submissionStats.accepted)
                    putInt("wrongAnswer", _user.submissionStats.wrongAnswer)
                    putInt("compilationError", _user.submissionStats.compilationError)
                    putInt("runtimeError", _user.submissionStats.runtimeError)
                    putInt("timeLimitExceeded", _user.submissionStats.timeLimitExceeded)
                    putString("ratingsChange",json)
                    apply()
                }
            }
        } catch (e : Exception) {
            throw e
        }
    }

    fun clearDetails() {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
    }

    class Factory(private val activity: FragmentActivity) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TitleViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TitleViewModel(activity) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}

