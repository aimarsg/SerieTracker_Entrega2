package com.aimarsg.serietracker.ui

import android.content.res.Configuration
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNodeLifecycleCallback
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
import com.aimarsg.serietracker.ui.pantallas.LoginScreen
import com.aimarsg.serietracker.ui.pantallas.MapaScreen
import com.aimarsg.serietracker.ui.pantallas.PendienteScreen
import com.aimarsg.serietracker.ui.pantallas.RegisterScreen
import com.aimarsg.serietracker.ui.pantallas.SiguiendoScreen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

/**
 * Definition of the different app screens
 * @param title screen name
 */
enum class TrackerScreen(@StringRes val title: Int){ // STRINGRESOURCE PARA AÑADIR EL TITULO DE CADA VENTANA
    Siguiendo(title = R.string.siguiendo),
    Pendiente(title = R.string.pendiente),
    Ajustes(title = R.string.Ajustes),
    Login(title = R.string.IniciarSesion),
    Registro(title = R.string.Registro),
    Mapa(title = R.string.Mapa)

}

/**
 * App's main screen. This hosts composable hosts the scaffold which
 * contains the different screens of the app as well as the navigation behaviour
 * @param viewModel: apps viewmodel
 * @param navController: navigation controller which is common to both landscape and portrait modes
 */
@Composable
fun SerieTrackerApp(
    viewModel: SeriesViewModel,
    navController: NavHostController,
    logedIn: Boolean
) {


    val backStackEntry by navController.currentBackStackEntryAsState()
    val (siguiendo, setSiguiendo) = rememberSaveable { mutableStateOf(true) }
    val config = LocalConfiguration.current
    val currentScreen = TrackerScreen.valueOf(
        backStackEntry?.destination?.route ?: (if (!logedIn) TrackerScreen.Login.name else TrackerScreen.Siguiendo.name)
    )

    Scaffold(
        topBar = {
            if (currentScreen != TrackerScreen.Registro && currentScreen != TrackerScreen.Login){
                SerieTrackerTopBar(
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    navController = navController,
                    currentScreen = currentScreen,
                    viewModel = viewModel
                )
            }
        },
        floatingActionButton = {

        },
        bottomBar = {
            if (config.orientation != Configuration.ORIENTATION_LANDSCAPE
                && currentScreen != TrackerScreen.Registro && currentScreen != TrackerScreen.Login){
                BottomBar(navController = navController,  siguiendo = siguiendo)
            }
        }
    ) {
            innerPadding ->
        var expanded by remember { mutableStateOf(false) }
        Row {
            if (config.orientation == Configuration.ORIENTATION_LANDSCAPE
                && currentScreen != TrackerScreen.Registro && currentScreen != TrackerScreen.Login){
                AppNavigationRail(navController = navController, siguiendo = siguiendo)
            }
            NavHost(
                navController = navController,
                startDestination = if (!logedIn) TrackerScreen.Login.name else TrackerScreen.Siguiendo.name,
                modifier = Modifier.padding(innerPadding),
            ) {

                composable(route = TrackerScreen.Login.name) {
                    LoginScreen(
                        onLogedIn = {
                                    navController.popBackStack()
                                    navController.navigate(TrackerScreen.Siguiendo.name)
                                    },
                        register = {
                                    //navController.popBackStack()
                                    navController.navigate(TrackerScreen.Registro.name)
                        },
                        viewModel = viewModel
                    )
                }

                composable(route = TrackerScreen.Registro.name) {
                    RegisterScreen(
                        onRegistered = {
                                    navController.popBackStack() /*TODO*/
                                    //navController.navigate(TrackerScreen.Login.name)
                                       },
                        inicioSesion = {
                                    navController.popBackStack()
                                    //navController.navigate(TrackerScreen.Login.name)
                        },
                        viewModel = viewModel
                    )
                }

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
                    if (config.orientation == Configuration.ORIENTATION_LANDSCAPE){
                        AjustesLanscape(
                            viewModel = viewModel,
                            expanded = expanded,
                            changeExpanded = { expanded = !expanded }
                        )
                    }else{
                        Ajustes(
                            viewModel = viewModel,
                            expanded = expanded,
                            changeExpanded = { expanded = !expanded }
                        )
                    }

                }
                composable(route = TrackerScreen.Mapa.name) {
                    MapaScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
