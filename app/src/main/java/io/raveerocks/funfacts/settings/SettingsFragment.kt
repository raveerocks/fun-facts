package io.raveerocks.funfacts.settings

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import io.raveerocks.funfacts.ActivityViewModel
import io.raveerocks.funfacts.R

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        const val TAG = "SettingsFragment"
    }

    private val activityViewModel: ActivityViewModel by lazy { ViewModelProvider(requireActivity())[ActivityViewModel::class.java] }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        activityViewModel.authenticationState.observe(viewLifecycleOwner) { authenticationState ->
            when (authenticationState) {
                ActivityViewModel.AuthenticationState.AUTHENTICATED -> Log.i(TAG, "Authenticated")
                ActivityViewModel.AuthenticationState.UNAUTHENTICATED -> navController.navigate(
                    R.id.loginFragment
                )
                else -> Log.e(
                    TAG, "New $authenticationState state that doesn't require any UI change"
                )
            }
        }
    }
}
