package com.example.hablalma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.hablalma.ui.theme.HablalmaTheme
import com.example.hablalma.ui.theme.Screens.LoginScreen.LoginScreen
import com.example.hablalma.ui.theme.Screens.LoginScreen.LoginViewModel


class MainActivity : ComponentActivity() {

    private val lgv: LoginViewModel = LoginViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HablalmaTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable(Destinations.login) {
                        LoginScreen()
                    }

                }
            }
        }
    }
}