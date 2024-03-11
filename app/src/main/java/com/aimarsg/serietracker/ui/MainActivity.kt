package com.aimarsg.serietracker.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
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

        setContent {
            val windowSize = calculateWindowSizeClass(this)
            SerieTrackerTheme(
                // Select the app theme based on the theme salected by the user (dark/light)
                useDarkTheme = viewmodel.tema.collectAsState(initial = true).value
            ){

                // Update the app language, to restore the previous app language in case a different
                // language has been stablished before closing the app
                viewmodel.reloadLang(viewmodel.idioma.collectAsState(initial = viewmodel.idiomaActual).value, this)

                // initialize the navigation controller
                var navHostController  = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {

                    // choose between the portrait or landscape screen depending on the device
                    // orientation or screen size
                    when (windowSize.widthSizeClass) {
                        WindowWidthSizeClass.Compact -> {
                            SerieTrackerApp(
                                viewModel = viewmodel,
                                navController = navHostController
                            )
                        }
                        WindowWidthSizeClass.Expanded -> {
                            SerieTrackerAppLandscape(
                                viewModel = viewmodel,
                                navController = navHostController
                            )
                        }
                    }
                }

            }
        }
    }
}
