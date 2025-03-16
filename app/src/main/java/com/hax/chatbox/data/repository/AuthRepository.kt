package com.hax.chatbox.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import com.hax.chatbox.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser
    
    init {
        // Update the current user when Firebase Auth state changes
        firebaseAuth.addAuthStateListener { auth ->
            auth.currentUser?.let { firebaseUser ->
                _currentUser.value = mapFirebaseUser(firebaseUser)
            } ?: run {
                _currentUser.value = null
            }
        }
    }
    
    suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Authentication failed")
            val user = mapFirebaseUser(firebaseUser)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun signUp(email: String, password: String, username: String): Result<User> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("User creation failed")
            
            // Create a user profile
            val user = User(
                id = firebaseUser.uid,
                username = username,
                email = email
            )
            
            // Here you would typically store additional user info in Firestore
            // For now, we'll just return the user
            
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun signOut() {
        firebaseAuth.signOut()
    }
    
    fun getCurrentFirebaseUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
    
    private fun mapFirebaseUser(firebaseUser: FirebaseUser): User {
        return User(
            id = firebaseUser.uid,
            username = firebaseUser.displayName ?: "User",
            email = firebaseUser.email ?: "",
            profilePictureUrl = firebaseUser.photoUrl?.toString() ?: ""
        )
    }
}