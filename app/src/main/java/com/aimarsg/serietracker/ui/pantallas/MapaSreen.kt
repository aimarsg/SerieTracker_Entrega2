package com.aimarsg.serietracker.ui.pantallas



import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.aimarsg.serietracker.model.webclient.Marcador
import com.aimarsg.serietracker.ui.SeriesViewModel
import com.aimarsg.serietracker.ui.componentes.mapa
import com.aimarsg.serietracker.ui.theme.SerieTrackerTheme

@Composable
fun MapaScreen(
    viewModel: SeriesViewModel
) {

    var permisoUbicacion by rememberSaveable {
        mutableStateOf(false)
    }

    var marcadores: List<Marcador>? by rememberSaveable {
        mutableStateOf(null)
    }
    val context = LocalContext.current

    if (marcadores == null){
        viewModel.getMarcadores {
            if (it != null) {
                marcadores = it
            }
        }
    }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            // Permission is granted
            permisoUbicacion = true
        } else {
            // Permission is denied
            permisoUbicacion = false
        }
    }

    val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    if (ContextCompat.checkSelfPermission(LocalContext.current, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(LocalContext.current, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        // Permission is already granted
        permisoUbicacion = true
    } else {
        // Request for permission
        LaunchedEffect(permissions) {
            requestPermissionLauncher.launch(permissions)
        }
    }

    if (marcadores != null) {
        mapa(marcadores!!, permisoUbicacion)
    }



    /*if (location.checkPermissions(context)){

    }else{
        // TODO: mostrar mensaje de error
        Text(
            text = stringResource(id = R.string.permisos_no_concedidos),

            )

    with(NotificationManagerCompat.from(context)){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Si no hay permisos, solicitarlos en tiempo de ejecución
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            return@with
        }
        if (marcadores == null){
            viewModel.getMarcadores {
                if (it != null) {
                    marcadores = it
                }
            }
        }
        if (marcadores != null) {
            mapa(marcadores!!)
        }
    }
    }*/
}


// make a preview of the mapascreen composable
@Preview(showBackground = true)
@Composable
fun MapaScreenPreview() {
    SerieTrackerTheme {
        //MapaScreen()
    }

}