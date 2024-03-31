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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.model.entities.SerieUsuario
import com.aimarsg.serietracker.ui.SeriesViewModel
import com.aimarsg.serietracker.ui.componentes.DialogoBorrar
import com.aimarsg.serietracker.ui.componentes.NuevoSiguiendo
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

/**
 * Screen that shows a list of the series that the user has marked as following,
 * with a floating button to add a new one
 * @param viewModel app's viewmodel
 */
@Composable
fun SiguiendoScreen(
    modifier: Modifier = Modifier,
    viewModel: SeriesViewModel,
) {

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        var newDialogOpen by rememberSaveable {
            mutableStateOf(false)
        }
        val lista by viewModel.seriesSiguiendo.collectAsState(initial = emptyList())
        LazyColumn(
            modifier = modifier.padding(top = 20.dp)
        ) {

            // DISPLAY A LIST OF THE FOLLOWING SERIES //

            items(items = lista, key = {it.titulo}){ item ->
                ItemSerieSiguiendo(serie = item, viewModel = viewModel)
            }
        }

        // FLOATING BUTTON TO ADD A NEW SERIE //

        ExtendedFloatingActionButton(
            onClick = { newDialogOpen = true },
            icon = { Icon(Icons.Filled.Add, stringResource(R.string.Añadir)) },
            text = { Text(text = stringResource(R.string.Añadir)) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
        if (newDialogOpen) {
            NuevoSiguiendo(
                onDismissRequest = { newDialogOpen = false },
                onDoneButtonClicked = {}, // TODO onNextButtonClicked,
                viewModel = viewModel
            )
        }
    }
}


/**
 * Composable to display each following series as a card with two buttons, share and delete.
 * It has buttons to modify the current season or chapter, and it shows a progressbar
 * representing the amount of viewed chapters.
 * At the botton it renders a clickable rating bar with the series rating.
 * @param viewModel: apps main viewmodel
 * @param serie: series item to be displayed
 */
@Composable
fun ItemSerieSiguiendo(
    modifier: Modifier = Modifier,
    viewModel: SeriesViewModel,
    serie: SerieUsuario
) {
    val context = LocalContext.current
    var deleteOpen by rememberSaveable {
        mutableStateOf(false)
    }
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

                        // CURRENT SEASON AND BOTTONS TO MODIFY IT //

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

                        // CURRENT CHAPTER AND BOTTONS TO MODIFY //

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

            // SHARE AND DELETE BUTTONS //


            val mensaje = stringResource(R.string.estoyViendo) + "'" + serie.titulo + "'" + stringResource(R.string.enTemporada) + serie.tempActual.toString() + stringResource(R.string.enEpisodio) + serie.epActual.toString()
            Column {
                IconButton(
                    onClick = {
                        compartirSerie(context = context,
                            subject = "",
                            summary = mensaje
                            )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = stringResource(R.string.Compartir)
                    )
                }
                IconButton(
                    onClick = { deleteOpen = true }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.Borrar)
                    )
                }
                if ( deleteOpen ){
                    DialogoBorrar(
                        onDismiss = { deleteOpen = false },
                        onDelete = {
                            viewModel.eliminarSerie(serie)
                            deleteOpen = false
                        })
                }
            }
        }

        // PROGRESS BAR INDICATING THE % OF VIEWED EPISODES

        LinearProgressIndicator(
            progress = { calcularPorcentajeVisto(serie.epActual, serie.tempActual, serie.epTemp) },
            modifier = Modifier.padding(start = 30.dp),
            trackColor = MaterialTheme.colorScheme.outline,
        )

        // RATING BAR

        RatingBar(
            modifier = Modifier
                .padding(bottom = 20.dp, top = 15.dp, start = 5.dp, end = 5.dp)
                .scale(0.75F),
            value = serie.valoracion,
            style = RatingBarStyle.Fill(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.outline),
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

/**
 * Function to create the implicit intent to share the series information to another app
 * It shares the current chapter and season information as a string
 * @param context: apps context
 * @param subject: message's subject
 * @param summary: the text to send to the other apps
 */
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

/**
 * Function that calculates the % of the seen episodes, based on the series infomation and the users
 * last episode
 * @param epActual: users current episode
 * @param tempActual: users current season
 * @param epTemp: series information about the episodes per season
 */
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

/**
 * This function reuturns the number of episodes of a specific season of a series
 * @param tempActual: the season of wich number of episodes we want to calculate
 * @param epTemp: information about the episodes per season of that series
 */
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

