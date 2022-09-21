package io.raveerocks.funfacts

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations



class ActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository: AuthRepository by  lazy { AuthRepository(application.applicationContext) }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED
    }

    val authenticationState = Transformations.map(authRepository.currentUser){ user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    fun logOut(){
        authRepository.logout()
    }
}
