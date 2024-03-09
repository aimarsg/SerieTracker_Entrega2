package com.aimarsg.serietracker.ui.pantallas

import android.content.ContentResolver
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.aimarsg.serietracker.ui.SeriesViewModel
import com.aimarsg.serietracker.ui.theme.SerieTrackerTheme
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

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
        val contentResolver = LocalContext.current.contentResolver
        val filename = "MyData.json" //stringResource(R.string.visit_json_name_template, LocalDate.now().format(DateTimeFormatter.ISO_DATE))

        val saverLauncher = rememberLauncherForActivityResult(contract = CreateDocument("application/json")) { uri ->
            if (uri != null) {
                try {
                    contentResolver.openFileDescriptor(uri, "w")?.use {
                        FileOutputStream(it.fileDescriptor).use {fileOutputStream ->
                            fileOutputStream.write(
                                //"Hello, World!".toByteArray()
                                (viewModel.seriesSiguiendoToJson()).toByteArray()
                            )
                        }
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

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
            Icon(painter = painterResource(R.drawable.baseline_download_24), contentDescription = "")

            Text(text = stringResource(R.string.exportardatos),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .clickable(onClick = { saverLauncher.launch(filename) })
            )
        }
    }
}


@Composable
fun AjustesLanscape(
    modifier: Modifier = Modifier,
    viewModel: SeriesViewModel
){
    val contentResolver = LocalContext.current.contentResolver
    val filename = "MyData.json" //stringResource(R.string.visit_json_name_template, LocalDate.now().format(DateTimeFormatter.ISO_DATE))

    val saverLauncher = rememberLauncherForActivityResult(contract = CreateDocument("application/json")) { uri ->
        if (uri != null) {
            try {
                contentResolver.openFileDescriptor(uri, "w")?.use {
                    FileOutputStream(it.fileDescriptor).use {fileOutputStream ->
                        fileOutputStream.write(
                            //"Hello, World!".toByteArray()
                            (viewModel.seriesSiguiendoToJson()).toByteArray()
                        )
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }
    val context = LocalContext.current
    Row(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .padding(start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(30.dp)

    ) {
        Column {
            Row{
                Column {
                    Icon(painter = painterResource(R.drawable.baseline_translate_24), contentDescription = stringResource(R.string.SeleccionarIdioma))
                }

                Column (
                ){
                    var expanded by remember { mutableStateOf(false) }
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
        }
        Column {
            Row {
                Column {
                    Icon(
                        painter = painterResource(R.drawable.baseline_color_lens_24),
                        contentDescription = stringResource(R.string.SeleccionaModo)
                    )
                }

                Column(
                ) {
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
                }
            }
        }
        Column {
            Row {
                Icon(painter = painterResource(R.drawable.baseline_download_24), contentDescription = "" )
                Text(text = stringResource(R.string.exportardatos), modifier = Modifier.padding(start = 10.dp).clickable(onClick = { saverLauncher.launch(filename) }))
            }
        }
    }
}


@Preview(widthDp = 640, heightDp = 360, showBackground = true)
@Composable
fun PreviewAjustes (){
    SerieTrackerTheme(content = {
        //AjustesLanscape(modifier = Modifier)
    })
}