package com.traq.core.location.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.traq.core.location.model.TrackingState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TrackingNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_ID = "traq_tracking"
        const val NOTIFICATION_ID = 1001
    }

    fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Trip Tracking",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Shows tracking progress during active trips"
            setShowBadge(false)
        }
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(channel)
    }

    fun buildNotification(state: TrackingState): Notification {
        val distanceKm = "%.1f km".format(state.distanceMeters / 1000)
        val elapsed = formatElapsed(state.elapsedMs)
        val speed = state.currentSpeedMps?.let { "%.0f km/h".format(it * 3.6f) } ?: "--"
        val mode = state.currentMode?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: ""
        val statusText = when {
            state.isPaused -> "Paused"
            state.isLocationStale && state.isUsingEstimatedLocation -> "GPS weak · Estimating position"
            state.isLocationStale -> "GPS signal weak · Waiting for update"
            else -> "$distanceKm · $elapsed · $speed"
        }

        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val contentPendingIntent = launchIntent?.let {
            PendingIntent.getActivity(context, 0, it, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(if (state.isPaused) "Traq — Paused" else "Traq — Recording${if (mode.isNotEmpty()) " ($mode)" else ""}")
            .setContentText(statusText)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(contentPendingIntent)

        if (state.isPaused) {
            val resumeIntent = Intent(context, TrackingService::class.java).apply {
                action = TrackingService.ACTION_RESUME
            }
            val resumePi = PendingIntent.getService(
                context, 1, resumeIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            builder.addAction(android.R.drawable.ic_media_play, "Resume", resumePi)
        } else {
            val pauseIntent = Intent(context, TrackingService::class.java).apply {
                action = TrackingService.ACTION_PAUSE
            }
            val pausePi = PendingIntent.getService(
                context, 1, pauseIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            builder.addAction(android.R.drawable.ic_media_pause, "Pause", pausePi)
        }

        val stopIntent = Intent(context, TrackingService::class.java).apply {
            action = TrackingService.ACTION_STOP
        }
        val stopPi = PendingIntent.getService(
            context, 2, stopIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.addAction(android.R.drawable.ic_delete, "Stop", stopPi)

        return builder.build()
    }

    fun updateNotification(state: TrackingState) {
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_ID, buildNotification(state))
    }

    private fun formatElapsed(ms: Long): String {
        val hours = ms / 3_600_000
        val minutes = (ms % 3_600_000) / 60_000
        val seconds = (ms % 60_000) / 1_000
        return if (hours > 0) "%d:%02d:%02d".format(hours, minutes, seconds)
        else "%02d:%02d".format(minutes, seconds)
    }
}
