package com.aimarsg.serietracker.ui.pantallas

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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.model.entities.SerieUsuario
import com.aimarsg.serietracker.ui.SeriesViewModel
import com.aimarsg.serietracker.ui.componentes.DateDialog
import com.aimarsg.serietracker.ui.componentes.DialogoBorrar
import com.aimarsg.serietracker.ui.componentes.NuevoPendiente
import kotlinx.datetime.LocalDate

/**
 * Screen that shows a list of the series that the user has marked as pending,
 * with a floating button to add a new one
 * @param viewModel app's viewmodel
 */
@Composable
fun PendienteScreen(
    modifier: Modifier = Modifier,
    viewModel: SeriesViewModel,
){
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        var dialogOpen by rememberSaveable {
            mutableStateOf(false)
        }

        // Display a list of the series

        val lista by viewModel.seriesPendiente.collectAsState(initial = emptyList())
        LazyColumn(
            modifier = modifier.padding(top = 20.dp)
        ) {

            items(items = lista, key = {it.titulo}){ item ->
                ItemPendiente(serie = item, viewModel = viewModel)
            }

        }

        // Floating button to add a new serie

        ExtendedFloatingActionButton(
            onClick = {
                        dialogOpen = true
                      },
            icon = { Icon(Icons.Filled.Add, stringResource(R.string.Añadir)) },
            text = { Text(text = stringResource(R.string.Añadir)) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
        if (dialogOpen){
            NuevoPendiente(
                onDismissRequest = { dialogOpen = false },
                onDoneButtonClicked = {} , // TODO onNextButtonClicked,
                viewModel = viewModel
            )
        }
    }
}


/**
 * Composable to display each pending series as a card with two buttons, play (start following) and delete
 * Each series has also a reminder date that can be modified
 * @param viewModel app's main viewmodel
 * @param serie: series item that has to be displayed
 */
@Composable
fun ItemPendiente(
    modifier: Modifier = Modifier,
    viewModel: SeriesViewModel,
    serie: SerieUsuario
){
    var datepickerOpen by rememberSaveable {
        mutableStateOf(false)
    }
    Card(
        modifier = modifier
            .padding(vertical = 10.dp, horizontal = 15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
    ) {
        Row (
            modifier = Modifier
                .padding(24.dp)
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
            ) {
                Text(
                    text = serie.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = modifier.padding(bottom = 10.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column() {
                        Text(text = stringResource(R.string.FechaRecordatorio), style = MaterialTheme.typography.labelMedium)
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // CODE TO OPEN THE DATEPICKER IN ORDER TO MODIFY THE REMINDER DATE //

                            Text(text = serie.recordatorio.toString())
                            IconButton(onClick = {
                                datepickerOpen = true
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = stringResource(R.string.Editar),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            if (datepickerOpen) {
                                DateDialog(
                                    onDismissRequest = { datepickerOpen = false },
                                    onDateEntered = { viewModel.selectedDate = it.toString()
                                        datepickerOpen = false
                                        val serieAct = serie.copy(recordatorio = LocalDate.parse(viewModel.selectedDate))
                                        viewModel.editarSerie(serieAct)
                                    },
                                )
                            }
                        }
                    }
                }
            }

            var deleteOpened by rememberSaveable {
                mutableStateOf(false)
            }
            // DELETE BUTTON AND CONFIRMATION DIALOG //
            Column {
                IconButton(
                    onClick = {
                        val serieAct = serie.copy(siguiendo = true)
                        viewModel.editarSerie(serieAct)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = stringResource(R.string.Seguir)
                    )
                }
                IconButton(
                    onClick = {
                        deleteOpened = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.Borrar)
                    )
                }
            }
            if ( deleteOpened ){
                DialogoBorrar(
                    onDismiss = { deleteOpened = false },
                    onDelete = {
                        viewModel.eliminarSerie(serie)
                        deleteOpened = false
                    })
            }
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun PreviewElementoPendiente(){
    SerieTrackerTheme(content = {
        ItemPendiente(modifier = Modifier.fillMaxWidth())
    })
}

@Preview(showBackground = true)
@Composable
fun PreviewPendiente(){
    SerieTrackerTheme(content = {
        PendienteScreen(modifier = Modifier.fillMaxSize(), onUserInputChanged = {}, userInput = "")
    })
}
*/

