package com.aimarsg.serietracker.ui.pantallas

import android.R.attr.clickable
import android.R.attr.duration
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.model.webclient.AuthenticationException
import com.aimarsg.serietracker.ui.SeriesViewModel
import com.aimarsg.serietracker.ui.theme.SerieTrackerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Screen to login
 * @param onLogedIn: callback to go to the main screen
 * @param register: callback to go to the register screen
 * @param viewModel: viewmodel to handle the login request
 */
@Composable
fun LoginScreen(
    onLogedIn: () -> Unit,
    register: () -> Unit,
    viewModel: SeriesViewModel
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()
    var sesionIniciada by remember { mutableStateOf(false) }
    var mostrarError by remember { mutableStateOf(false) }

    val onLogin: () -> Unit = {
        coroutineScope.launch(Dispatchers.IO){
            try {
                viewModel.authenticate(username, password)
                sesionIniciada = true
                mostrarError = false
            } catch (e: AuthenticationException) {
                mostrarError = true
                sesionIniciada = false
            }
        }
    }
    if (sesionIniciada) {
        onLogedIn()
    }
    if (mostrarError){
        Toast.makeText(
            context,
            R.string.error_login,
            Toast.LENGTH_SHORT
        ).show()
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono_s),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.FillBounds

                //.background(MaterialTheme.colors.primary)
            )
        }
        Card(
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    stringResource(R.string.IniciarSesion),
                    style = MaterialTheme.typography.titleLarge
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    isError = mostrarError,
                    label = { Text(stringResource(R.string.usuario)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = {
                        // Move focus to next field
                        focusManager.moveFocus(FocusDirection.Down)
                    })
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    isError = mostrarError,
                    onValueChange = { password = it },
                    label = { Text(stringResource(R.string.Contraseña)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    enabled = username.isNotEmpty() && password.isNotEmpty(),
                    onClick = {
                        mostrarError = false
                        onLogin()
                    }) {
                    Text(text = stringResource(R.string.IniciarSesion))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = stringResource(R.string.notienescuenta))
                OutlinedButton(onClick = { register() }) {
                    Text(text = stringResource(R.string.Registro))
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}