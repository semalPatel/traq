package com.traq.core.export.format

import android.util.Xml
import com.traq.core.common.model.ExportFormat
import com.traq.core.data.model.TrackPoint
import com.traq.core.data.model.Trip
import com.traq.core.export.api.TripExporter
import org.xmlpull.v1.XmlSerializer
import java.io.OutputStream
import javax.inject.Inject

class KmlExporter @Inject constructor() : TripExporter {
    override val format = ExportFormat.KML
    override val fileExtension = "kml"

    override suspend fun export(trip: Trip, trackPoints: List<TrackPoint>, outputStream: OutputStream) {
        val serializer = Xml.newSerializer()
        serializer.setOutput(outputStream, "UTF-8")
        serializer.startDocument("UTF-8", true)

        serializer.startTag(null, "kml")
        serializer.attribute(null, "xmlns", "http://www.opengis.net/kml/2.2")

        serializer.startTag(null, "Document")
        writeElement(serializer, "name", trip.name ?: "Traq Trip")

        serializer.startTag(null, "Placemark")
        writeElement(serializer, "name", trip.name ?: "Traq Trip")

        serializer.startTag(null, "LineString")
        writeElement(serializer, "tessellate", "1")

        val coords = trackPoints.joinToString("\n") { point ->
            val alt = point.altitude ?: 0.0
            "${point.longitude},${point.latitude},${alt}"
        }
        writeElement(serializer, "coordinates", coords)

        serializer.endTag(null, "LineString")
        serializer.endTag(null, "Placemark")
        serializer.endTag(null, "Document")
        serializer.endTag(null, "kml")
        serializer.endDocument()
        outputStream.flush()
    }

    private fun writeElement(serializer: XmlSerializer, tag: String, text: String) {
        serializer.startTag(null, tag)
        serializer.text(text)
        serializer.endTag(null, tag)
    }
}
