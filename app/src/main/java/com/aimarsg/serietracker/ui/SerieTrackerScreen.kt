package com.aimarsg.serietracker.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.ui.componentes.AppNavigationRail
import com.aimarsg.serietracker.ui.componentes.SerieTrackerTopBar
import com.aimarsg.serietracker.ui.componentes.BottomBar
import com.aimarsg.serietracker.ui.pantallas.Ajustes
import com.aimarsg.serietracker.ui.pantallas.AjustesLanscape
import com.aimarsg.serietracker.ui.pantallas.PendienteScreen
import com.aimarsg.serietracker.ui.pantallas.SiguiendoScreen
import com.aimarsg.serietracker.ui.theme.SerieTrackerTheme

//  RUTAS PARA LA NAVEGACION
enum class TrackerScreen(@StringRes val title: Int){ // STRINGRESOURCE PARA AÑADIR EL TITULO DE CADA VENTANA
    Siguiendo(title = R.string.siguiendo),
    Pendiente(title = R.string.pendiente),
    Ajustes(title = R.string.Ajustes)

}


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



/*
@Preview(showBackground = true)
@Composable
fun topBarPreview(){
    SerieTrackerTheme(content = {
        //SerieTrackerTopBar(canNavigateBack = true /*  TODO*/ , navigateUp = { /*TODO*/ }, navController = NavHostController(this))
    })
}

@Preview(showBackground = true)
@Composable
fun serieAppPreviewNav(){
    SerieTrackerTheme(content = {
        //SerieTrackerApp()
    })
}*/
