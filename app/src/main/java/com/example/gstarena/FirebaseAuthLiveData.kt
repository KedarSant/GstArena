package com.example.gstarena

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthLiveData : MutableLiveData<FirebaseUser>() {

    private var authStateListener : FirebaseAuth.AuthStateListener =  FirebaseAuth.AuthStateListener { auth->
        value = auth.currentUser
    }

    override fun onInactive() {
        super.onInactive()
        FirebaseAuth.getInstance().removeAuthStateListener (authStateListener)
    }

    override fun onActive() {
        super.onActive()
        FirebaseAuth.getInstance().addAuthStateListener (authStateListener)
    }
}