package com.traq.core.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.traq.core.common.model.TransportMode
import com.traq.core.ui.theme.ModeCycling
import com.traq.core.ui.theme.ModeDriving
import com.traq.core.ui.theme.ModeRunning
import com.traq.core.ui.theme.ModeStationary
import com.traq.core.ui.theme.ModeTransit
import com.traq.core.ui.theme.ModeWalking

data class ModeSegment(
    val startFraction: Float,
    val endFraction: Float,
    val mode: TransportMode
)

@Composable
fun TransportModeTimeline(
    segments: List<ModeSegment>,
    modifier: Modifier = Modifier
) {
    if (segments.isEmpty()) return

    Column(modifier = modifier) {
        Text(
            text = "Transport Modes",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
        ) {
            val cornerRadius = CornerRadius(4.dp.toPx())
            segments.forEach { segment ->
                val x = segment.startFraction * size.width
                val w = (segment.endFraction - segment.startFraction) * size.width
                drawRoundRect(
                    color = segment.mode.toColor(),
                    topLeft = Offset(x, 0f),
                    size = Size(w.coerceAtLeast(2f), size.height),
                    cornerRadius = cornerRadius
                )
            }
        }
    }
}

private fun TransportMode.toColor() = when (this) {
    TransportMode.WALKING -> ModeWalking
    TransportMode.RUNNING -> ModeRunning
    TransportMode.CYCLING -> ModeCycling
    TransportMode.DRIVING -> ModeDriving
    TransportMode.TRANSIT -> ModeTransit
    TransportMode.STATIONARY -> ModeStationary
}
