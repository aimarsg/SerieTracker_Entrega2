package com.aimarsg.serietracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.ui.theme.SerieTrackerTheme


@Composable
fun NuevoSiguiendo(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onNextButtonClicked: (Int) -> Unit,
    onUserInputChanged: (String) -> Unit,
    userInput  :String
) {
    Dialog(onDismissRequest = { onDismissRequest() } ) {
        Card(
            modifier = Modifier
                .width(550.dp)
                //.height(400.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = stringResource(R.string.Titulo),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp),
                textAlign = TextAlign.Center,
            )
            OutlinedTextField(
                value = userInput,
                onValueChange = { onUserInputChanged(it) },
                label = { Text(text = stringResource(R.string.Titulo))},
                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
            )
            Row(
                modifier = modifier
                    .padding(20.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(onClick = { onDismissRequest() }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.Guardar)
                    )
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "")
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.Cancelar)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoPendiente(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onNextButtonClicked: (Int) -> Unit,
    onUserInputChanged: (String) -> Unit,
    userInput  :String
) {
    DatePickerDialog(
        onDismissRequest = {  } ,
        confirmButton = {  }, //TODO
        dismissButton = { }
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
                //.fillMaxWidth(),
                //.height(400.dp)
                //.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            /*Text(
                text = stringResource(R.string.Titulo),
                modifier = Modifier, //.padding(start = 20.dp, end = 20.dp, top = 40.dp),
                textAlign = TextAlign.Center,
            )*/
            OutlinedTextField(
                value = userInput,
                onValueChange = { onUserInputChanged(it) },
                label = { Text(text = stringResource(R.string.Titulo))},
                modifier = Modifier.padding(start = 20.dp, end = 20.dp)
            )
            Text(
                text = stringResource(R.string.FechaRecordatorio),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp),
                textAlign = TextAlign.Center,
            )

            val datePickerState = rememberDatePickerState()

            DatePicker(
                state = datePickerState,
                title = null,
                headline = null,
                showModeToggle = false,
                modifier = Modifier.padding(bottom = 0.dp)
            )
            Row(
                modifier = modifier
                    //.padding(20.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(onClick = { onDismissRequest() }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.Guardar)
                    )
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "")
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.Cancelar)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPendiente(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onNextButtonClicked: (Int) -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = {  } ,
        confirmButton = {  }, //TODO
        dismissButton = { }
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = stringResource(R.string.FechaRecordatorio),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp),
                textAlign = TextAlign.Center,
            )
            val datePickerState = rememberDatePickerState()
            DatePicker(
                state = datePickerState,
                title = null,
                modifier = Modifier.padding(bottom = 0.dp)
            )
            Row(
                modifier = modifier
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(onClick = { onDismissRequest() }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.Guardar)
                    )
                }
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "")
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.Cancelar)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDialog(){
    SerieTrackerTheme(content = {
        NuevoSiguiendo(onDismissRequest = { /*TODO*/ }, onNextButtonClicked = {}, onUserInputChanged = {}, userInput = "")
    })
}

@Preview(showBackground = true)
@Composable
fun PreviewDialog2(){
    SerieTrackerTheme(content = {
        NuevoPendiente(onDismissRequest = { /*TODO*/ }, onNextButtonClicked = {}, onUserInputChanged = {}, userInput = "")
    })
}

@Preview(showBackground = true)
@Composable
fun PreviewDialog3(){
    SerieTrackerTheme(content = {
        EditarPendiente(onDismissRequest = { /*TODO*/ }, onNextButtonClicked = {})
    })
}