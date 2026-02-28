package com.traq.core.export.api

import com.traq.core.common.model.ExportFormat
import com.traq.core.data.model.TrackPoint
import com.traq.core.data.model.Trip
import java.io.OutputStream

interface TripExporter {
    val format: ExportFormat
    val fileExtension: String
    suspend fun export(trip: Trip, trackPoints: List<TrackPoint>, outputStream: OutputStream)
}
