package com.traq.feature.tripdetail.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.traq.core.common.model.ExportFormat
import com.traq.core.maps.api.CameraPosition
import com.traq.core.maps.api.MapRenderer
import com.traq.core.maps.api.RenderMapView
import com.traq.core.ui.component.ChartDataPoint
import com.traq.core.ui.component.ElevationChart
import com.traq.core.ui.component.LoadingIndicator
import com.traq.core.ui.component.MetricCard
import com.traq.core.ui.component.ModeSegment
import com.traq.core.ui.component.SpeedChart
import com.traq.core.ui.component.TransportModeIcon
import com.traq.core.ui.component.TransportModeTimeline
import com.traq.core.ui.util.FormatUtils
import com.traq.feature.tripdetail.viewmodel.TripDetailViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM d yyyy · h:mm a")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    onBack: () -> Unit,
    mapRenderer: MapRenderer? = null,
    viewModel: TripDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.shareIntent.collect { intent ->
            context.startActivity(Intent.createChooser(intent, "Share Trip"))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.trip?.name ?: "Trip Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleExportSheet() }) {
                        Icon(Icons.Default.Share, contentDescription = "Export")
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            LoadingIndicator()
            return@Scaffold
        }

        val trip = state.trip ?: return@Scaffold

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Route map
            if (mapRenderer != null && state.mapBounds != null) {
                val bounds = state.mapBounds!!
                val latSpan = bounds.northEast.latitude - bounds.southWest.latitude
                val lngSpan = bounds.northEast.longitude - bounds.southWest.longitude
                val maxSpan = maxOf(latSpan, lngSpan).coerceAtLeast(0.001)
                val zoom = (Math.log(360.0 / maxSpan) / Math.log(2.0))
                    .toFloat()
                    .coerceIn(2f, 18f) - 1f

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(16.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    RenderMapView(
                        renderer = mapRenderer,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.large),
                        cameraPosition = CameraPosition(
                            bounds.center.latitude,
                            bounds.center.longitude,
                            zoom, 0f
                        ),
                        polylines = state.routePolylines,
                        markers = emptyList(),
                        onCameraMove = {},
                        isInteractive = false
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(trip.name ?: "Trip", style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(4.dp))
                Text(
                    trip.startTime.atZone(ZoneId.systemDefault()).format(dateFormatter),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                trip.dominantMode?.let { mode ->
                    Spacer(Modifier.height(8.dp))
                    Row {
                        TransportModeIcon(mode = mode, size = 20.dp)
                        Text(
                            "  ${mode.name.lowercase().replaceFirstChar { it.uppercase() }}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard(label = "Distance", value = FormatUtils.formatDistance(trip.metrics.totalDistanceMeters), modifier = Modifier.weight(1f))
                    MetricCard(label = "Duration", value = FormatUtils.formatDuration(trip.metrics.totalDurationMs), modifier = Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard(label = "Avg Speed", value = FormatUtils.formatSpeed(trip.metrics.avgSpeedMps.toFloat()), modifier = Modifier.weight(1f))
                    MetricCard(label = "Max Speed", value = FormatUtils.formatSpeed(trip.metrics.maxSpeedMps.toFloat()), modifier = Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard(label = "Ascent", value = FormatUtils.formatElevation(trip.metrics.totalAscentMeters), modifier = Modifier.weight(1f))
                    MetricCard(label = "Descent", value = FormatUtils.formatElevation(trip.metrics.totalDescentMeters), modifier = Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
                MetricCard(label = "Track Points", value = trip.metrics.pointCount.toString(), modifier = Modifier.fillMaxWidth())

                if (state.trackPoints.size >= 2) {
                    Spacer(Modifier.height(16.dp))

                    val elevationPoints = state.trackPoints.filter { it.altitude != null }
                    if (elevationPoints.size >= 2) {
                        val minAlt = elevationPoints.minOf { it.altitude!! }
                        val maxAlt = elevationPoints.maxOf { it.altitude!! }
                        val altRange = (maxAlt - minAlt).coerceAtLeast(1.0)

                        ElevationChart(
                            points = elevationPoints.mapIndexed { index, pt ->
                                ChartDataPoint(
                                    x = index.toFloat() / (elevationPoints.size - 1).coerceAtLeast(1),
                                    y = ((pt.altitude!! - minAlt) / altRange).toFloat()
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    val speedPoints = state.trackPoints.filter { it.speed != null }
                    if (speedPoints.size >= 2) {
                        val maxSpeed = speedPoints.maxOf { it.speed!! }.coerceAtLeast(1f)

                        SpeedChart(
                            points = speedPoints.mapIndexed { index, pt ->
                                ChartDataPoint(
                                    x = index.toFloat() / (speedPoints.size - 1).coerceAtLeast(1),
                                    y = (pt.speed!! / maxSpeed)
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    val modePoints = state.trackPoints.filter { it.transportMode != null }
                    if (modePoints.size >= 2) {
                        val segments = mutableListOf<ModeSegment>()
                        var currentMode = modePoints.first().transportMode!!
                        var segStart = 0
                        modePoints.forEachIndexed { index, pt ->
                            val mode = pt.transportMode!!
                            if (mode != currentMode || index == modePoints.size - 1) {
                                segments.add(
                                    ModeSegment(
                                        startFraction = segStart.toFloat() / (modePoints.size - 1).coerceAtLeast(1),
                                        endFraction = index.toFloat() / (modePoints.size - 1).coerceAtLeast(1),
                                        mode = currentMode
                                    )
                                )
                                currentMode = mode
                                segStart = index
                            }
                        }
                        TransportModeTimeline(
                            segments = segments,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }

                Spacer(Modifier.height(24.dp))
                Button(onClick = { viewModel.toggleExportSheet() }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Text("  Export Trip")
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (state.showExportSheet) {
        AlertDialog(
            onDismissRequest = { viewModel.toggleExportSheet() },
            title = { Text("Export As") },
            text = {
                Column {
                    viewModel.getSupportedFormats().forEach { format ->
                        Text(
                            text = format.displayLabel(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.exportTrip(format) }
                                .padding(vertical = 12.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.toggleExportSheet() }) { Text("Cancel") }
            }
        )
    }
}

private fun ExportFormat.displayLabel(): String = when (this) {
    ExportFormat.TRAQ -> ".traq — Full trip data (recommended)"
    ExportFormat.GPX -> ".gpx — Universal GPS format"
    ExportFormat.GEOJSON -> ".geojson — Web/GIS format"
    ExportFormat.KML -> ".kml — Google Earth"
}
