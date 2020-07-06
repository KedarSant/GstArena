package com.example.gstarena.profileScreen

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

private const val IMAGE = "Image"

class ProfileViewModel(val activity : FragmentActivity) : ViewModel() {

    private val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)

    private val _username =  MutableLiveData<String>()

    val username : LiveData<String>
    get() = _username

    private val _email = MutableLiveData<String>()

    val email : LiveData<String>
    get() = _email

    private val _submissions = MutableLiveData<String>()

    val submissions : LiveData<String>
    get() = _submissions

    private val _ratings = MutableLiveData<String>()

    val ratings : LiveData<String>
    get() = _ratings

    private val _image = MutableLiveData<String>()

    val image : LiveData<String>
    get() = _image

    init{
        _username.value = sharedPref.getString("username","")
        _email.value = sharedPref.getString("email","")
        _submissions.value = sharedPref.getInt("accepted",0).toString()
        _ratings.value = sharedPref.getInt("ratings",0).toString()
        _image.value = sharedPref.getString(IMAGE,"").toString()
    }

    fun saveImage(string: String) {
        with(sharedPref.edit()) {
            putString(IMAGE,string)
            apply()
        }
        _image.value = string
    }

    fun deleteImage() {
        with(sharedPref.edit()) {
            remove(IMAGE)
            apply()
        }
        _image.value = ""
    }

    class Factory(private val activity: FragmentActivity) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(activity) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }


}