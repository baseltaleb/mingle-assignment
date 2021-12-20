package tech.mingle.basel.assignment.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tech.mingle.basel.assignment.ui.airports.AirportListScreen
import tech.mingle.basel.assignment.ui.airports.AirportListViewModel
import tech.mingle.basel.assignment.ui.map.MapScreen
import tech.mingle.basel.assignment.ui.map.MapViewModel
import tech.mingle.basel.assignment.ui.settings.SettingsScreen
import tech.mingle.basel.assignment.ui.settings.SettingsViewModel
import tech.mingle.basel.assignment.ui.common.BottomNavigationBar
import tech.mingle.basel.assignment.ui.common.NavigationItems

@Composable
fun AssignmentNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavigationItems.MAP.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavigationItems.MAP.name) {
                val mapViewModel: MapViewModel = hiltViewModel()
                MapScreen(mapViewModel)
            }
            composable(NavigationItems.AIRPORTS.name) {
                val airportListViewModel: AirportListViewModel = hiltViewModel()
                AirportListScreen(airportListViewModel)
            }
            composable(NavigationItems.SETTINGS.name) {
                val settingsViewModel: SettingsViewModel = hiltViewModel()
                SettingsScreen(settingsViewModel)
            }
        }
    }
}
