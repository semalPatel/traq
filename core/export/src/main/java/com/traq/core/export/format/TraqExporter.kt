package com.traq.core.export.format

import com.traq.core.common.model.ExportFormat
import com.traq.core.data.model.TrackPoint
import com.traq.core.data.model.Trip
import com.traq.core.export.api.TripExporter
import com.traq.core.export.model.TraqFile
import com.traq.core.export.model.TraqMetrics
import com.traq.core.export.model.TraqPoint
import com.traq.core.export.model.TraqTrip
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.OutputStream
import java.time.Instant
import javax.inject.Inject

class TraqExporter @Inject constructor() : TripExporter {
    override val format = ExportFormat.TRAQ
    override val fileExtension = "traq"

    private val json = Json {
        prettyPrint = true
        encodeDefaults = false
    }

    override suspend fun export(trip: Trip, trackPoints: List<TrackPoint>, outputStream: OutputStream) {
        val traqFile = TraqFile(
            exportedAt = Instant.now().toString(),
            trip = TraqTrip(
                id = trip.id,
                name = trip.name,
                startTime = trip.startTime.toString(),
                endTime = trip.endTime?.toString(),
                metrics = TraqMetrics(
                    totalDistanceMeters = trip.metrics.totalDistanceMeters,
                    movingTimeMs = trip.metrics.movingDurationMs,
                    stoppedTimeMs = trip.metrics.totalDurationMs - trip.metrics.movingDurationMs,
                    avgSpeedMps = trip.metrics.avgSpeedMps,
                    maxSpeedMps = trip.metrics.maxSpeedMps,
                    totalAscentMeters = trip.metrics.totalAscentMeters,
                    totalDescentMeters = trip.metrics.totalDescentMeters,
                    batteryUsedPercent = trip.metrics.batteryUsedPercent,
                    pointCount = trip.metrics.pointCount,
                    dominantMode = trip.dominantMode?.name
                ),
                trackPoints = trackPoints.map { it.toTraqPoint() }
            )
        )
        outputStream.bufferedWriter().use { writer ->
            writer.write(json.encodeToString(traqFile))
        }
    }

    private fun TrackPoint.toTraqPoint() = TraqPoint(
        t = timestamp.toEpochMilli(),
        lat = latitude,
        lon = longitude,
        alt = altitude,
        spd = speed,
        brg = bearing,
        acc = horizontalAccuracy,
        vacc = verticalAccuracy,
        mode = transportMode?.name,
        interp = isInterpolated
    )
}
