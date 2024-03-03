package com.aimarsg.serietracker.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.ui.theme.SerieTrackerTheme

@Composable
fun PendienteScreen(
    modifier: Modifier = Modifier,
    onUserInputChanged: (String) -> Unit,
    userInput  :String
){
    val openNewDialog = rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = modifier.padding(top = 20.dp)
        ) {
            item {
                /* TODO Por cada elemento de la lista se llama a un itemSerieSiguiiendo
                    y con eso ya se displayea la lista*/
                ItemPendiente()
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
        if (openNewDialog.value){
            NuevoPendiente(
                onDismissRequest = { openNewDialog.value = false },
                onNextButtonClicked = {} , // TODO onNextButtonClicked,
                onUserInputChanged = onUserInputChanged,
                userInput = userInput
            )
        }
    }
}

@Composable
fun ItemPendiente(
    modifier: Modifier = Modifier
){
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
                //.padding(bottom = extraPadding.coerceAtLeast(0.dp))
                //Weight al composable inicial

            ) {
                Text(
                    text = "PLACEHOLDER TITULO ", /* TODO titulo*/
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
                            Text(text = "XX/XX/XXXX")/*TODO*/
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = stringResource(R.string.Editar),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            Column {
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = stringResource(R.string.Seguir)
                    )
                }
                IconButton(
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = stringResource(R.string.Borrar)
                    )
                }
            }
        }
    }
}

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


