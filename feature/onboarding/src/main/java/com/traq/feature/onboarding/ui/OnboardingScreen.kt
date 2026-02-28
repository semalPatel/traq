package com.traq.feature.onboarding.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.traq.feature.onboarding.viewmodel.OnboardingViewModel

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val progress = (state.currentPage + 1).toFloat() / state.totalPages

    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { viewModel.onPermissionResult() }

    val bgLocationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { viewModel.onPermissionResult() }

    val notificationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { viewModel.onPermissionResult() }

    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(32.dp))

            when (state.currentPage) {
                0 -> OnboardingPage(
                    icon = Icons.Default.Route,
                    title = "Welcome to Traq",
                    description = "AI-powered GPS tracking that respects your battery. Record your journeys with precision.",
                    buttonText = "Get Started",
                    onAction = { viewModel.nextPage() }
                )
                1 -> OnboardingPage(
                    icon = Icons.Default.LocationOn,
                    title = "Location Access",
                    description = "Traq needs location access to record your trips. This is the core feature of the app.",
                    buttonText = if (state.permissionState.location) "Granted" else "Grant Location",
                    isGranted = state.permissionState.location,
                    onAction = {
                        if (!state.permissionState.location) {
                            locationLauncher.launch(arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ))
                        } else viewModel.nextPage()
                    },
                    onSkip = { viewModel.nextPage() }
                )
                2 -> OnboardingPage(
                    icon = Icons.Default.MyLocation,
                    title = "Background Location",
                    description = "Allow 'All the time' location access so tracking works when your screen is off.",
                    buttonText = if (state.permissionState.backgroundLocation) "Granted" else "Grant Background",
                    isGranted = state.permissionState.backgroundLocation,
                    onAction = {
                        if (!state.permissionState.backgroundLocation && Build.VERSION.SDK_INT >= 29) {
                            bgLocationLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        } else viewModel.nextPage()
                    },
                    onSkip = { viewModel.nextPage() }
                )
                3 -> OnboardingPage(
                    icon = Icons.Default.BatteryChargingFull,
                    title = "Battery Optimization",
                    description = "Disable battery optimization for Traq to prevent the system from stopping tracking.",
                    buttonText = if (state.permissionState.batteryOptimization) "Disabled" else "Fix Battery Settings",
                    isGranted = state.permissionState.batteryOptimization,
                    onAction = { viewModel.nextPage() },
                    onSkip = { viewModel.nextPage() }
                )
                else -> {
                    if (state.currentPage == state.totalPages - 1) {
                        OnboardingPage(
                            icon = Icons.Default.Check,
                            title = "You're All Set!",
                            description = "Traq is ready to record your trips. Tap below to start.",
                            buttonText = "Start Using Traq",
                            onAction = onComplete
                        )
                    } else if (Build.VERSION.SDK_INT >= 33 && !state.permissionState.notifications) {
                        OnboardingPage(
                            icon = Icons.Default.Notifications,
                            title = "Notifications",
                            description = "See live trip progress in your notification bar.",
                            buttonText = "Allow Notifications",
                            onAction = {
                                if (Build.VERSION.SDK_INT >= 33) {
                                    notificationLauncher.launch("android.permission.POST_NOTIFICATIONS")
                                }
                                viewModel.nextPage()
                            },
                            onSkip = { viewModel.nextPage() }
                        )
                    } else {
                        viewModel.nextPage()
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingPage(
    icon: ImageVector,
    title: String,
    description: String,
    buttonText: String,
    isGranted: Boolean = false,
    onAction: () -> Unit,
    onSkip: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon, contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = if (isGranted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))
        Text(title, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(Modifier.height(12.dp))
        Text(
            description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))
        Button(onClick = onAction, modifier = Modifier.fillMaxWidth()) {
            if (isGranted) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
            Text("  $buttonText")
        }
        if (onSkip != null && !isGranted) {
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onSkip) { Text("Skip for now") }
        }
    }
}
