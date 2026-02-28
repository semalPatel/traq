package com.traq.core.export.format

import com.traq.core.common.model.ExportFormat
import com.traq.core.data.model.TrackPoint
import com.traq.core.data.model.Trip
import com.traq.core.export.api.TripExporter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.io.OutputStream
import javax.inject.Inject

class GeoJsonExporter @Inject constructor() : TripExporter {
    override val format = ExportFormat.GEOJSON
    override val fileExtension = "geojson"

    private val json = Json { prettyPrint = true }

    override suspend fun export(trip: Trip, trackPoints: List<TrackPoint>, outputStream: OutputStream) {
        val coordinates = trackPoints.map { point ->
            buildJsonArray {
                add(JsonPrimitive(point.longitude))
                add(JsonPrimitive(point.latitude))
                point.altitude?.let { add(JsonPrimitive(it)) }
            }
        }

        val geoJson = buildJsonObject {
            put("type", "FeatureCollection")
            put("features", buildJsonArray {
                add(buildJsonObject {
                    put("type", "Feature")
                    put("geometry", buildJsonObject {
                        put("type", "LineString")
                        put("coordinates", JsonArray(coordinates))
                    })
                    put("properties", buildJsonObject {
                        put("name", trip.name ?: "Traq Trip")
                        put("startTime", trip.startTime.toString())
                        trip.endTime?.let { put("endTime", it.toString()) }
                        put("totalDistance", trip.metrics.totalDistanceMeters)
                        put("duration", trip.metrics.totalDurationMs)
                        put("avgSpeed", trip.metrics.avgSpeedMps)
                        put("maxSpeed", trip.metrics.maxSpeedMps)
                        trip.dominantMode?.let { put("dominantMode", it.name) }
                    })
                })
            })
        }

        outputStream.bufferedWriter().use { writer ->
            writer.write(json.encodeToString(JsonObject.serializer(), geoJson))
        }
    }
}
