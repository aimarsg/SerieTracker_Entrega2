package com.aimarsg.serietracker.ui.pantallas

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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.data.entities.SerieUsuario
import com.aimarsg.serietracker.ui.pantallas.componentes.NuevoSiguiendo
import com.aimarsg.serietracker.ui.SeriesViewModel
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
                                    val serieAct = serie.copy(tempActual = serie.tempActual-1)
                                    viewModel.editarSerie(serieAct)
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
                                val serieAct = serie.copy(tempActual = serie.tempActual+1)
                                viewModel.editarSerie(serieAct)
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
                                    val serieAct = serie.copy(epActual = serie.epActual-1)
                                    viewModel.editarSerie(serieAct)
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
                                val serieAct = serie.copy(epActual = serie.epActual+1)
                                viewModel.editarSerie(serieAct)
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

            Column {
                IconButton(
                    onClick = { /* TODO*/ }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = stringResource(R.string.Compartir)
                    )
                }
                IconButton(
                    onClick = { viewModel.eliminarSerie(serie) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.Borrar)
                    )
                }
            }
        }
        Row (
            modifier = Modifier.padding(horizontal = 30.dp)
        ){
            Divider(
                modifier = Modifier,
                    //.background(MaterialTheme.colorScheme.secondary)
                thickness = 0.75.dp,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        //var rating: Float by remember { mutableStateOf(3.2f) }

        RatingBar(
            modifier = Modifier
                .padding(bottom = 20.dp, top = 15.dp, start = 5.dp, end = 5.dp)
                .scale(0.75F),
            value = serie.valoracion,
            style = RatingBarStyle.Fill(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary),
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

