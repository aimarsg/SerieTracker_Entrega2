package com.aimarsg.serietracker.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.navigation.compose.rememberNavController
import com.aimarsg.serietracker.ui.theme.SerieTrackerTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewmodel by viewModels<SeriesViewModel>()  //hiltviewmodel
    //private val viewmodel : SeriesViewModel = HiltViewModel()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSize = calculateWindowSizeClass(this)
            SerieTrackerTheme(
                useDarkTheme = viewmodel.tema.collectAsState(initial = true).value
            ){
                //Log.d("a", "idioma oncreate ${viewmodel.idioma.collectAsState(initial = viewmodel.idiomaActual).value}")
                viewmodel.reloadLang(viewmodel.idioma.collectAsState(initial = viewmodel.idiomaActual).value, this)
                var navHostController  = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    //color = MaterialTheme.colorScheme.background
                ) {
                    /*SerieTrackerApp(
                        viewModel = viewmodel
                    )*/
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
