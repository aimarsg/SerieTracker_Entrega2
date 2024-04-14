package com.aimarsg.serietracker.ui.pantallas

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.dp
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.model.webclient.UserExistsException
import com.aimarsg.serietracker.ui.SeriesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.ConnectException


/**
 * Screen to register a new user
 * @param onRegistered: callback to go to the login screen
 * @param inicioSesion: callback to go to the login screen
 * @param viewModel: viewmodel to handle the register request
 */
@Composable
fun RegisterScreen(
    onRegistered: () -> Unit,
    inicioSesion: () -> Unit,
    viewModel: SeriesViewModel
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()

    var registrado by remember { mutableStateOf(false) }
    var mostrarError by remember { mutableStateOf(false) }
    var contrasenasNoCoinciden by remember { mutableStateOf(false) }
    var mostrarErrorConexion by remember { mutableStateOf(false) }

    val onRegister: () -> Unit = {
        coroutineScope.launch(Dispatchers.IO){
            try {
                viewModel.register(username, password)
                registrado = true
                mostrarError = false
                contrasenasNoCoinciden = false
            } catch (e: UserExistsException) {
                mostrarError = true
                registrado = false
                contrasenasNoCoinciden = false
            }catch (e: ConnectException) {
                mostrarError = true
                registrado = false
                mostrarErrorConexion = true
            }
        }
    }
    if (registrado) {
        registrado = false
        // Show success message
        Toast.makeText(
            context,
            R.string.Registroexitoso,
            Toast.LENGTH_SHORT
        ).show()
        onRegistered()
    }
    if (mostrarError){
        // Show error message
        Toast.makeText(
            context,
            R.string.usuarioyaexiste,
            Toast.LENGTH_SHORT
        ).show()
    }
    if (mostrarErrorConexion){
        Toast.makeText(
            context,
            R.string.no_internet,
            Toast.LENGTH_SHORT
        ).show()
        mostrarErrorConexion = false
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
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
                .fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.Registro), style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(
                    value = username,
                    isError = mostrarError,
                    onValueChange = { username = it },
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
                    onValueChange = { password = it },
                    isError = contrasenasNoCoinciden,
                    label = { Text(stringResource(R.string.Contraseña)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Password),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(
                            FocusDirection.Down
                        )
                    })
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    isError = contrasenasNoCoinciden,
                    label = { Text(stringResource(R.string.confirmarcontraseña)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                    })
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    enabled = username.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank(),
                    onClick = {
                        if (validateFields(username, password, confirmPassword)) {
                            mostrarError = false
                            contrasenasNoCoinciden = false
                            onRegister()
                        } else {
                            // Show error message
                            Toast.makeText(
                                context,
                                R.string.contraseñasnocoinciden,
                                Toast.LENGTH_SHORT
                            ).show()
                            contrasenasNoCoinciden = true
                        }

                    }) {
                    Text(text = stringResource(R.string.Registrarse))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = stringResource(R.string.yatienescuenta))
                OutlinedButton(onClick = { inicioSesion() }) {
                    Text(text = stringResource(R.string.IniciarSesion))
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


/**
 * Method to validate the fields of the register form
 * @param username: user name
 * @param password: user password
 * @param confirmPassword: user password confirmation
 * @return true if the fields are valid, false otherwise
 */
fun validateFields(
    username: String,
    password: String,
    confirmPassword: String,
): Boolean {
    return username.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank() && password == confirmPassword
}
