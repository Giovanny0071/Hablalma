package com.example.hablalma.ui.theme.Screens.LoginScreen

import com.google.firebase.auth.FirebaseUser
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hablalma.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

    class LoginViewModel : ViewModel() {

        private val auth = FirebaseAuth.getInstance()

        private val _isLoading = MutableStateFlow(false)
        val isLoading = _isLoading.asStateFlow()

        private val _loginSuccess = MutableStateFlow(false)
        val loginSuccess = _loginSuccess.asStateFlow()

        private val _errorMessage = MutableStateFlow<String?>(null)
        val errorMessage = _errorMessage.asStateFlow()

        private val _googleToastMessage = MutableStateFlow<String?>(null)
        val googleToastMessage = _googleToastMessage.asStateFlow()

        val isUserLoggedIn: Boolean
            get() = auth.currentUser != null

        fun loginWithEmail(email: String, password: String) {
            prepareRequest()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    finishRequest(task.isSuccessful, task.exception)
                }
        }

        fun registerWithEmail(email: String, password: String) {
            prepareRequest()
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    finishRequest(task.isSuccessful, task.exception)
                }
        }

        fun loginWithGoogle(idToken: String) {
            if (idToken.isBlank()) {
                _errorMessage.value = "Google no devolvió un identificador válido."
                return
            }

            prepareRequest()

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    finishRequest(task.isSuccessful, task.exception)
                }
        }

        fun launchGoogle(context: Context, isRegistering: Boolean) {
            viewModelScope.launch {
                try {
                    val googleClientId = context.getString(R.string.default_web_client_id)
                    val credentialManager = CredentialManager.create(context)

                    val googleOption = if (isRegistering) {
                        GetSignInWithGoogleOption.Builder(googleClientId).build()
                    } else {
                        GetGoogleIdOption.Builder()
                            .setServerClientId(googleClientId)
                            .setFilterByAuthorizedAccounts(false)
                            .setAutoSelectEnabled(false)
                            .build()
                    }

                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleOption)
                        .build()

                    val result = credentialManager.getCredential(context, request)
                    val credential = GoogleIdTokenCredential.createFrom(result.credential.data)

                    loginWithGoogle(credential.idToken)
                } catch (_: GetCredentialCancellationException) {
                    _googleToastMessage.value = "Se canceló el acceso con Google."
                } catch (_: GetCredentialException) {
                    _googleToastMessage.value = "No se pudo seleccionar la cuenta de Google."
                } catch (_: GoogleIdTokenParsingException) {
                    _googleToastMessage.value = "La respuesta de Google no es válida."
                }
            }
        }

        fun consumeGoogleToastMessage() {
            _googleToastMessage.value = null
        }

        fun getCurrentUser(): FirebaseUser? = auth.currentUser

        fun signOut() {
            auth.signOut()
            _loginSuccess.value = false
            _errorMessage.value = null
        }

        private fun prepareRequest() {
            _isLoading.value = true
            _loginSuccess.value = false
            _errorMessage.value = null
        }

        private fun finishRequest(success: Boolean, exception: Exception?) {
            _isLoading.value = false
            _loginSuccess.value = success

            if (!success) {
                _errorMessage.value =
                    exception?.localizedMessage ?: "No se pudo completar la operación."
            }
        }
    }
