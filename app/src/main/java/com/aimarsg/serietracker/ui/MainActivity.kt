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



// appmodule.kt
// object module
// guardar los singleton
// se utiliza para la base de datos
// viewmodel para injectar repositorios y en los repositorios



// los repositorios son singletons que van a estar definidos en el modulo.
// estos repositorios se injectan en los viewmodel. los repositorios se usan para conectar con los datos (datastore, database etc)




// copiar la funcion para generar la base de datos la primera vez, y despues eso se usa desde las daos.
// (las daos son las funciones para hacer las operaciones a la bd)
// el script de creacion de la bd se puede sacar del emulador

// language conf se guarda la logica para cambiar de idioma - el idiioma se guarda en el datastore


/**
 *  --------- base de datos ---------
 *  inserciones directamente a la base de datos y los datos se actualizan automaticamente con los lfows
 *  viewmodel -> repositorio -> dao -> base de daots
 *
 *  en la dao las operaciones que hacen acceso a la base de datos
 *
 *
 *
 *  definicion de las entidades -> @Entity
 *  data class -> es el mismo para la bd y para los objetos
 *
 *  opcional: añadir indices para que la busqueda vaya más rápido
 *
 *  para que la lista no vaya lageada_> hay que hacer los dataclasses inmutables (las que se rendereizan)
 *  hacer una copia del objeto, y usar la copia para hacer el update de la base de datos.
 *  (hace falta hacer la copia porque al ser inmutable no se puede modificar para pasarselo despues al update de la base d datos)
 *
 *
 *  el usuario logeado se mete en el putextra cuando se pasa de un activity al otro --> se puede recoger del viewmodel (savedstatehandle)
 *
 *
 *  !!! para la fecha hace falta unn type converter -> sqlite no tiene fecha , se guarda en miliseconds
 *  se define un convertirdor , copiarlo del codigo de iker esta en la dao de la base de datos
 *  el convertidor hay que copiarlo de etzi
 */
