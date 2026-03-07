package com.traq.core.data.repository

import com.traq.core.data.db.dao.TripDao
import com.traq.core.data.mapper.TripMapper.toDomain
import com.traq.core.data.mapper.TripMapper.toEntity
import com.traq.core.data.model.Trip
import com.traq.core.data.model.TripMetrics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val tripDao: TripDao
) : TripRepository {

    override fun getTripsFlow(): Flow<List<Trip>> =
        tripDao.getAllTrips().map { entities -> entities.map { it.toDomain() } }

    override fun getTripFlow(id: String): Flow<Trip?> =
        tripDao.getTripById(id).map { it?.toDomain() }

    override suspend fun createTrip(trip: Trip): String = withContext(Dispatchers.IO) {
        tripDao.insert(trip.toEntity())
        trip.id
    }

    override suspend fun updateTrip(trip: Trip) = withContext(Dispatchers.IO) {
        tripDao.update(trip.toEntity())
    }

    override suspend fun completeTrip(id: String, endTime: Instant, metrics: TripMetrics) =
        withContext(Dispatchers.IO) {
            val entity = tripDao.getTripById(id).first() ?: return@withContext
            tripDao.update(
                entity.copy(
                    endTime = endTime.toEpochMilli(),
                    status = "COMPLETED",
                    totalDistanceMeters = metrics.totalDistanceMeters,
                    totalDurationMs = metrics.totalDurationMs,
                    movingDurationMs = metrics.movingDurationMs,
                    avgSpeedMps = metrics.avgSpeedMps,
                    maxSpeedMps = metrics.maxSpeedMps,
                    totalAscentMeters = metrics.totalAscentMeters,
                    totalDescentMeters = metrics.totalDescentMeters,
                    batteryEndPercent = metrics.batteryEndPercent,
                    pointCount = metrics.pointCount
                )
            )
        }

    override suspend fun renameTrip(id: String, name: String) = withContext(Dispatchers.IO) {
        tripDao.updateName(id, name)
    }

    override suspend fun deleteTrip(id: String) = withContext(Dispatchers.IO) {
        tripDao.deleteById(id)
    }

    override suspend fun getActiveTrip(): Trip? = withContext(Dispatchers.IO) {
        tripDao.getActiveTrip()?.toDomain()
    }
}
