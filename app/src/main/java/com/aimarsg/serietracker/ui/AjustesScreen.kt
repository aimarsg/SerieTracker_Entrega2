package com.aimarsg.serietracker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aimarsg.serietracker.data.Idioma
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.ui.theme.SerieTrackerTheme

@Composable
fun Ajustes(
    modifier: Modifier = Modifier,
    viewModel: SeriesViewModel
){
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .padding(horizontal = 50.dp)

    ) {

        Row(

        ) {
            Column {
                Icon(painter = painterResource(R.drawable.baseline_translate_24), contentDescription = stringResource(R.string.SeleccionarIdioma))
            }

            Column (
            ){
                var expanded by remember { mutableStateOf(false) }
                //val idiomas = listOf("Castellano", "English", "Euskera")
                val idiomaSeleccionado by viewModel.idioma.collectAsState(initial = Idioma.Castellano)

                Text(text = stringResource(R.string.SeleccionarIdioma), modifier = Modifier.padding(start = 10.dp))
                TextButton(onClick = { expanded = true }) {
                    Text(text = idiomaSeleccionado.name)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    Idioma.entries.forEach { idioma ->
                        DropdownMenuItem(
                            text = { Text(idioma.name) },
                            onClick = {
                                expanded = false
                                viewModel.updateIdioma(idioma, context)
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(20.dp))
        Divider(
            modifier = Modifier,
            //.background(MaterialTheme.colorScheme.secondary)
            thickness = 0.75.dp,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.padding(20.dp))

        Row {
            Column {
                Icon(
                    painter = painterResource(R.drawable.baseline_color_lens_24),
                    contentDescription = stringResource(R.string.SeleccionaModo)
                )
            }

            Column(
            ) {
                //var expanded by remember { mutableStateOf(false) }
                val modos =
                    listOf(stringResource(R.string.ModoClaro), stringResource(R.string.ModoOscuro))
                val booleanState by viewModel.tema.collectAsState(initial = true)

                Text(
                    text = stringResource(R.string.SeleccionaModo),
                    modifier = Modifier.padding(start = 10.dp)
                )
                TextButton(
                    onClick = {
                        //expanded = true
                        if (booleanState){
                            viewModel.updateTheme(false)
                        }else{
                            viewModel.updateTheme(true)
                        }
                    }
                ) {
                    if (!booleanState){
                        Text(text = stringResource(R.string.ModoClaro))
                    }
                    else{
                        Text(text = stringResource(R.string.ModoOscuro))
                    }
                }
                /*DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    modos.forEach { modo ->
                        DropdownMenuItem(
                            text = { Text(modo) },
                            onClick = {
                                expanded = false

                                viewModel.updateTheme(true)
                            }
                        )
                    }
                }*/
            }
        }
        Spacer(modifier = Modifier.padding(20.dp))
        Divider(
            modifier = Modifier,
            //.background(MaterialTheme.colorScheme.secondary)
            thickness = 0.75.dp,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.padding(20.dp))

        Row {
            Icon(painter = painterResource(R.drawable.baseline_download_24), contentDescription = "" )
            Text(text = stringResource(R.string.exportardatos), modifier = Modifier.padding(start = 10.dp))
        }
    }
}

@Composable
fun LanguageSelectionInput(
    onLanguageSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("Español") }

    val languages = listOf("Español", "Euskara", "English")

    Box(
        modifier = Modifier
            .width(150.dp)
            .height(50.dp)
            .padding(8.dp)
            .clickable { expanded = true },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            /*Text(
                text = selectedLanguage,
                style = MaterialTheme.typography.titleMedium
            )*/
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.padding(start = 8.dp),
                content = {
                    languages.forEach { language ->
                        DropdownMenuItem(
                            onClick = {
                                selectedLanguage = language
                                expanded = false
                                onLanguageSelected(language)
                            },
                            text = {}
                        )
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAjustes (){
    SerieTrackerTheme(content = {
        //Ajustes(modifier = Modifier)
    })
}