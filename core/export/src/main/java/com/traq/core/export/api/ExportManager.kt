package com.traq.core.export.api

import android.content.Intent
import android.net.Uri
import com.traq.core.common.model.ExportFormat

interface ExportManager {
    suspend fun exportTrip(tripId: String, format: ExportFormat): Uri
    suspend fun exportTripToShare(tripId: String, format: ExportFormat): Intent
    fun getSupportedFormats(): List<ExportFormat>
}
