package com.aimarsg.serietracker.ui

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.ui.theme.SerieTrackerTheme
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle

@Composable
fun SiguiendoScreen(
    modifier: Modifier = Modifier,
    onUserInputChanged: (String) -> Unit,
    userInput: String
) {
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
                ItemSerieSiguiendo()
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
                onNextButtonClicked = {}, // TODO onNextButtonClicked,
                onUserInputChanged = onUserInputChanged,
                userInput = userInput
            )
        }
    }
}

@Composable
fun ItemSerieSiguiendo(
    modifier: Modifier = Modifier
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
                    text = "PLACEHOLDER TITULO ",/*TODO TITULO*/
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
                                onClick = { /*TODO*/ }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowDown,
                                    contentDescription = stringResource(R.string.Menos),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(text = "0") /*TODO*/
                            IconButton(onClick = { /*TODO*/ }) {
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
                                onClick = { /*TODO*/ }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowDown,
                                    contentDescription = stringResource(R.string.Menos),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Text(text = "0") /*TODO*/
                            IconButton(onClick = { /*TODO*/ }) {
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
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = stringResource(R.string.Compartir)
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

        var rating: Float by remember { mutableStateOf(3.2f) } //TODO PONER ESTO COMO VARIABLE

        RatingBar(
            modifier = Modifier
                .padding(bottom = 20.dp, top = 15.dp, start = 5.dp, end = 5.dp)
                .scale(0.75F),
            value = rating,
            style = RatingBarStyle.Fill(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary),
            //painterEmpty = painterResource(id = android.R.drawable.btn_star_big_off),
            //painterFilled = painterResource(id = android.R.drawable.btn_star_big_on),
            onValueChange = {
                rating = it
            },
            onRatingChanged = {
                Log.d("TAG", "onRatingChanged: $it")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewElemento() {
    SerieTrackerTheme(content = {
        ItemSerieSiguiendo(modifier = Modifier.fillMaxWidth())
    })
}

@Preview(showBackground = true)
@Composable
fun PreviewSiguiendo() {
    SerieTrackerTheme(content = {
        SiguiendoScreen(modifier = Modifier.fillMaxSize(), userInput = "", onUserInputChanged = {})
    })
}


