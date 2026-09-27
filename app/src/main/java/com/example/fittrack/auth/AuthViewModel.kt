package com.example.fittrack.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val uid: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

/**
 * Handles cloud authentication via Firebase Auth (email/password).
 * Swap in Google Sign-In / phone auth the same way if needed.
 */
class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var state: AuthState = AuthState.Idle
        private set

    val currentUserId: String?
        get() = auth.currentUser?.uid

    fun register(email: String, password: String, onResult: (AuthState) -> Unit) {
        state = AuthState.Loading
        viewModelScope.launch {
            state = try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                AuthState.Success(result.user?.uid.orEmpty())
            } catch (e: Exception) {
                AuthState.Error(e.message ?: "Registration failed")
            }
            onResult(state)
        }
    }

    fun login(email: String, password: String, onResult: (AuthState) -> Unit) {
        state = AuthState.Loading
        viewModelScope.launch {
            state = try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                AuthState.Success(result.user?.uid.orEmpty())
            } catch (e: Exception) {
                AuthState.Error(e.message ?: "Login failed")
            }
            onResult(state)
        }
    }

    fun logout() {
        auth.signOut()
        state = AuthState.Idle
    }
}
