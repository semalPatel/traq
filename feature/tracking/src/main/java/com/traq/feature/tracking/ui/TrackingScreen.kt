package com.traq.feature.tracking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.traq.core.maps.api.CameraPosition
import com.traq.core.maps.api.LatLng
import com.traq.core.ui.component.MetricCard
import com.traq.core.ui.component.SpeedGauge
import com.traq.core.ui.util.FormatUtils
import com.traq.feature.tracking.viewmodel.TrackingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingScreen(
    onTripCompleted: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: TrackingViewModel = hiltViewModel(),
    mapContent: @Composable (
        modifier: Modifier,
        cameraPosition: CameraPosition,
        routePoints: List<LatLng>
    ) -> Unit = { _, _, _ -> }
) {
    val state by viewModel.uiState.collectAsState()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var hasSeenTracking by remember { mutableStateOf(false) }

    if (state.trackingState.isTracking) {
        hasSeenTracking = true
    }

    LaunchedEffect(state.trackingState.isTracking, hasSeenTracking) {
        if (hasSeenTracking && !state.trackingState.isTracking) {
            onTripCompleted(state.tripId)
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 280.dp,
        sheetContent = {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SpeedGauge(speedMps = state.trackingState.currentSpeedMps ?: 0f)
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard(label = "Distance", value = FormatUtils.formatDistance(state.trackingState.distanceMeters), modifier = Modifier.weight(1f))
                    MetricCard(label = "Time", value = FormatUtils.formatDuration(state.trackingState.elapsedMs), modifier = Modifier.weight(1f))
                    MetricCard(label = "Points", value = state.trackingState.pointsRecorded.toString(), modifier = Modifier.weight(1f))
                }
                Spacer(Modifier.height(8.dp))
                val modeText = state.trackingState.currentMode?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "Detecting..."
                val intervalText = "%.1fs".format(state.trackingState.samplingIntervalMs / 1000.0)
                Text(
                    "$modeText · GPS interval: $intervalText",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (state.trackingState.isPaused) {
                        Button(onClick = { viewModel.onResume() }, modifier = Modifier.weight(1f)) {
                            Text("Resume")
                        }
                    } else {
                        OutlinedButton(onClick = { viewModel.onPause() }, modifier = Modifier.weight(1f)) {
                            Text("Pause")
                        }
                    }
                    Button(
                        onClick = { viewModel.onRequestStop() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Stop")
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (state.isMapReady) {
                mapContent(
                    Modifier.fillMaxSize(),
                    state.cameraPosition,
                    state.routePoints
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(8.dp))
                        Text("Getting your location...", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }

    if (state.showStopConfirmation) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissStop() },
            title = { Text("Stop Trip?") },
            text = { Text("Are you sure you want to end this trip?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onConfirmStop()
                }) { Text("Stop") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDismissStop() }) { Text("Cancel") }
            }
        )
    }
}
