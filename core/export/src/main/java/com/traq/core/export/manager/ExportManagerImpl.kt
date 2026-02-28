package com.traq.core.export.manager

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.traq.core.common.model.ExportFormat
import com.traq.core.data.repository.TrackPointRepository
import com.traq.core.data.repository.TripRepository
import com.traq.core.export.api.ExportManager
import com.traq.core.export.api.TripExporter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ExportManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tripRepository: TripRepository,
    private val trackPointRepository: TrackPointRepository,
    private val exporters: Map<ExportFormat, @JvmSuppressWildcards TripExporter>
) : ExportManager {

    override suspend fun exportTrip(tripId: String, format: ExportFormat): Uri =
        withContext(Dispatchers.IO) {
            val trip = tripRepository.getTripFlow(tripId).first()
                ?: throw IllegalArgumentException("Trip not found: $tripId")
            val points = trackPointRepository.getTrackPointsFlow(tripId).first()

            val exporter = exporters[format]
                ?: throw IllegalArgumentException("Unsupported format: $format")

            val fileName = buildFileName(trip.name, trip.startTime, exporter.fileExtension)
            val dir = File(context.getExternalFilesDir(null), "exports")
            dir.mkdirs()
            val file = File(dir, fileName)

            FileOutputStream(file).use { fos ->
                exporter.export(trip, points, fos)
            }

            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        }

    override suspend fun exportTripToShare(tripId: String, format: ExportFormat): Intent {
        val uri = exportTrip(tripId, format)
        return Intent(Intent.ACTION_SEND).apply {
            type = getMimeType(format)
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    override fun getSupportedFormats(): List<ExportFormat> = ExportFormat.entries.toList()

    private fun buildFileName(name: String?, startTime: java.time.Instant, extension: String): String {
        val date = startTime.atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmm"))
        val safeName = name?.replace(Regex("[^a-zA-Z0-9]"), "_") ?: "trip"
        return "${safeName}_${date}.${extension}"
    }

    private fun getMimeType(format: ExportFormat): String = when (format) {
        ExportFormat.TRAQ -> "application/json"
        ExportFormat.GPX -> "application/gpx+xml"
        ExportFormat.GEOJSON -> "application/geo+json"
        ExportFormat.KML -> "application/vnd.google-earth.kml+xml"
    }
}
