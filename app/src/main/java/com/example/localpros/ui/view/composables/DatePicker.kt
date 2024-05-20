package com.example.localpros.ui.view.composables

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material3.ButtonDefaults
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.Instant
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.datepicker.MaterialDatePicker
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.FragmentActivity

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
        colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary)
    ) {
        Text(text = "Seleccionar fecha: $dateString")
    }
}

private fun showMaterialDatePicker(context: Context, selectedDate: LocalDate, onDateChange: (LocalDate) -> Unit) {
    val activity = context.findActivity() ?: return

    val datePicker = MaterialDatePicker.Builder.datePicker()
        .setSelection(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())
        .build()

    datePicker.addOnPositiveButtonClickListener { selection ->
        val newDate = Instant.ofEpochMilli(selection).atZone(ZoneId.systemDefault()).toLocalDate()
        onDateChange(newDate)
    }

    datePicker.show(activity.supportFragmentManager, "DATE_PICKER")
}

private fun Context.findActivity(): FragmentActivity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is FragmentActivity) return context
        context = context.baseContext
    }
    return null
}
