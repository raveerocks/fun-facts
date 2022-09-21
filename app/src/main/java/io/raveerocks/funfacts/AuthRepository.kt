package io.raveerocks.funfacts

import android.content.Context
import androidx.lifecycle.LiveData
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository(private val context: Context) {

    val currentUser = getUser()

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val authUI = AuthUI.getInstance()

    fun logout(){
        authUI.signOut(context)
    }

    private fun getUser():LiveData<FirebaseUser?>{
        return object : LiveData<FirebaseUser?>() {
            private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
                value = firebaseAuth.currentUser
            }
            override fun onActive() {
                firebaseAuth.addAuthStateListener(authStateListener)
            }
            override fun onInactive() {
                firebaseAuth.removeAuthStateListener(authStateListener)
            }
        }
    }

}