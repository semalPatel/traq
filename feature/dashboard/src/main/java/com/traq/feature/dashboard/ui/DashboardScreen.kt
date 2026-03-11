package com.traq.feature.dashboard.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.traq.core.ui.component.LoadingIndicator
import com.traq.core.ui.component.MetricCard
import com.traq.core.ui.component.TransportModeIcon
import com.traq.core.ui.util.FormatUtils
import com.traq.feature.dashboard.model.DashboardTimeframe
import com.traq.feature.dashboard.viewmodel.DashboardViewModel
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onStartTrip: (String) -> Unit,
    onTripClick: (String) -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var startTripIfReady: () -> Unit = {}

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (Build.VERSION.SDK_INT < 33 || granted) {
            startTripIfReady()
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("Notification permission is required for reliable foreground tracking")
            }
        }
    }

    val bgLocationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (Build.VERSION.SDK_INT < 29 || granted) {
            startTripIfReady()
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("Background location is required so tracking keeps working with the screen off")
            }
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        if (fineGranted) {
            startTripIfReady()
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("Location permission is required to track trips")
            }
        }
    }

    startTripIfReady = {
        val readiness = viewModel.getTrackingReadiness()
        when {
            !readiness.hasForegroundLocation -> {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }

            !readiness.hasBackgroundLocation && Build.VERSION.SDK_INT >= 29 -> {
                bgLocationPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }

            !readiness.hasNotifications && Build.VERSION.SDK_INT >= 33 -> {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }

            else -> {
                val tripId = viewModel.startNewTrip()
                onStartTrip(tripId)
                if (!readiness.isBatteryOptimizationDisabled) {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Battery optimization is still enabled. Tracking may stop in the background."
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Traq", style = MaterialTheme.typography.headlineMedium) },
                actions = {
                    IconButton(onClick = onHistoryClick) {
                        Icon(Icons.Default.History, contentDescription = "History")
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            val hasActiveTrip = state.activeTripState != null
            FloatingActionButton(
                onClick = {
                    if (hasActiveTrip) {
                        state.activeTripState?.tripId?.let { onStartTrip(it) }
                    } else {
                        startTripIfReady()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    if (hasActiveTrip) Icons.Default.Navigation else Icons.Default.Add,
                    contentDescription = if (hasActiveTrip) "View Trip" else "Start Trip"
                )
            }
        }
    ) { padding ->
        if (state.isLoading) {
            LoadingIndicator()
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            state.activeTripState?.let { trackingState ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable {
                            trackingState.tripId?.let { onStartTrip(it) }
                        },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Active Trip", style = MaterialTheme.typography.titleMedium)
                                trackingState.currentMode?.let { mode ->
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        TransportModeIcon(mode = mode, size = 18.dp)
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            mode.name.lowercase().replaceFirstChar { it.uppercase() },
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "${FormatUtils.formatDistance(trackingState.distanceMeters)} · ${FormatUtils.formatDuration(trackingState.elapsedMs)}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            trackingState.currentSpeedMps?.let { speed ->
                                Text(
                                    FormatUtils.formatSpeed(speed),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Overview", style = MaterialTheme.typography.titleMedium)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DashboardTimeframe.entries.forEach { timeframe ->
                            FilterChip(
                                selected = state.selectedTimeframe == timeframe,
                                onClick = { viewModel.updateTimeframe(timeframe) },
                                label = { Text(timeframe.label) }
                            )
                        }
                    }
                }
            }

            item {
                SummaryCard(
                    timeframe = state.selectedTimeframe,
                    tripCount = state.filteredTripCount,
                    distance = state.filteredDistance,
                    duration = state.filteredDuration,
                    latestTripLabel = state.latestCompletedTrip?.let(::formatTripDate)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        label = "Distance",
                        value = FormatUtils.formatDistance(state.filteredDistance),
                        icon = Icons.Default.Route,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        label = "Time",
                        value = FormatUtils.formatDuration(state.filteredDuration),
                        icon = Icons.Default.AccessTime,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        label = "Trips",
                        value = state.filteredTripCount.toString(),
                        icon = Icons.Default.Explore,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        label = "Avg Trip",
                        value = if (state.filteredTripCount == 0) "0 km" else FormatUtils.formatDistance(state.averageDistanceMeters),
                        icon = Icons.Default.Straighten,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        label = "Longest",
                        value = if (state.filteredTripCount == 0) "0 km" else FormatUtils.formatDistance(state.longestTripDistanceMeters),
                        icon = Icons.Default.Navigation,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        label = "Ascent",
                        value = if (state.filteredTripCount == 0) "0 m" else FormatUtils.formatElevation(state.totalAscentMeters),
                        icon = Icons.Default.Terrain,
                        modifier = Modifier.weight(1f)
                    )
                    FavoriteModeCard(
                        modeLabel = state.favoriteMode?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "None yet",
                        mode = state.favoriteMode,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            if (state.filteredTripCount == 0) {
                item {
                    EmptyTimeframeCard(onHistoryClick = onHistoryClick)
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun SummaryCard(
    timeframe: DashboardTimeframe,
    tripCount: Int,
    distance: Double,
    duration: Long,
    latestTripLabel: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Your ${timeframe.label} snapshot",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
            )
            Text(
                if (tripCount == 0) "No completed trips yet" else FormatUtils.formatDistance(distance),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                if (tripCount == 0) {
                    "Start a trip to build your stats for this time window."
                } else {
                    "$tripCount trips logged in ${FormatUtils.formatDuration(duration)}"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
            )
            latestTripLabel?.let {
                Text(
                    "Last completed trip: $it",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.72f)
                )
            }
        }
    }
}

@Composable
private fun FavoriteModeCard(
    modeLabel: String,
    mode: com.traq.core.common.model.TransportMode?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (mode != null) {
                TransportModeIcon(mode = mode, size = 22.dp)
            } else {
                Icon(
                    imageVector = Icons.Default.Explore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = modeLabel,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Favorite Mode",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyTimeframeCard(onHistoryClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Nothing in this range yet", style = MaterialTheme.typography.titleMedium)
            Text(
                "Try a broader timeframe or open your full history to revisit older adventures.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Open History",
                modifier = Modifier.clickable(onClick = onHistoryClick),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun formatTripDate(trip: com.traq.core.data.model.Trip): String {
    val formatter = DateTimeFormatter.ofPattern("MMM d").withZone(ZoneId.systemDefault())
    return formatter.format(trip.startTime)
}
