package com.aimarsg.serietracker.ui.componentes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.ui.theme.SerieTrackerTheme

/**
 * Dialog that shows the app instructions
 * @param onConfirmation
 * @param onDismissRequest
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(imageVector = Icons.Default.Info, contentDescription = "Example Icon")
        },
        title = {
            Text(text = stringResource(id = R.string.comoFunciona))
        },
        text = {
            Text(text = stringResource(R.string.INSintro) +
                    "\n\n" + stringResource(R.string.INSSiguiendo) +
                    "\n\n" + stringResource(R.string.INSPendiente))

        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(stringResource(R.string.Cancelar))
            }
        }
    )
}

@Preview
@Composable
fun dialogopreview(){
    SerieTrackerTheme {
        HelpDialog(onDismissRequest = {}, onConfirmation = {})
    }
}