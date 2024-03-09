package com.aimarsg.serietracker.ui.componentes

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.data.entities.SerieCatalogo
import com.aimarsg.serietracker.data.entities.SerieUsuario
import com.aimarsg.serietracker.ui.SeriesViewModel
import com.aimarsg.serietracker.ui.theme.SerieTrackerTheme
import com.aimarsg.serietracker.utils.today
//import com.kanyidev.searchable_dropdown.SearchableExpandedDropDownMenu
import kotlinx.datetime.LocalDate
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NuevoSiguiendo(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    viewModel: SeriesViewModel,
    onDoneButtonClicked: (Int) -> Unit
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

            var expanded by remember{mutableStateOf(false)}
            val lista by viewModel.seriesCatalogo.collectAsState(initial = emptyList())
            /*Box(
                modifier = Modifier
                    .fillMaxWidth()
                    //.padding(32.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded, onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = viewModel.serieSeleccionada.titulo,
                        onValueChange = { },
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        //label = { Text(text = stringResource(R.string.Titulo))},
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        lista.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item.titulo) },
                                onClick = {
                                    viewModel.serieSeleccionada = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            */
            val keyboardController = LocalSoftwareKeyboardController.current
            SearchableExpandedDropDownMenu(
                listOfItems = lista,
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                onDropDownItemSelected = { item -> // Returns the item selected in the dropdown
                    viewModel.serieSeleccionada = item
                    expanded = false
                },
                enable = expanded,
                placeholder = { stringResource(R.string.Titulo)},
                colors = OutlinedTextFieldDefaults.colors(), // Customize the colors of the input text, background and content used in a text field in different states
                dropdownItem = { item ->
                    Text(item.titulo)
                },
                defaultItem = {SerieCatalogo("", 1, "")},
                onSearchTextFieldClicked = {
                    keyboardController?.show()
                }
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
                Button(onClick = {
                    var nuevaSerie = SerieUsuario(
                        titulo = viewModel.serieSeleccionada.titulo,
                        numTemps = viewModel.serieSeleccionada.numTemps,
                        epTemp = viewModel.serieSeleccionada.epTemp,
                        recordatorio = LocalDate.fromEpochDays(0),
                        siguiendo = true,
                        tempActual = 1, epActual = 1,
                        valoracion = 0.0F
                    )
                    viewModel.serieSeleccionada = SerieCatalogo("", 0, "")
                    viewModel.addSerie(nuevaSerie)
                    onDismissRequest()
                }) {
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NuevoPendiente(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onDoneButtonClicked: (Int) -> Unit,
    viewModel: SeriesViewModel
) {
    Dialog(onDismissRequest = { onDismissRequest() } )
    {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = stringResource(R.string.Titulo),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp),
                textAlign = TextAlign.Center,
            )

            var expanded by remember{mutableStateOf(false)}
            val lista by viewModel.seriesCatalogo.collectAsState(initial = emptyList())
            val keyboardController = LocalSoftwareKeyboardController.current
            SearchableExpandedDropDownMenu(
                listOfItems = lista,
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                onDropDownItemSelected = { item -> // Returns the item selected in the dropdown
                    viewModel.serieSeleccionada = item
                    expanded = false
                },
                enable = expanded,
                placeholder = { stringResource(R.string.Titulo)},
                colors = OutlinedTextFieldDefaults.colors(), // Customize the colors of the input text, background and content used in a text field in different states
                dropdownItem = { item ->
                    Text(item.titulo)
                },
                defaultItem = {SerieCatalogo("", 1, "")},
                onSearchTextFieldClicked = {
                    keyboardController?.show()
                }
            )


            Text(
                text = stringResource(R.string.FechaRecordatorio),
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 40.dp),
                textAlign = TextAlign.Center,
            )

            var datePickerOpened by rememberSaveable { mutableStateOf(false) }
            var selectedDate by rememberSaveable { mutableStateOf(LocalDate.today.toString()) }

            OutlinedTextField(
                value = selectedDate,
                onValueChange = {},
                modifier = Modifier
                    .padding(16.dp)
                    .clickable(onClick = { datePickerOpened = true }),
                enabled = false, // Deshabilitar la edición
            )
            if (datePickerOpened) {
                DateDialog(
                    onDismissRequest = { datePickerOpened = false },
                    onDateEntered = { selectedDate = it.toString()
                                        datePickerOpened = false
                                    },
                )
            }
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
                Button(onClick = {
                    var nuevaSerie = SerieUsuario(
                        titulo = viewModel.serieSeleccionada.titulo,
                        numTemps = viewModel.serieSeleccionada.numTemps,
                        epTemp = viewModel.serieSeleccionada.epTemp,
                        recordatorio = LocalDate.parse(selectedDate),
                        siguiendo = false,
                        tempActual = 1, epActual = 1,
                        valoracion = 0.0F
                    )
                    viewModel.serieSeleccionada = SerieCatalogo("", 0, "")
                    viewModel.addSerie(nuevaSerie)
                    onDismissRequest()
                }) {
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
        NuevoSiguiendo(onDismissRequest = { }, onDoneButtonClicked = {}, viewModel = viewModel())
    })
}

@Preview(showBackground = true)
@Composable
fun PreviewDialog2(){
    SerieTrackerTheme(content = {
        NuevoPendiente(onDismissRequest = {  }, onDoneButtonClicked = {}, viewModel = viewModel())
    })
}

