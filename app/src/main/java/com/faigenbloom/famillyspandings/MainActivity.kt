@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.faigenbloom.famillyspandings

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.faigenbloom.famillyspandings.comon.Destination
import com.faigenbloom.famillyspandings.login.LoginPage
import com.faigenbloom.famillyspandings.login.LoginPageViewModel
import com.faigenbloom.famillyspandings.onboarding.OnboardingPage
import com.faigenbloom.famillyspandings.register.RegisterPage
import com.faigenbloom.famillyspandings.register.RegisterPageViewModel
import com.faigenbloom.famillyspandings.spandings.SpandingsPage
import com.faigenbloom.famillyspandings.spandings.SpendingsPageViewModel
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isLoggedIn by remember {
                mutableStateOf(false)
            }
            val mainNavController = rememberNavController()
            FamillySpandingsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Scaffold(
                        bottomBar = {
                            if (isLoggedIn) {
                                BottomNavigationBar(
                                    onDestinationChanged = {
                                        mainNavController.navigate(it.route)
                                    },
                                )
                            }
                        },
                    ) { _ ->
                        NavHost(
                            navController = mainNavController,
                            startDestination = if (!isLoggedIn) Destination.Onboarding.route else Destination.SpendingsPage.route,
                        ) {
                            composable(
                                route = Destination.Onboarding.route,
                            ) {
                                OnboardingPage(
                                    onLogin = {
                                        mainNavController.navigate(Destination.Login.route)
                                    },
                                    onRegister = {
                                        mainNavController.navigate(Destination.Register.route)
                                    },
                                )
                            }

                            composable(
                                route = Destination.Login.route,
                            ) {
                                val loginPageViewModel = koinViewModel<LoginPageViewModel>()
                                loginPageViewModel.onLoggedIn = {
                                    isLoggedIn = true
                                    mainNavController.navigate(Destination.SpendingsPage.route)
                                }
                                val state by loginPageViewModel
                                    .loginStateFlow
                                    .collectAsState()
                                LoginPage(state)
                            }
                            composable(
                                route = Destination.Register.route,
                            ) {
                                val registerPageViewModel = koinViewModel<RegisterPageViewModel>()
                                registerPageViewModel.onLoggedIn = {
                                    isLoggedIn = true
                                    mainNavController.navigate(Destination.SpendingsPage.route)
                                }
                                val state by registerPageViewModel
                                    .loginStateFlow
                                    .collectAsState()
                                RegisterPage(state)
                            }

                            composable(
                                route = Destination.SpendingsPage.route,
                            ) {
                                val state by koinViewModel<SpendingsPageViewModel>()
                                    .spendingsStateFlow
                                    .collectAsState()
                                SpandingsPage(state)
                            }
                        }
                    }
                }
            }
        }
    }
}
