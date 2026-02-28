package com.traq.core.ui.component

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.traq.core.common.model.TripStatus

@Composable
fun TripStatusBadge(
    status: TripStatus,
    modifier: Modifier = Modifier
) {
    val (label, containerColor) = when (status) {
        TripStatus.ACTIVE -> "Active" to MaterialTheme.colorScheme.primary
        TripStatus.PAUSED -> "Paused" to MaterialTheme.colorScheme.secondary
        TripStatus.COMPLETED -> "Completed" to MaterialTheme.colorScheme.surfaceVariant
    }
    AssistChip(
        onClick = {},
        label = { Text(label, style = MaterialTheme.typography.labelMedium) },
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(containerColor = containerColor, labelColor = Color.White)
    )
}
