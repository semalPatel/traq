package com.traq.feature.dashboard.ui

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.traq.core.data.model.Trip
import com.traq.core.ui.component.LoadingIndicator
import com.traq.core.ui.component.MetricCard
import com.traq.core.ui.util.FormatUtils
import com.traq.feature.dashboard.viewmodel.DashboardViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        if (fineGranted) {
            val tripId = viewModel.startNewTrip()
            onStartTrip(tripId)
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("Location permission is required to track trips")
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
                    } else if (viewModel.hasLocationPermission()) {
                        val tripId = viewModel.startNewTrip()
                        onStartTrip(tripId)
                    } else {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
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
                            trackingState.tripId?.let { onTripClick(it) }
                        },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Active Trip", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "${FormatUtils.formatDistance(trackingState.distanceMeters)} · ${FormatUtils.formatDuration(trackingState.elapsedMs)}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }

            item {
                Text("This Week", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard(label = "Distance", value = FormatUtils.formatDistance(state.weeklyDistance), modifier = Modifier.weight(1f))
                    MetricCard(label = "Time", value = FormatUtils.formatDuration(state.weeklyDuration), modifier = Modifier.weight(1f))
                    MetricCard(label = "Trips", value = state.weeklyTripCount.toString(), modifier = Modifier.weight(1f))
                }
            }

            if (state.recentTrips.isNotEmpty()) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Text("Recent Trips", style = MaterialTheme.typography.titleMedium)
                }
                items(state.recentTrips, key = { it.id }) { trip ->
                    TripCard(trip = trip, onClick = { onTripClick(trip.id) })
                }
                item {
                    Text(
                        "View All History",
                        modifier = Modifier.clickable { onHistoryClick() }.padding(vertical = 8.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun TripCard(trip: Trip, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(trip.name ?: "Trip", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(
                "${FormatUtils.formatDistance(trip.metrics.totalDistanceMeters)} · ${FormatUtils.formatDuration(trip.metrics.totalDurationMs)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
