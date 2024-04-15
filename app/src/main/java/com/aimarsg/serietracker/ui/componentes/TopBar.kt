package com.aimarsg.serietracker.ui.componentes

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.ui.SeriesViewModel
import com.aimarsg.serietracker.ui.TrackerScreen
import com.aimarsg.serietracker.ui.isNetworkAvailable

/**
 * Top bar of the app. It shows the screen's name. When not in main screen, 'back' button is shown.
 * It has a dropdown menu and a link to the settings screen.
 * @param currentScreen: CurrentScreen object which contains screen name
 * @param canNavigateBack: false if the current screen is the main screen, true else
 * @param navigateUp: action to be completed when navigating back
 * @param navController: navcontroller of the app
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SerieTrackerTopBar(
    currentScreen: TrackerScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: SeriesViewModel
) {
    val (isExpanded, setExpanded) = rememberSaveable { mutableStateOf(false) }
    var helpDialogOpened by rememberSaveable{mutableStateOf(false)}
    var logoutDialogOpened by rememberSaveable{mutableStateOf(false)}
    var context = LocalContext.current
    var noConnection by rememberSaveable{mutableStateOf(false)}

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
            IconButton(onClick = { helpDialogOpened = true }) {
                Icon(painter = painterResource(R.drawable.baseline_help_24), contentDescription = stringResource(R.string.ayuda), tint = (MaterialTheme.colorScheme.onPrimary))
            }
            IconButton(onClick = { logoutDialogOpened = true }) {
                Icon(painter = painterResource(R.drawable.baseline_logout_24), contentDescription = stringResource(R.string.Logout), tint = (MaterialTheme.colorScheme.onPrimary))
            }
            if (helpDialogOpened){
                HelpDialog(onDismissRequest = {helpDialogOpened = false}, onConfirmation = {helpDialogOpened = false})
            }
            if (logoutDialogOpened){
                DialogoLogout(
                    onDismiss = {logoutDialogOpened = false},
                    onDelete = {
                        logoutDialogOpened = false
                        viewModel.uploadUserData()
                        viewModel.logout()
                        navController.navigate(TrackerScreen.Login.name)
                    }
                )
            }
            IconButton(onClick = { setExpanded(true) }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = stringResource(R.string.Opciones), tint = (MaterialTheme.colorScheme.onPrimary))
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { setExpanded(false) },
                    offset = DpOffset(x = 0.dp, y = 0.dp)
                ) {
                    DropdownMenuItem(
                        onClick = {
                            if (!isNetworkAvailable(context = context)){
                                noConnection = true
                            }else{
                                viewModel.uploadUserData()
                            }
                            setExpanded(false)
                        },
                        text = { Text(text = stringResource(R.string.subir)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_cloud_upload_24),
                                contentDescription = stringResource(R.string.subir)
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            if (!isNetworkAvailable(context = context)){
                                noConnection = true
                            }else{
                                viewModel.downloadUserData()
                            }
                            setExpanded(false)
                        },
                        text = { Text(text = stringResource(R.string.descargar)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_cloud_download_24),
                                contentDescription = stringResource(R.string.descargar)
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            if (!isNetworkAvailable(context = context)){
                                noConnection = true
                            }else{
                                viewModel.updateCatalogue()
                            }
                            setExpanded(false)
                        },
                        text = { Text(text = stringResource(R.string.actualizar_catalogo)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_library_books_24),
                                contentDescription = stringResource(R.string.actualizar_catalogo)
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            //navController.navigate(TrackerScreen.Ajustes.name)
                            if (navController.currentBackStackEntry?.destination?.route != TrackerScreen.Mapa.name){
                                navController.navigate(TrackerScreen.Mapa.name)
                            }
                            setExpanded(false)
                        },
                        text = { Text(text = stringResource(R.string.Mapa)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.baseline_map_24),
                                contentDescription = stringResource(R.string.Mapa)
                            )
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            //navController.navigate(TrackerScreen.Ajustes.name)
                            if (navController.currentBackStackEntry?.destination?.route != TrackerScreen.Ajustes.name){
                                navController.navigate(TrackerScreen.Ajustes.name)
                            }
                            setExpanded(false)
                        },
                        text = { Text(text = stringResource(R.string.Ajustes)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = stringResource(R.string.Ajustes)
                            )
                        }
                    )
                }
            }
            if (noConnection){
                val mensaje = stringResource(R.string.no_internet)
                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
            }
        }
    )
}
