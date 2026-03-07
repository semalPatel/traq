package com.traq.feature.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.traq.core.common.model.ExportFormat
import com.traq.core.common.model.TrackingAccuracy
import com.traq.feature.settings.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var showAccuracyDialog by remember { mutableStateOf(false) }
    var showExportFormatDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
        ) {
            // Tracking section
            Text("Tracking", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            ListItem(
                headlineContent = { Text("Accuracy") },
                supportingContent = { Text(state.trackingAccuracy.displayName()) },
                modifier = Modifier.clickable { showAccuracyDialog = true }
            )

            // AI section
            Text("AI", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            ListItem(
                headlineContent = { Text("AI Features") },
                supportingContent = { Text("Adaptive sampling, mode detection, GPS smoothing") },
                trailingContent = {
                    Switch(checked = state.aiEnabled, onCheckedChange = { viewModel.setAiEnabled(it) })
                }
            )

            // Maps section
            Text("Maps", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            ListItem(
                headlineContent = { Text("Map Provider") },
                supportingContent = { Text("MapLibre \u00b7 OpenStreetMap (free, no API key)") }
            )
            Text(
                "Powered by OpenFreeMap",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            ListItem(
                headlineContent = { Text("Offline Maps") },
                supportingContent = {
                    Text(
                        if (state.isOfflineMapsSupported) {
                            "Download the area around your current location for remote trips."
                        } else {
                            "Offline maps are not supported by the current renderer."
                        }
                    )
                }
            )
            if (state.isOfflineMapsSupported) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.downloadOfflineRegionAroundLastLocation() },
                        enabled = !state.isDownloadingOfflineRegion
                    ) {
                        Text(if (state.isDownloadingOfflineRegion) "Downloading..." else "Download Current Area")
                    }
                }
                ListItem(
                    headlineContent = { Text("Offline Map Storage") },
                    supportingContent = { Text(state.offlineMapsSizeMb) }
                )
                state.offlineDownloadPercent?.let { progress ->
                    ListItem(
                        headlineContent = { Text("Download Progress") },
                        supportingContent = { Text("$progress%") }
                    )
                }
                state.offlineStatusMessage?.let { message ->
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
                if (state.offlineRegions.isEmpty()) {
                    Text(
                        "No offline map regions downloaded yet.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                } else {
                    state.offlineRegions.forEach { region ->
                        ListItem(
                            headlineContent = { Text(region.name) },
                            supportingContent = { Text("${formatSize(region.sizeBytes)} · saved ${formatTimestamp(region.createdAt)}") },
                            trailingContent = {
                                TextButton(onClick = { viewModel.deleteOfflineRegion(region.id) }) {
                                    Text("Delete")
                                }
                            }
                        )
                    }
                }
            }

            // Battery section
            Text("Battery", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            ListItem(
                headlineContent = { Text("Battery Optimization") },
                supportingContent = { Text(if (state.isBatteryOptimized) "Disabled (recommended)" else "Enabled — may stop tracking") },
                modifier = Modifier.clickable { viewModel.requestBatteryOptimization() }
            )

            // Export section
            Text("Export", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            ListItem(
                headlineContent = { Text("Default Format") },
                supportingContent = { Text(state.defaultExportFormat.displayName()) },
                modifier = Modifier.clickable { showExportFormatDialog = true }
            )
            ListItem(
                headlineContent = { Text("Auto-export on trip end") },
                trailingContent = {
                    Switch(checked = state.autoExportEnabled, onCheckedChange = { viewModel.setAutoExport(it) })
                }
            )

            // Storage section
            Text("Storage", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            ListItem(
                headlineContent = { Text("Trip Database") },
                supportingContent = { Text(state.databaseSizeMb) }
            )
            ListItem(
                headlineContent = { Text("Cache") },
                supportingContent = { Text(state.cacheSizeMb) },
                modifier = Modifier.clickable { viewModel.clearCache() }
            )

            // About section
            Text("About", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            ListItem(
                headlineContent = { Text("Traq v1.0.0") },
                supportingContent = { Text("AI-powered GPS tracker") }
            )
        }
    }

    // Accuracy Dialog
    if (showAccuracyDialog) {
        SingleChoiceDialog(
            title = "Tracking Accuracy",
            options = TrackingAccuracy.entries.map { it to it.displayName() },
            selected = state.trackingAccuracy,
            onSelect = { viewModel.setTrackingAccuracy(it); showAccuracyDialog = false },
            onDismiss = { showAccuracyDialog = false }
        )
    }

    // Export Format Dialog
    if (showExportFormatDialog) {
        SingleChoiceDialog(
            title = "Default Export Format",
            options = ExportFormat.entries.map { it to it.displayName() },
            selected = state.defaultExportFormat,
            onSelect = { viewModel.setExportFormat(it); showExportFormatDialog = false },
            onDismiss = { showExportFormatDialog = false }
        )
    }
}

@Composable
private fun <T> SingleChoiceDialog(
    title: String,
    options: List<Pair<T, String>>,
    selected: T,
    onSelect: (T) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                options.forEach { (value, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(value) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = value == selected, onClick = { onSelect(value) })
                        Text(label, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

private fun TrackingAccuracy.displayName(): String = when (this) {
    TrackingAccuracy.HIGH -> "High (best accuracy, more battery)"
    TrackingAccuracy.BALANCED -> "Balanced"
    TrackingAccuracy.LOW -> "Low (saves battery)"
}

private fun ExportFormat.displayName(): String = when (this) {
    ExportFormat.TRAQ -> ".traq (full data)"
    ExportFormat.GPX -> "GPX (universal)"
    ExportFormat.GEOJSON -> "GeoJSON (web)"
    ExportFormat.KML -> "KML (Google Earth)"
}

private fun formatSize(bytes: Long): String = when {
    bytes < 1024 -> "$bytes B"
    bytes < 1024 * 1024 -> "%.1f KB".format(bytes / 1024.0)
    else -> "%.1f MB".format(bytes / (1024.0 * 1024.0))
}

private fun formatTimestamp(timestampMs: Long): String {
    return java.time.Instant.ofEpochMilli(timestampMs)
        .atZone(java.time.ZoneId.systemDefault())
        .toLocalDate()
        .toString()
}
