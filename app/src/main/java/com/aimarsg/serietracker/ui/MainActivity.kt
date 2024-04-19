package com.aimarsg.serietracker.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.services.AlarmScheduler
import com.aimarsg.serietracker.services.suscribeToFCM
import com.aimarsg.serietracker.ui.theme.SerieTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * App's main activity
 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // viewModel initialization
    private val viewmodel by viewModels<SeriesViewModel>()  //hiltviewmodel

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO updateData()
        installSplashScreen()
        setContent {
            val windowSize = calculateWindowSizeClass(this)
            SerieTrackerTheme(
                // Select the app theme based on the theme salected by the user (dark/light)
                useDarkTheme = viewmodel.tema.collectAsState(initial = true).value
            ){
                var logedIn by rememberSaveable {
                    mutableStateOf(false)
                }
                //val alarmScheduler = AlarmScheduler(this)

                // check if there is internet conection
                if (!isNetworkAvailable(this)) {
                    // show a toast
                    val mensaje = stringResource(R.string.no_internet)
                    Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
                }else {
                    logedIn = viewmodel.obtenerUsuarioLogeado() != ""
                    if (logedIn) {
                        Log.d("login", "Usuario logeado1: $logedIn")
                        viewmodel.loginUsuarioGuardado()
                        //viewmodel.downloadUserData()
                        suscribeToFCM(this)
                    }
                }
                //alarmScheduler.schedule()
                // Update the app language, to restore the previous app language in case a different
                // language has been stablished before closing the app
                viewmodel.reloadLang(viewmodel.idioma.collectAsState(initial = viewmodel.idiomaActual).value, this)

                // initialize the navigation controller
                var navHostController  = rememberNavController()
                var landscape by rememberSaveable {
                    mutableStateOf(false)
                }

                Log.d("login", "Usuario logeado2: $logedIn")

                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    SerieTrackerApp(
                        viewModel = viewmodel,
                        navController = navHostController,
                        logedIn = logedIn
                    )
                }

            }
        }
    }
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected ?: false
    }
}
