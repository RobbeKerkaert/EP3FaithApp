package com.example.faith.ui.login

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.management.ManagementException
import com.auth0.android.management.UsersAPIClient
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.example.faith.databinding.FragmentLoginBinding
import com.example.faith.R
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.faith.MainActivity
import com.example.faith.database.FaithDatabase
import com.example.faith.databinding.FragmentHomeBinding
import com.example.faith.login.CredentialsManager
import com.example.faith.login.LoginViewModel
import com.example.faith.login.LoginViewModelFactory
import com.example.faith.ui.home.HomeViewModel
import com.example.faith.ui.home.HomeViewModelFactory

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    // Login/logout-related properties
    private lateinit var account: Auth0
    private var cachedCredentials: Credentials? = null
    private var cachedUserProfile: UserProfile? = null

    var currentUserDetails: MutableMap<String, Any> = mutableMapOf<String, Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // For action bar title
        (activity as MainActivity).supportActionBar?.title = "Log In"

        setHasOptionsMenu(true)

        super.onCreateView(inflater, container, savedInstanceState)
        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )

        // All for viewmodel
        val application = requireNotNull(this.activity).application
        val dataSource = FaithDatabase.getInstance(application).userDatabaseDao
        val viewModelFactory = LoginViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        // Binding inflation
        binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater,
            R.layout.fragment_login, container, false)

        // Button Listeners
        binding.buttonLogin.setOnClickListener {
            login()
        }
        binding.buttonLogout.setOnClickListener { logout() }
//        binding.buttonGet.setOnClickListener { getUserMetadata() }
//        binding.buttonSet.setOnClickListener { setUserMetadata() }

        // Check if user is still logged in
        checkForValidToken()

        return binding.root
    }

    private fun checkForValidToken(){
        val token = CredentialsManager.getAccessToken(requireContext())
        if(token != null){
            showUserProfile(token)
        }
        else {
            Toast.makeText(context, "Something went wrong with the login", Toast.LENGTH_SHORT).show()
        }
    }

    // Login / Logout methods

    private fun login() {
        WebAuthProvider
            .login(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .withScope(getString(R.string.login_scopes))
            .withAudience(getString(R.string.login_audience, getString(R.string.com_auth0_domain)))
            .start(requireContext(), object : Callback<Credentials, AuthenticationException> {

                override fun onFailure(exception: AuthenticationException) {
                    showSnackBar(getString(R.string.login_failure_message, exception.getCode()))
                }

                override fun onSuccess(credentials: Credentials) {
                    cachedCredentials = credentials
                    showSnackBar(getString(R.string.login_success_message, credentials.accessToken))
                    CredentialsManager.saveCredentials(requireContext(), credentials)
                    checkForValidToken()
                    updateUI()
                }
            })

    }

    private fun logout() {
        WebAuthProvider
            .logout(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .start(requireContext(), object : Callback<Void?, AuthenticationException> {

                override fun onFailure(exception: AuthenticationException) {
                    updateUI()
                    showSnackBar(getString(R.string.general_failure_with_exception_code,
                        exception.getCode()))
                }

                override fun onSuccess(payload: Void?) {
                    cachedCredentials = null
                    cachedUserProfile = null
                    updateUI()
                    currentUserDetails["userId"] = 0
                    currentUserDetails["userName"] = ""
                    CredentialsManager.setUserDetails(currentUserDetails)
                }

            })
    }

    // Metadata methods
    private fun getUserMetadata() {
        // Guard against getting the metadata when no user is logged in
        if (cachedCredentials == null) {
            return
        }

        val usersClient = UsersAPIClient(account, cachedCredentials!!.accessToken!!)

        usersClient
            .getProfile(cachedUserProfile!!.getId()!!)
            .start(object : Callback<UserProfile, ManagementException> {

                override fun onFailure(exception: ManagementException) {
                    showSnackBar(getString(R.string.general_failure_with_exception_code,
                        exception.getCode()))
                }

                override fun onSuccess(userProfile: UserProfile) {
                    cachedUserProfile = userProfile
                    updateUI()

                    val userId = userProfile.getUserMetadata()["userId"] as String?
                    val userName = userProfile.getUserMetadata()["userName"] as String?
                    if (userId != null) {
                        currentUserDetails["userId"] = userId.toLong()
                        currentUserDetails["userName"] = userName.toString()
                        CredentialsManager.setUserDetails(currentUserDetails)
                    }
                }

            })
    }

    private fun setUserMetadata() {
        // Guard against getting the metadata when no user is logged in
        if (cachedCredentials == null) {
            return
        }

//        val usersClient = UsersAPIClient(account, cachedCredentials!!.accessToken!!)
//        val metadata = mapOf("userId" to binding.edittextCountry.text.toString())
//
//        usersClient
//            .updateMetadata(cachedUserProfile!!.getId()!!, metadata)
//            .start(object : Callback<UserProfile, ManagementException> {
//
//                override fun onFailure(exception: ManagementException) {
//                    showSnackBar(getString(R.string.general_failure_with_exception_code,
//                        exception.getCode()))
//                }
//
//                override fun onSuccess(profile: UserProfile) {
//                    cachedUserProfile = profile
//                    updateUI()
//
//                    showSnackBar(getString(R.string.general_success_message))
//                }
//
//            })
    }

    // UI Methods

    private fun showUserProfile(accessToken: String) {
        // Guard against showing the profile when no user is logged in
        if (cachedCredentials == null) {
            return
        }

        val client = AuthenticationAPIClient(account)

        client
            .userInfo(accessToken)
            .start(object : Callback<UserProfile, AuthenticationException> {

                override fun onFailure(exception: AuthenticationException) {
                    showSnackBar(getString(R.string.general_failure_with_exception_code,
                        exception.getCode()))
                }

                override fun onSuccess(profile: UserProfile) {
                    cachedUserProfile = profile
                    updateUI()
                    getUserMetadata()
                }

            })
    }

    private fun updateUI() {
        val isLoggedIn = cachedCredentials != null

        binding.textviewTitle.text = if (isLoggedIn) {
            getString(R.string.logged_in_title)
        } else {
            getString(R.string.logged_out_title)
        }
        binding.buttonLogin.isEnabled = !isLoggedIn
        binding.buttonLogout.isEnabled = isLoggedIn

        binding.textviewUserProfile.isVisible = isLoggedIn

        val userName = cachedUserProfile?.name ?: ""
        val userEmail = cachedUserProfile?.email ?: ""
        binding.textviewUserProfile.text = getString(R.string.user_profile, userName, userEmail)
    }

    private fun showSnackBar(text: String) {
        Snackbar
            .make(
                binding.root,
                text,
                Snackbar.LENGTH_LONG
            ).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }
}