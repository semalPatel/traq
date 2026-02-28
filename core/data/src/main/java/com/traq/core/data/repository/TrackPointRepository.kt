package com.traq.core.data.repository

import com.traq.core.data.model.TrackPoint
import kotlinx.coroutines.flow.Flow

interface TrackPointRepository {
    fun getTrackPointsFlow(tripId: String): Flow<List<TrackPoint>>
    fun getTrackPointsPaginated(tripId: String, limit: Int, offset: Int): Flow<List<TrackPoint>>
    suspend fun insert(point: TrackPoint)
    suspend fun insertBatch(points: List<TrackPoint>)
    suspend fun getPointCount(tripId: String): Int
}
