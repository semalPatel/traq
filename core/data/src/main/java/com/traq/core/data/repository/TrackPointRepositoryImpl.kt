package com.traq.core.data.repository

import com.traq.core.data.db.dao.TrackPointDao
import com.traq.core.data.mapper.TrackPointMapper.toDomain
import com.traq.core.data.mapper.TrackPointMapper.toEntity
import com.traq.core.data.model.TrackPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrackPointRepositoryImpl @Inject constructor(
    private val trackPointDao: TrackPointDao
) : TrackPointRepository {

    override fun getTrackPointsFlow(tripId: String): Flow<List<TrackPoint>> =
        trackPointDao.getTrackPointsForTrip(tripId).map { entities -> entities.map { it.toDomain() } }

    override fun getTrackPointsPaginated(tripId: String, limit: Int, offset: Int): Flow<List<TrackPoint>> =
        trackPointDao.getTrackPointsPaginated(tripId, limit, offset).map { entities -> entities.map { it.toDomain() } }

    override suspend fun insert(point: TrackPoint) = withContext(Dispatchers.IO) {
        trackPointDao.insert(point.toEntity())
    }

    override suspend fun insertBatch(points: List<TrackPoint>) = withContext(Dispatchers.IO) {
        trackPointDao.insertAll(points.map { it.toEntity() })
    }

    override suspend fun getPointCount(tripId: String): Int = withContext(Dispatchers.IO) {
        trackPointDao.getPointCount(tripId)
    }
}
