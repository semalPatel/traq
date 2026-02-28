package com.traq.core.location.util

import android.content.Context
import android.os.PowerManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WakeLockManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var wakeLock: PowerManager.WakeLock? = null

    fun acquire() {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "traq:tracking")
        wakeLock?.acquire(60 * 60 * 1000L)
    }

    fun release() {
        wakeLock?.let { if (it.isHeld) it.release() }
        wakeLock = null
    }
}
