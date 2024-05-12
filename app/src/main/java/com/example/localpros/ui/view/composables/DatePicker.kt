package com.example.localpros.ui.view.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material3.ButtonDefaults
import java.time.LocalDate
import java.time.ZoneId
import java.time.Instant
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.datepicker.MaterialDatePicker
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.FragmentActivity

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dateString = remember(selectedDate) { selectedDate.toString() }
    var showDatePicker by remember { mutableStateOf(false) }

    // Prepare the launcher for receiving the date picker result
    val datePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val selection = (result.data?.extras?.get("com.google.android.material.datepicker.extra.SELECTION") as? Long)
            if (selection != null) {
                val newDate = Instant.ofEpochMilli(selection).atZone(ZoneId.systemDefault()).toLocalDate()
                onDateChange(newDate)
            }
        }
    }

    LaunchedEffect(showDatePicker) {
        if (showDatePicker) {
            showMaterialDatePicker(context, selectedDate, onDateChange)
            showDatePicker = false
        }
    }

    Button(
        onClick = { showDatePicker = true },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors()
    ) {
        Text("Seleccionar Fecha: $dateString")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun showMaterialDatePicker(
    context: Context,
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Seleccionar fecha")
        .setSelection(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
        .build()

    datePicker.addOnPositiveButtonClickListener { selection ->
        val newDate = Instant.ofEpochMilli(selection).atZone(ZoneId.systemDefault()).toLocalDate()
        onDateChange(newDate)
    }

    val fragmentManager = context.findFragmentActivity()?.supportFragmentManager
    if (fragmentManager != null) {
        datePicker.show(fragmentManager, "date_picker")
    }
}
fun Context.findFragmentActivity(): FragmentActivity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is FragmentActivity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}