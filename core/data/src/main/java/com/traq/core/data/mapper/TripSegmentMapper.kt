package com.traq.core.data.mapper

import com.traq.core.common.model.SegmentReason
import com.traq.core.common.model.TransportMode
import com.traq.core.data.db.entity.TripSegmentEntity
import com.traq.core.data.model.TripSegment
import java.time.Instant

object TripSegmentMapper {
    fun TripSegmentEntity.toDomain(): TripSegment = TripSegment(
        id = id,
        tripId = tripId,
        segmentIndex = segmentIndex,
        startTime = Instant.ofEpochMilli(startTime),
        endTime = endTime?.let { Instant.ofEpochMilli(it) },
        transportMode = transportMode?.let { TransportMode.valueOf(it) },
        distanceMeters = distanceMeters,
        reason = SegmentReason.valueOf(reason)
    )

    fun TripSegment.toEntity(): TripSegmentEntity = TripSegmentEntity(
        id = id,
        tripId = tripId,
        segmentIndex = segmentIndex,
        startTime = startTime.toEpochMilli(),
        endTime = endTime?.toEpochMilli(),
        transportMode = transportMode?.name,
        distanceMeters = distanceMeters,
        reason = reason.name
    )
}
