package com.traq.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.traq.core.data.repository.TripRepository
import com.traq.core.permissions.PermissionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {

    @Inject lateinit var tripRepository: TripRepository
    @Inject lateinit var permissionManager: PermissionManager

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action != Intent.ACTION_BOOT_COMPLETED && action != Intent.ACTION_MY_PACKAGE_REPLACED) {
            return
        }

        val pendingResult = goAsync()
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            try {
                val activeTrip = tripRepository.getActiveTrip()
                val canRecoverTracking = permissionManager.hasLocationPermission() &&
                    permissionManager.hasBackgroundLocationPermission() &&
                    permissionManager.hasNotificationPermission()

                if (activeTrip != null && canRecoverTracking) {
                    ContextCompat.startForegroundService(
                        context,
                        Intent(context, com.traq.core.location.service.TrackingService::class.java)
                    )
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
