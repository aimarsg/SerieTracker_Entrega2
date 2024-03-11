package com.aimarsg.serietracker.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.ui.componentes.AppNavigationRail
import com.aimarsg.serietracker.ui.componentes.BottomBar
import com.aimarsg.serietracker.ui.componentes.SerieTrackerTopBar
import com.aimarsg.serietracker.ui.pantallas.Ajustes
import com.aimarsg.serietracker.ui.pantallas.AjustesLanscape
import com.aimarsg.serietracker.ui.pantallas.PendienteScreen
import com.aimarsg.serietracker.ui.pantallas.SiguiendoScreen

/**
 * Definition of the different app screens
 * @param title screen name
 */
enum class TrackerScreen(@StringRes val title: Int){ // STRINGRESOURCE PARA AÑADIR EL TITULO DE CADA VENTANA
    Siguiendo(title = R.string.siguiendo),
    Pendiente(title = R.string.pendiente),
    Ajustes(title = R.string.Ajustes)

}

/**
 * App's portrait mode main screen. This hosts composable hosts the scaffold which
 * contains the different screens of the app as well as the navigation behaviour
 * @param viewModel: apps viewmodel
 * @param navController: navigation controller which is common to both landscape and portrait modes
 */
@Composable
fun SerieTrackerApp(
    viewModel: SeriesViewModel,
    navController: NavHostController
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val (siguiendo, setSiguiendo) = rememberSaveable { mutableStateOf(true) }

    val currentScreen = TrackerScreen.valueOf(
        backStackEntry?.destination?.route ?: TrackerScreen.Siguiendo.name
    )


    Scaffold(
        topBar = {
            SerieTrackerTopBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                navController = navController,
                currentScreen = currentScreen
            )
        },
        floatingActionButton = {

        },
        bottomBar = { BottomBar(navController = navController,  siguiendo = siguiendo) }
    ) {
            innerPadding ->

        NavHost(
            navController = navController,
            startDestination = TrackerScreen.Siguiendo.name,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(route = TrackerScreen.Siguiendo.name){
                SiguiendoScreen(
                    viewModel = viewModel
                )
                setSiguiendo(true)
            }

            composable(route = TrackerScreen.Pendiente.name){
                PendienteScreen(
                    viewModel = viewModel
                )
                setSiguiendo(false)
            }

            composable(route = TrackerScreen.Ajustes.name){
                Ajustes(
                    viewModel = viewModel,
                )
            }
        }
    }
}

/**
 * App's landscape mode main screen. This hosts composable hosts the scaffold which
 * contains the different screens of the app as well as the navigation behaviour
 * @param viewModel: apps viewmodel
 * @param navController: navigation controller which is common to both landscape and portrait modes
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun SerieTrackerAppLandscape(
    viewModel: SeriesViewModel,
    navController: NavHostController
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val (siguiendo, setSiguiendo) = rememberSaveable { mutableStateOf(true) }

    val currentScreen = TrackerScreen.valueOf(
        backStackEntry?.destination?.route ?: TrackerScreen.Siguiendo.name
    )

    Scaffold(
        topBar = {
            SerieTrackerTopBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                navController = navController,
                currentScreen = currentScreen
            )
        }
    ) {
        innerPadding ->
        Row {
            AppNavigationRail(navController = navController, siguiendo = siguiendo)
            NavHost(
                navController = navController,
                startDestination = TrackerScreen.Siguiendo.name,
                modifier = Modifier.padding(innerPadding)
            ) {

                composable(route = TrackerScreen.Siguiendo.name) {
                    SiguiendoScreen(
                        viewModel = viewModel
                    )
                    setSiguiendo(true)
                }

                composable(route = TrackerScreen.Pendiente.name) {
                    PendienteScreen(
                        viewModel = viewModel
                    )
                    setSiguiendo(false)
                }

                composable(route = TrackerScreen.Ajustes.name) {
                    AjustesLanscape(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
