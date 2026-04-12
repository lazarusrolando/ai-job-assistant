package io.lazar.jobassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.lazar.jobassistant.ui.screens.*
import io.lazar.jobassistant.ui.theme.AIJobAssistantTheme

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object JobMatch : Screen("job_match", "Job Match", Icons.Filled.Work)
    object Description : Screen("description", "Description", Icons.Filled.Description)
    object CareerPath : Screen("career_path", "Career Path", Icons.Filled.TrendingUp)
    object History : Screen("history", "History", Icons.Filled.History)
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings)
}

class MainActivity : ComponentActivity() {

    private val viewModelFactory by lazy {
        ViewModelFactory(
            application as JobAssistantApp,
            (application as JobAssistantApp).suggestionRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AIJobAssistantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JobAssistantApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobAssistantApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        Screen.JobMatch,
        Screen.Description,
        Screen.CareerPath,
        Screen.History,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (currentRoute == screen.route) screen.icon else
                                    when (screen) {
                                        Screen.JobMatch -> Icons.Outlined.Work
                                        Screen.Description -> Icons.Outlined.Description
                                        Screen.CareerPath -> Icons.Outlined.TrendingUp
                                        Screen.History -> Icons.Outlined.History
                                        Screen.Settings -> Icons.Outlined.Settings
                                    },
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.JobMatch.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.JobMatch.route) {
                JobMatchScreen()
            }
            composable(Screen.Description.route) {
                JobDescriptionScreen()
            }
            composable(Screen.CareerPath.route) {
                CareerPathScreen()
            }
            composable(Screen.History.route) {
                HistoryScreen((application as JobAssistantApp).suggestionRepository)
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
