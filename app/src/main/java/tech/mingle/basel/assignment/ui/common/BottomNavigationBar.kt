package tech.mingle.basel.assignment.ui.common

import androidx.annotation.StringRes
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import tech.mingle.basel.assignment.R


@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.onSurface
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = backStackEntry?.destination
        NavigationItems.values().forEach { item ->
            BottomNavigationItem(
                selected = currentDestination?.hierarchy?.any { it.route == item.name } == true,
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(id = item.titleRes)
                    )
                },
                label = { Text(text = stringResource(id = item.titleRes)) },
                selectedContentColor = MaterialTheme.colors.onSurface,
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(0.4f),
                alwaysShowLabel = true,
                onClick = {
                    navController.navigate(item.name) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}

enum class NavigationItems(@StringRes val titleRes: Int, val icon: ImageVector) {
    MAP(R.string.tabs_map_title, Icons.Default.Star),
    AIRPORTS(R.string.tabs_airports_title, Icons.Default.Star),
    SETTINGS(R.string.tabs_settings_title, Icons.Default.Settings)
}