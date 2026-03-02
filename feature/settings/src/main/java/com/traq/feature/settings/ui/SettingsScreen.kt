package com.traq.feature.settings.ui

import androidx.compose.foundation.clickable
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
import com.traq.core.common.model.MapRendererType
import com.traq.core.common.model.TrackingAccuracy
import com.traq.feature.settings.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var showMapRendererDialog by remember { mutableStateOf(false) }
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
                headlineContent = { Text("Map Renderer") },
                supportingContent = { Text(state.mapRenderer.displayName()) },
                modifier = Modifier.clickable { showMapRendererDialog = true }
            )
            Text(
                "Changing map renderer requires app restart",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

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

            // About section
            Text("About", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            ListItem(
                headlineContent = { Text("Traq v1.0.0") },
                supportingContent = { Text("AI-powered GPS tracker") }
            )
        }
    }

    // Map Renderer Dialog
    if (showMapRendererDialog) {
        SingleChoiceDialog(
            title = "Map Renderer",
            options = MapRendererType.entries.map { it to it.displayName() },
            selected = state.mapRenderer,
            onSelect = { viewModel.setMapRenderer(it); showMapRendererDialog = false },
            onDismiss = { showMapRendererDialog = false }
        )
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

private fun MapRendererType.displayName(): String = when (this) {
    MapRendererType.GOOGLE -> "Google Maps"
    MapRendererType.MAPLIBRE -> "MapLibre (offline)"
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
