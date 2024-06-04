package com.example.localpros.ui.view.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.localpros.data.model.IndicadoresDesempeno

@Composable
fun IndicadoresDesempeno(
    performanceIndicators: IndicadoresDesempeno,
    onIndicatorsChange: ((IndicadoresDesempeno) -> Unit)? = null
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Indicadores de Desempeño", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))

        ProgressBarWithLabel(
            label = "Cumplimiento de Plazos",
            progress = performanceIndicators.cumplimientoPlazos / 100.0,
            isEditable = onIndicatorsChange != null,
            onValueChange = { newValue ->
                onIndicatorsChange?.invoke(performanceIndicators.copy(cumplimientoPlazos = newValue * 100))
            }
        )

        ProgressBarWithLabel(
            label = "Ajuste al Presupuesto",
            progress = performanceIndicators.adherenciaPresupuesto / 100.0,
            isEditable = onIndicatorsChange != null,
            onValueChange = { newValue ->
                onIndicatorsChange?.invoke(performanceIndicators.copy(adherenciaPresupuesto = newValue * 100))
            }
        )

        ProgressBarWithLabel(
            label = "Calidad de Materiales",
            progress = performanceIndicators.calidadMateriales / 100.0,
            isEditable = onIndicatorsChange != null,
            onValueChange = { newValue ->
                onIndicatorsChange?.invoke(performanceIndicators.copy(calidadMateriales = newValue * 100))
            }
        )

        ProgressBarWithLabel(
            label = "Calidad de Procedimientos",
            progress = performanceIndicators.calidadProcedimientos / 100.0,
            isEditable = onIndicatorsChange != null,
            onValueChange = { newValue ->
                onIndicatorsChange?.invoke(performanceIndicators.copy(calidadProcedimientos = newValue * 100))
            }
        )

        ProgressBarWithLabel(
            label = "Cumplimiento de Seguridad",
            progress = performanceIndicators.cumplimientoSeguridad / 100.0,
            isEditable = onIndicatorsChange != null,
            onValueChange = { newValue ->
                onIndicatorsChange?.invoke(performanceIndicators.copy(cumplimientoSeguridad = newValue * 100))
            }
        )

        ProgressBarWithLabel(
            label = "Fiabilidad y Compromiso",
            progress = performanceIndicators.compromisoFiabilidad / 100.0,
            isEditable = onIndicatorsChange != null,
            onValueChange = { newValue ->
                onIndicatorsChange?.invoke(performanceIndicators.copy(compromisoFiabilidad = newValue * 100))
            }
        )

        ProgressBarWithLabel(
            label = "Transparencia y Ética",
            progress = performanceIndicators.transparenciaEticaComunicacion / 100.0,
            isEditable = onIndicatorsChange != null,
            onValueChange = { newValue ->
                onIndicatorsChange?.invoke(performanceIndicators.copy(transparenciaEticaComunicacion = newValue * 100))
            }
        )
    }
}

@Composable
fun ProgressBarWithLabel(
    label: String,
    progress: Double,
    isEditable: Boolean = false,
    onValueChange: ((Double) -> Unit)? = null
) {
    Column {
        Text(label, style = MaterialTheme.typography.bodySmall)
        if (isEditable && onValueChange != null) {
            Slider(
                value = progress.toFloat(),
                onValueChange = { onValueChange(it.toDouble()) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                valueRange = 0f..1f
            )
        } else {
            LinearProgressIndicator(
                progress = { progress.toFloat() },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            )
        }
    }
}
