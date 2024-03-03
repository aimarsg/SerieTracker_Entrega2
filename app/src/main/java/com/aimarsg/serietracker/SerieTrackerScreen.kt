package com.aimarsg.serietracker

import androidx.annotation.StringRes
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aimarsg.serietracker.ui.Ajustes
import com.aimarsg.serietracker.ui.PendienteScreen
import com.aimarsg.serietracker.ui.SeriesViewModel
import com.aimarsg.serietracker.ui.SiguiendoScreen
import com.aimarsg.serietracker.ui.theme.SerieTrackerTheme

//  RUTAS PARA LA NAVEGACION
enum class TrackerScreen(@StringRes val title: Int){ // STRINGRESOURCE PARA AÑADIR EL TITULO DE CADA VENTANA
    Siguiendo(title = R.string.siguiendo),
    Pendiente(title = R.string.pendiente),
    Ajustes(title = R.string.Ajustes)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SerieTrackerTopBar(
    currentScreen: TrackerScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val (isExpanded, setExpanded) = rememberSaveable { mutableStateOf(false) }
    TopAppBar(
        title = {  Text(text = stringResource(currentScreen.title), color = MaterialTheme.colorScheme.onPrimary) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = stringResource(R.string.Atras),

                    )
                }
            }
        },

        actions = {
            IconButton(onClick = { setExpanded(true) }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = stringResource(R.string.Opciones), tint = (MaterialTheme.colorScheme.onPrimary))
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { setExpanded(false) },
                    offset = DpOffset(x = 0.dp, y = 0.dp)
                ) {
                    DropdownMenuItem(
                        onClick = {
                            //navController.navigate(TrackerScreen.Ajustes.name)
                            if (navController.currentBackStackEntry?.destination?.route != TrackerScreen.Ajustes.name){
                                navController.navigate(TrackerScreen.Ajustes.name)
                            }
                            setExpanded(false)
                        },
                        text = { Text(text = stringResource(R.string.Ajustes))},
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = stringResource(R.string.Ajustes)
                            )
                        }
                    )
                }
            }
        }
    )
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    siguiendo: Boolean,
    //setSiguiendo: (Boolean) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        NavigationBarItem(
            icon = {
                if (siguiendo){
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = stringResource(R.string.siguiendo))
                }else{
                    Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = stringResource(R.string.siguiendo))
                }
            },
            label = {
                Text(text =stringResource(R.string.siguiendo))
            },
            selected = siguiendo,
            onClick = {
                if (siguiendo){
                    // do nothing
                }else{
                    navController.navigateUp()
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = stringResource(R.string.pendiente))
            },
            label = {
                Text(text =stringResource(R.string.pendiente))
            },
            selected = !siguiendo,
            onClick = {
                if (siguiendo){
                    navController.navigate(TrackerScreen.Pendiente.name)
                }else{
                    // do nothing
                }
            }
        )
    }
}

@Composable
fun SerieTrackerApp(
    viewModel: SeriesViewModel,
    navController: NavHostController = rememberNavController()
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
        bottomBar = { BottomBar(navController = navController,  siguiendo = siguiendo)}
    ) {
            innerPadding ->

        NavHost(
            navController = navController,
            startDestination = TrackerScreen.Siguiendo.name,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(route = TrackerScreen.Siguiendo.name){
                SiguiendoScreen(
                    userInput = viewModel.tituloInput,
                    onUserInputChanged = {viewModel.tituloInput = it}
                )
                setSiguiendo(true)
            }

            composable(route = TrackerScreen.Pendiente.name){
                PendienteScreen(
                    userInput = viewModel.tituloInput,
                    onUserInputChanged = {viewModel.tituloInput = it}
                )
                setSiguiendo(false)
            }

            composable(route = TrackerScreen.Ajustes.name){
                Ajustes(
                    viewModel = viewModel,
                )
                //setSiguiendo(false)
            }
        }
    }
}


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
}
