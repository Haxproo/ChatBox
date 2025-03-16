package com.hax.chatbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHostpackage com.hax.chatbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hax.chatbox.ui.screens.ChatScreen
import com.hax.chatbox.ui.screens.HomeScreen
import com.hax.chatbox.ui.screens.LoginScreen
import com.hax.chatbox.ui.screens.ProfileScreen
import com.hax.chatbox.ui.screens.SignUpScreen
import com.hax.chatbox.ui.theme.ChatBoxTheme
import com.hax.chatbox.ui.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModel()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatBoxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatBoxApp(authViewModel)
                }
            }
        }
    }
}

@Composable
fun ChatBoxApp(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(
                    viewModel = authViewModel,
                    onNavigateToSignUp = { navController.navigate("signup") },
                    onLoginSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } }
                )
            }
            
            composable("signup") {
                SignUpScreen(
                    viewModel = authViewModel,
                    onNavigateToLogin = { navController.navigate("login") },
                    onSignUpSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } }
                )
            }
            
            composable("home") {
                HomeScreen(
                    onNavigateToChat = { chatId -> navController.navigate("chat/$chatId") },
                    onNavigateToProfile = { navController.navigate("profile") }
                )
            }
            
            composable("chat/{chatId}") { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                ChatScreen(
                    chatId = chatId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable("profile") {
                ProfileScreen(
                    viewModel = authViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onLogout = { 
                        authViewModel.signOut()
                        navController.navigate("login") { 
                            popUpTo("home") { inclusive = true } 
                        } 
                    }
                )
            }
        }
    }
}
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hax.chatbox.ui.screens.ChatScreen
import com.hax.chatbox.ui.screens.HomeScreen
import com.hax.chatbox.ui.screens.LoginScreen
import com.hax.chatbox.ui.screens.ProfileScreen
import com.hax.chatbox.ui.screens.SignUpScreen
import com.hax.chatbox.ui.theme.ChatBoxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatBoxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatBoxApp()
                }
            }
        }
    }
}

@Composable
fun ChatBoxApp() {
    val navController = rememberNavController()
    
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                LoginScreen(
                    onNavigateToSignUp = { navController.navigate("signup") },
                    onLoginSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } }
                )
            }
            
            composable("signup") {
                SignUpScreen(
                    onNavigateToLogin = { navController.navigate("login") },
                    onSignUpSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } }
                )
            }
            
            composable("home") {
                HomeScreen(
                    onNavigateToChat = { chatId -> navController.navigate("chat/$chatId") },
                    onNavigateToProfile = { navController.navigate("profile") }
                )
            }
            
            composable("chat/{chatId}") { backStackEntry ->
                val chatId = backStackEntry.arguments?.getString("chatId") ?: ""
                ChatScreen(
                    chatId = chatId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            
            composable("profile") {
                ProfileScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onLogout = { navController.navigate("login") { popUpTo("home") { inclusive = true } } }
                )
            }
        }
    }
}