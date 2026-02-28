package com.traq.core.export.format

import android.util.Xml
import com.traq.core.common.model.ExportFormat
import com.traq.core.data.model.TrackPoint
import com.traq.core.data.model.Trip
import com.traq.core.export.api.TripExporter
import org.xmlpull.v1.XmlSerializer
import java.io.OutputStream
import javax.inject.Inject

class GpxExporter @Inject constructor() : TripExporter {
    override val format = ExportFormat.GPX
    override val fileExtension = "gpx"

    override suspend fun export(trip: Trip, trackPoints: List<TrackPoint>, outputStream: OutputStream) {
        val serializer = Xml.newSerializer()
        serializer.setOutput(outputStream, "UTF-8")
        serializer.startDocument("UTF-8", true)

        serializer.startTag(null, "gpx")
        serializer.attribute(null, "version", "1.1")
        serializer.attribute(null, "creator", "Traq")
        serializer.attribute(null, "xmlns", "http://www.topografix.com/GPX/1/1")

        serializer.startTag(null, "metadata")
        writeElement(serializer, "name", trip.name ?: "Traq Trip")
        writeElement(serializer, "time", trip.startTime.toString())
        serializer.endTag(null, "metadata")

        serializer.startTag(null, "trk")
        writeElement(serializer, "name", trip.name ?: "Traq Trip")

        val segments = trackPoints.groupBy { it.segmentIndex }
        segments.toSortedMap().forEach { (_, segmentPoints) ->
            serializer.startTag(null, "trkseg")
            segmentPoints.forEach { point ->
                writeTrackPoint(serializer, point)
            }
            serializer.endTag(null, "trkseg")
        }

        serializer.endTag(null, "trk")
        serializer.endTag(null, "gpx")
        serializer.endDocument()
        outputStream.flush()
    }

    private fun writeTrackPoint(serializer: XmlSerializer, point: TrackPoint) {
        serializer.startTag(null, "trkpt")
        serializer.attribute(null, "lat", point.latitude.toString())
        serializer.attribute(null, "lon", point.longitude.toString())

        point.altitude?.let { writeElement(serializer, "ele", it.toString()) }
        writeElement(serializer, "time", point.timestamp.toString())

        val hasExtensions = point.speed != null || point.transportMode != null || point.isInterpolated
        if (hasExtensions) {
            serializer.startTag(null, "extensions")
            point.speed?.let { writeElement(serializer, "speed", it.toString()) }
            point.horizontalAccuracy?.let { writeElement(serializer, "accuracy", it.toString()) }
            point.transportMode?.let { writeElement(serializer, "mode", it.name) }
            if (point.isInterpolated) writeElement(serializer, "interpolated", "true")
            serializer.endTag(null, "extensions")
        }

        serializer.endTag(null, "trkpt")
    }

    private fun writeElement(serializer: XmlSerializer, tag: String, text: String) {
        serializer.startTag(null, tag)
        serializer.text(text)
        serializer.endTag(null, tag)
    }
}
