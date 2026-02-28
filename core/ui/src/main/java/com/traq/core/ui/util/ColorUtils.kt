package com.traq.core.ui.util

import androidx.compose.ui.graphics.Color
import com.traq.core.common.model.TransportMode
import com.traq.core.ui.theme.ModeCycling
import com.traq.core.ui.theme.ModeDriving
import com.traq.core.ui.theme.ModeRunning
import com.traq.core.ui.theme.ModeStationary
import com.traq.core.ui.theme.ModeTransit
import com.traq.core.ui.theme.ModeWalking

object ColorUtils {
    fun transportModeColor(mode: TransportMode): Color = when (mode) {
        TransportMode.WALKING -> ModeWalking
        TransportMode.RUNNING -> ModeRunning
        TransportMode.CYCLING -> ModeCycling
        TransportMode.DRIVING -> ModeDriving
        TransportMode.TRANSIT -> ModeTransit
        TransportMode.STATIONARY -> ModeStationary
    }
}
