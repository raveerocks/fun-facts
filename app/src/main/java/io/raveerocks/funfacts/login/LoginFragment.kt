package io.raveerocks.funfacts.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import io.raveerocks.funfacts.ActivityViewModel
import io.raveerocks.funfacts.ActivityViewModel.AuthenticationState.AUTHENTICATED
import io.raveerocks.funfacts.R
import io.raveerocks.funfacts.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    companion object {
        const val TAG = "LoginFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private lateinit var binding: FragmentLoginBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private val activityViewModel: ActivityViewModel by lazy { ViewModelProvider(requireActivity())[ActivityViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.i(TAG, "Successfully signed in user " + "${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                Log.i(TAG, "Sign in unsuccessful")
            }
        }

        bind()
        setupNavigation()
        setupObservers()
        return binding.root
    }

    private fun bind() {
        binding.authButton.setOnClickListener { launchSignInFlow() }
    }

    private fun setupNavigation() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack(R.id.mainFragment, false)
        }
    }

    private fun setupObservers() {
        activityViewModel.authenticationState.observe(viewLifecycleOwner) { authenticationState ->
            when (authenticationState) {
                AUTHENTICATED -> findNavController().popBackStack()
                else ->
                    Log.e(
                        TAG,
                        "Authentication state that doesn't require any UI change $authenticationState"
                    )
            }
        }
    }

    private fun launchSignInFlow() {
        launcher.launch(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                    arrayListOf(
                        AuthUI
                            .IdpConfig
                            .EmailBuilder()
                            .build(),
                        AuthUI
                            .IdpConfig
                            .GoogleBuilder()
                            .build()
                    )
                )
                .build()
        )

    }

}
