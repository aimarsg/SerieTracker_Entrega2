package com.aimarsg.serietracker.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.aimarsg.serietracker.R

/**
 * Dialog that is shown to confirm the deletion of the elements from the main screens
 */
@Composable
fun DialogoBorrar(
    onDismiss: () -> Unit,
    onDelete: () -> Unit
){
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp)
        ) {
            Text(text = stringResource(R.string.confirmarborrar), modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                //horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(onClick = {
                    onDismiss()
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.Cancelar)
                    )
                }

                Button(onClick = {
                    onDelete()
                }) {
                    Text(text = "")
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.Borrar)
                    )
                }
            }
        }
    }
}
