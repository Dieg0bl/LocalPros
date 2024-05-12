package com.example.localpros.ui.view.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.localpros.data.model.IndicadoresDesempeno

@Composable
fun IndicadoresDesempenoSection(indicadoresDesempeno: IndicadoresDesempeno) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Indicadores de Desempeño", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))

        ProgressBarWithLabel(
            label = "Cumplimiento de Prazos",
            progress = indicadoresDesempeno.cumplimientoPrazos / 100
        )

        ProgressBarWithLabel(
            label = "Ajuste al Presupuesto",
            progress = indicadoresDesempeno.ajustePresupuesto / 100
        )

        ProgressBarWithLabel(
            label = "Calidad de Materiales",
            progress = indicadoresDesempeno.calidadMateriales / 100
        )

        ProgressBarWithLabel(
            label = "Calidad de Procedimientos",
            progress = indicadoresDesempeno.calidadProcedimientos / 100
        )

        ProgressBarWithLabel(
            label = "Cumplimiento de Seguridad",
            progress = indicadoresDesempeno.cumplimientoSeguridad / 100
        )

        ProgressBarWithLabel(
            label = "Fiabilidad y Compromiso",
            progress = indicadoresDesempeno.fiabilidadCompromiso / 100
        )

        ProgressBarWithLabel(
            label = "Transparencia, Ética y Comunicación",
            progress = indicadoresDesempeno.transparenciaEticaComunicacion / 100
        )
    }
}

@Composable
fun ProgressBarWithLabel(label: String, progress: Double) {
    Column {
        Text(label, style = MaterialTheme.typography.bodySmall)
        LinearProgressIndicator(
            progress = progress.toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}
