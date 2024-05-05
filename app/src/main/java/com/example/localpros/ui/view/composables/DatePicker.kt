package com.example.localpros.ui.view.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.material3.ButtonDefaults
import java.time.LocalDate
import com.google.android.material.datepicker.MaterialDatePicker
import androidx.compose.ui.platform.LocalContext

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dialogState = remember { mutableStateOf(false) }
    val dateString = remember(selectedDate) { selectedDate.toString() }

    if (dialogState.value) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Seleccionar fecha")
            .setSelection(selectedDate.toEpochDay() * 86400000)
            .build()

        datePicker.addOnPositiveButtonClickListener {

            val newDate = LocalDate.ofEpochDay(it / 86400000)
            onDateChange(newDate)
        }
        datePicker.addOnDismissListener {
            dialogState.value = false
        }
        datePicker.show(context.packageManager, datePicker.toString())
    }

    Button(
        onClick = { dialogState.value = true },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors()
    ) {
        Text("Seleccionar Fecha: $dateString")
    }
}
