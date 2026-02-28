package com.traq.core.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.traq.core.common.model.TransportMode
import com.traq.core.ui.theme.ModeCycling
import com.traq.core.ui.theme.ModeDriving
import com.traq.core.ui.theme.ModeRunning
import com.traq.core.ui.theme.ModeStationary
import com.traq.core.ui.theme.ModeTransit
import com.traq.core.ui.theme.ModeWalking

@Composable
fun TransportModeIcon(
    mode: TransportMode,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    val (icon, color) = when (mode) {
        TransportMode.WALKING -> Icons.Default.DirectionsWalk to ModeWalking
        TransportMode.RUNNING -> Icons.Default.DirectionsRun to ModeRunning
        TransportMode.CYCLING -> Icons.Default.DirectionsBike to ModeCycling
        TransportMode.DRIVING -> Icons.Default.DirectionsCar to ModeDriving
        TransportMode.TRANSIT -> Icons.Default.DirectionsBus to ModeTransit
        TransportMode.STATIONARY -> Icons.Default.LocationOn to ModeStationary
    }
    Icon(icon, contentDescription = mode.name, tint = color, modifier = modifier.size(size))
}
