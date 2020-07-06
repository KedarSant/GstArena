package com.example.gstarena.ratingsScreen

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gstarena.domain.Rating
import com.example.gstarena.titleScreen.TitleViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class RatingsViewModel(val activity : FragmentActivity) : ViewModel() {

    private lateinit var json : String

    private val _data = MutableLiveData<List<Rating>>()

    val data : LiveData<List<Rating>>
    get() = _data

    init {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        json = sharedPref.getString("ratingsChange","").toString()
        val type: Type = object : TypeToken<List<Rating?>?>(){}.type
        _data.value = Gson().fromJson(json,type)
    }

    class Factory(private val activity: FragmentActivity) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RatingsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RatingsViewModel(activity) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}