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
import com.example.faith.MainActivity
import com.example.faith.login.CredentialsManager
import com.google.android.material.navigation.NavigationView

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    // Login/logout-related properties
    private lateinit var account: Auth0
    private var cachedCredentials: Credentials? = null
    private var cachedUserProfile: UserProfile? = null

    private var currentUserDetails: MutableMap<String, Any> = mutableMapOf<String, Any>()

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

        // Binding inflation
        binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater, R.layout.fragment_login, container, false)

        // Button Listeners
        binding.buttonLogin.setOnClickListener { login() }
        binding.buttonLogout.setOnClickListener { logout() }

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
                    CredentialsManager.setLoggedIn()
                    cachedCredentials = credentials
                    showSnackBar(getString(R.string.login_success_message, credentials.accessToken))
                    CredentialsManager.saveCredentials(requireContext(), credentials)
                    checkForValidToken()
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
                    CredentialsManager.setLoggedOut()
                    cachedCredentials = null
                    cachedUserProfile = null
                    updateUI()
                    currentUserDetails["userId"] = 0
                    currentUserDetails["userName"] = ""
                    CredentialsManager.setUserDetails(currentUserDetails)
                    setNavigationVisibility()
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
                    setUserDetails(userProfile)
                    setNavigationVisibility()
                }

            })
    }

    private fun setUserMetadata(userName: String) {
        // Guard against getting the metadata when no user is logged in
        if (cachedCredentials == null) {
            return
        }

        val usersClient = UsersAPIClient(account, cachedCredentials!!.accessToken!!)

        currentUserDetails["userName"] = userName

        usersClient
            .updateMetadata(cachedUserProfile!!.getId()!!, currentUserDetails)
            .start(object : Callback<UserProfile, ManagementException> {

                override fun onFailure(exception: ManagementException) {
                    showSnackBar(getString(R.string.general_failure_with_exception_code,
                        exception.getCode()))
                }

                override fun onSuccess(profile: UserProfile) {
                    cachedUserProfile = profile
                    updateUI()

                    showSnackBar(getString(R.string.general_success_message))
                }

            })
    }

    private fun setUserDetails(profile: UserProfile) {
        val userMetadata = profile.getUserMetadata()
        val userId = userMetadata["userId"] as String?
        val userName = userMetadata["userName"] as String?
        val isMonitor = userMetadata["isMonitor"] as String?
        val userIdList = userMetadata["userIdList"] as ArrayList<Double>?
        if (userId != null && userName != null && isMonitor != null && userIdList != null) {
            currentUserDetails["userId"] = userId.toLong()
            currentUserDetails["userName"] = userName.toString()
            currentUserDetails["isMonitor"] = isMonitor.toBoolean()
            if (currentUserDetails["isMonitor"] as Boolean) {
                currentUserDetails["userIdList"] = userIdList.map {
                    it.toLong()
                }.toList()
            } else {
                currentUserDetails["userIdList"] = emptyList<Long>()
            }
            CredentialsManager.setUserDetails(currentUserDetails)
        }
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
                    var userDetails = CredentialsManager.getUserDetails()
                    if (userDetails["userName"] != currentUserDetails["userName"]) {
                        setUserMetadata(userDetails["userName"] as String)
                    }
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

    private fun setNavigationVisibility() {
        val activity: MainActivity = context as MainActivity
        val navigationView = activity.findViewById(R.id.navView) as NavigationView
        if (CredentialsManager.isLoggedIn.value == true) {
            if (currentUserDetails["isMonitor"] as Boolean) {
                navigationView.menu.findItem(R.id.homeFragment).isVisible = false
                navigationView.menu.findItem(R.id.profileFragment).isVisible = false
                navigationView.menu.findItem(R.id.monitorOverviewFragment).isVisible = true
            } else {
                navigationView.menu.findItem(R.id.homeFragment).isVisible = true
                navigationView.menu.findItem(R.id.profileFragment).isVisible = true
                navigationView.menu.findItem(R.id.monitorOverviewFragment).isVisible = false
            }
        } else {
            navigationView.menu.findItem(R.id.homeFragment).isVisible = false
            navigationView.menu.findItem(R.id.profileFragment).isVisible = false
            navigationView.menu.findItem(R.id.monitorOverviewFragment).isVisible = false
        }
    }
}