package com.aimarsg.serietracker.ui.pantallas

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.data.entities.SerieUsuario
import com.aimarsg.serietracker.ui.componentes.NuevoSiguiendo
import com.aimarsg.serietracker.ui.SeriesViewModel
import com.aimarsg.serietracker.ui.componentes.DialogoBorrar
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

@Composable
fun SiguiendoScreen(
    modifier: Modifier = Modifier,
    viewModel: SeriesViewModel,
) {
    val openNewDialog = rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        val lista by viewModel.seriesSiguiendo.collectAsState(initial = emptyList())
        LazyColumn(
            modifier = modifier.padding(top = 20.dp)
        ) {

            items(items = lista, key = {it.titulo}){ item ->
                ItemSerieSiguiendo(serie = item, viewModel = viewModel)
            }
        }
        ExtendedFloatingActionButton(
            onClick = { openNewDialog.value = true },
            icon = { Icon(Icons.Filled.Add, stringResource(R.string.Añadir)) },
            text = { Text(text = stringResource(R.string.Añadir)) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
        if (openNewDialog.value) {
            NuevoSiguiendo(
                onDismissRequest = { openNewDialog.value = false },
                onDoneButtonClicked = {}, // TODO onNextButtonClicked,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun ItemSerieSiguiendo(
    modifier: Modifier = Modifier,
    viewModel: SeriesViewModel,
    serie: SerieUsuario
) {
    var context = LocalContext.current
    Card(
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 15.dp)
            //.border(1.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp))
        ,
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        //shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 24.dp, bottom = 10.dp, start = 24.dp, end = 24.dp)
                // AÑADE ANIMACION AQUI PARA SUSTITUIR LO DEL EXTRA PADDING
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                //.padding(bottom = extraPadding.coerceAtLeast(0.dp))
                //Weight al composable inicial

            ) {
                Text(
                    text = serie.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = modifier.padding(bottom = 10.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.Temporada),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    if (serie.tempActual>1) {
                                        val serieAct = serie.copy(tempActual = serie.tempActual - 1, epActual = 1)
                                        viewModel.editarSerie(serieAct)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowDown,
                                    contentDescription = stringResource(R.string.Menos),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(text = serie.tempActual.toString())
                            IconButton(onClick = {
                                if (serie.tempActual<serie.numTemps) {
                                    val serieAct = serie.copy(tempActual = serie.tempActual + 1, epActual = 1)
                                    viewModel.editarSerie(serieAct)
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowUp,
                                    contentDescription = stringResource(R.string.Mas),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.Episodio),
                            style = MaterialTheme.typography.labelMedium
                        )
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    if (serie.tempActual == 1){
                                        if ( serie.epActual > 1){
                                            val serieAct = serie.copy(epActual = serie.epActual-1)
                                            viewModel.editarSerie(serieAct)
                                        }
                                    }else{
                                        if ( serie.epActual > 1){
                                            val serieAct = serie.copy(epActual = serie.epActual-1)
                                            viewModel.editarSerie(serieAct)
                                        }else{
                                            val serieAct = serie.copy(epActual = obtenerEpisodiosTemporada(serie.tempActual-1, serie.epTemp), tempActual = serie.tempActual-1)
                                            viewModel.editarSerie(serieAct)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowDown,
                                    contentDescription = stringResource(R.string.Menos),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(text = serie.epActual.toString())
                            IconButton(onClick = {
                                if (serie.tempActual == serie.numTemps){
                                    if ( serie.epActual < obtenerEpisodiosTemporada(serie.tempActual, serie.epTemp)){
                                        val serieAct = serie.copy(epActual = serie.epActual+1)
                                        viewModel.editarSerie(serieAct)
                                    }
                                }else{
                                    if ( serie.epActual < obtenerEpisodiosTemporada(serie.tempActual, serie.epTemp)){
                                        val serieAct = serie.copy(epActual = serie.epActual+1)
                                        viewModel.editarSerie(serieAct)
                                    }else{
                                        val serieAct = serie.copy(epActual = 1, tempActual = serie.tempActual+1)
                                        viewModel.editarSerie(serieAct)
                                    }
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowUp,
                                    contentDescription = stringResource(R.string.Mas),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
            var dialogoBorrarAct by rememberSaveable { mutableStateOf(false) }
            Column {
                IconButton(
                    onClick = {
                        compartirSerie(context = context,
                            subject = "",
                            summary = "${serie.titulo}, ${serie.valoracion}"
                            )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = stringResource(R.string.Compartir)
                    )
                }
                IconButton(
                    onClick = { dialogoBorrarAct = true }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.Borrar)
                    )
                }
                if ( dialogoBorrarAct ){
                    DialogoBorrar(
                        onDismiss = { dialogoBorrarAct = false },
                        onDelete = {
                            viewModel.eliminarSerie(serie)
                            dialogoBorrarAct = false
                        })
                }
            }
        }
        /*Row (
            modifier = Modifier.padding(horizontal = 30.dp)
        ){
            Divider(
                modifier = Modifier,
                    //.background(MaterialTheme.colorScheme.secondary)
                thickness = 0.75.dp,
                color = MaterialTheme.colorScheme.secondary
            )
        }*/

        LinearProgressIndicator(progress = calcularPorcentajeVisto(serie.epActual, serie.tempActual, serie.epTemp),
                                modifier = Modifier.padding(start = 30.dp), trackColor = MaterialTheme.colorScheme.outline)

        //var rating: Float by remember { mutableStateOf(3.2f) }

        RatingBar(
            modifier = Modifier
                .padding(bottom = 20.dp, top = 15.dp, start = 5.dp, end = 5.dp)
                .scale(0.75F),
            value = serie.valoracion,
            style = RatingBarStyle.Fill(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.outline),
            //painterEmpty = painterResource(id = android.R.drawable.btn_star_big_off),
            //painterFilled = painterResource(id = android.R.drawable.btn_star_big_on),
            onValueChange = {
                            val serieAct = serie.copy(valoracion = it)
                            viewModel.editarSerie(serieAct)
            },
            onRatingChanged = {
                Log.d("TAG", "onRatingChanged: $it")
            }
        )
    }
}

// FUCNIONALIDAD DE COMPARTIR / SHARE MENU
private fun compartirSerie(context: Context, subject: String, summary: String){
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.Compartir)
        )
    )
}

private fun calcularPorcentajeVisto(epActual: Int,  tempActual: Int, epTemp: String): Float{
    // Convertir el string de episodios a una lista de enteros
    val episodiosPorTemporada = epTemp.split(",").map { it.toInt() }

    // Calcular el total de episodios en todas las temporadas anteriores a la temporada actual
    val totalEpisodiosAnteriores = episodiosPorTemporada.subList(0, tempActual - 1).sum()

    // Añadir los episodios vistos en la temporada actual
    val totalEpisodiosVistos = totalEpisodiosAnteriores + epActual

    // Calcular el total de episodios en toda la serie
    val totalEpisodios = episodiosPorTemporada.sum()

    // Calcular el porcentaje de la serie vista
    val porcentajeVisto = (totalEpisodiosVistos.toFloat() / totalEpisodios.toFloat())

    return porcentajeVisto
}

private fun obtenerEpisodiosTemporada(tempActual: Int, epTemp: String): Int{
    val episodiosPorTemporada = epTemp.split(",").map { it.toInt() }

    // Obtener el número de episodios de la temporada proporcionada
    return episodiosPorTemporada[tempActual - 1]
}

/*
@Preview(showBackground = true)
@Composable
fun PreviewElemento() {
    SerieTrackerTheme(content = {
        ItemSerieSiguiendo(modifier = Modifier.fillMaxWidth(), serie = item)
    })
}

@Preview(showBackground = true)
@Composable
fun PreviewSiguiendo() {
    SerieTrackerTheme(content = {
        SiguiendoScreen(modifier = Modifier.fillMaxSize(), userInput = "", onUserInputChanged = {})
    })
}
*/

