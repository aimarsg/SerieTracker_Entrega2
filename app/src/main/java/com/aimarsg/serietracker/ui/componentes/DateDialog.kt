package com.aimarsg.serietracker.ui.componentes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.aimarsg.serietracker.R
import com.aimarsg.serietracker.ui.theme.SerieTrackerTheme
import com.aimarsg.serietracker.utils.fromEpochMilliseconds
import kotlinx.datetime.LocalDate


/**
 * Implementation of the default datepicker dialog of Material3
 * A dialog that shows a datepicker
 * @param onDismissRequest: action to be performed on dissmiss
 * @param onDateEntered: action performed when a date is selected an OK button is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onDateEntered: (LocalDate) -> Unit,
) {

    val datePickerState = rememberDatePickerState()
    val confirmEnabled = remember {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }
    DatePickerDialog(
        onDismissRequest = {
                           onDismissRequest()
            //openDialog.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    //openDialog.value = false
                    var fecha = datePickerState.selectedDateMillis?.let {
                        LocalDate.Companion.fromEpochMilliseconds(
                            it
                        )
                    }
                    if (fecha != null) {
                        onDateEntered(fecha)
                    }
                },
                enabled = confirmEnabled.value
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.Cancelar)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    //openDialog.value = false
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(R.string.Cancelar)
                )
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDialog3(){
    SerieTrackerTheme(content = {
        DateDialog(onDismissRequest = {}, onDateEntered = {})
    })
}