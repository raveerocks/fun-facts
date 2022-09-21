package io.raveerocks.funfacts.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import io.raveerocks.funfacts.ActivityViewModel
import io.raveerocks.funfacts.ActivityViewModel.AuthenticationState.AUTHENTICATED
import io.raveerocks.funfacts.ActivityViewModel.AuthenticationState.UNAUTHENTICATED
import io.raveerocks.funfacts.R
import io.raveerocks.funfacts.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val activityViewModel: ActivityViewModel by  lazy { ViewModelProvider(requireActivity())[ActivityViewModel::class.java] }
    private val mainViewModel: MainViewModel by  lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        bind()
        setupObservers()
        return binding.root
    }

    private fun bind() {
        binding.apply {
            settingsBtn.setOnClickListener {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment())
            }
        }
    }

    private fun setupObservers() {
        activityViewModel.authenticationState.observe(viewLifecycleOwner) { authenticationState ->
            when (authenticationState) {
                AUTHENTICATED -> { onAuthenticated() }
                else -> { onUnAuthenticated() }
            }
        }
    }

    private fun onUnAuthenticated() {
        binding.apply {
            authButton.text = getString(R.string.login_button_text)
            authButton.setOnClickListener { findNavController().navigate(R.id.loginFragment) }
            welcomeText.text = mainViewModel.getFactToDisplay(UNAUTHENTICATED,requireContext())
        }
    }

    private fun onAuthenticated() {
        binding.apply {
            authButton.text = getString(R.string.logout_button_text)
            authButton.setOnClickListener {
               activityViewModel.logOut()
            }
            welcomeText.text = getFactWithPersonalization(mainViewModel.getFactToDisplay(AUTHENTICATED,requireContext()))
        }
    }

    private fun getFactWithPersonalization(fact: String): String {
        return String.format(
            resources.getString(
                R.string.welcome_message_authed,
                FirebaseAuth.getInstance().currentUser?.displayName,
                Character.toLowerCase(fact[0]) + fact.substring(1)
            )
        )
    }

}