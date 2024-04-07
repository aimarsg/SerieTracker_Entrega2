package com.aimarsg.serietracker.ui.pantallas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import com.aimarsg.serietracker.model.webclient.Marcador
import com.aimarsg.serietracker.ui.SeriesViewModel
import com.aimarsg.serietracker.ui.componentes.mapa
import com.aimarsg.serietracker.ui.theme.SerieTrackerTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Composable
fun MapaScreen(
    viewModel: SeriesViewModel
) {
    var marcadores: List<Marcador>? by rememberSaveable {
        mutableStateOf(null)
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


// make a preview of the mapascreen composable
@Preview(showBackground = true)
@Composable
fun MapaScreenPreview() {
    SerieTrackerTheme {
        //MapaScreen()
    }

}