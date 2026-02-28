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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.traq.core.common.model.ExportFormat
import com.traq.core.ui.component.LoadingIndicator
import com.traq.core.ui.component.MetricCard
import com.traq.core.ui.util.FormatUtils
import com.traq.feature.tripdetail.viewmodel.TripDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    onBack: () -> Unit,
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
                .padding(16.dp)
        ) {
            Text(trip.name ?: "Trip", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(4.dp))
            Text(
                trip.startTime.toString().take(16).replace("T", " "),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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

            Spacer(Modifier.height(24.dp))
            Button(onClick = { viewModel.toggleExportSheet() }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Share, contentDescription = null)
                Text("  Export Trip")
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
    ExportFormat.TRAQ -> ".traq \u2014 Full trip data (recommended)"
    ExportFormat.GPX -> ".gpx \u2014 Universal GPS format"
    ExportFormat.GEOJSON -> ".geojson \u2014 Web/GIS format"
    ExportFormat.KML -> ".kml \u2014 Google Earth"
}
