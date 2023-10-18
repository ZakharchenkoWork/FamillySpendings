package com.faigenbloom.famillyspandings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.faigenbloom.famillyspandings.comon.Destination
import com.faigenbloom.famillyspandings.login_page.LoginPage
import com.faigenbloom.famillyspandings.login_page.LoginPageViewModel
import com.faigenbloom.famillyspandings.onboarding.OnboardingPage
import com.faigenbloom.famillyspandings.spandings_page.SpandingsPage
import com.faigenbloom.famillyspandings.spandings_page.SpendingsPageViewModel
import com.faigenbloom.famillyspandings.ui.theme.FamillySpandingsTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainNavController = rememberNavController()
            FamillySpandingsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = mainNavController,
                        startDestination = Destination.Onboarding.route
                    ) {
                        composable(
                            route = Destination.Onboarding.route
                        ) {
                            OnboardingPage(onLogin = {
                                mainNavController.navigate(Destination.Login.route)
                            }, onRegister = {

                            })
                        }

                        composable(
                            route = Destination.Login.route
                        ) {
                            val loginPageViewModel = koinViewModel<LoginPageViewModel>()

                            loginPageViewModel.onLoggedIn = {
                                mainNavController.navigate(Destination.SpendingsPage.route)
                            }

                            val state by loginPageViewModel
                                .loginStateFlow
                                .collectAsState()
                            LoginPage(state)
                        }
                        composable(
                            route = Destination.SpendingsPage.route
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

