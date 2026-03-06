package com.traq.core.data.repository

import com.traq.core.data.model.Trip
import com.traq.core.data.model.TripMetrics
import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface TripRepository {
    fun getTripsFlow(): Flow<List<Trip>>
    fun getTripFlow(id: String): Flow<Trip?>
    suspend fun createTrip(trip: Trip): String
    suspend fun updateTrip(trip: Trip)
    suspend fun completeTrip(id: String, endTime: Instant, metrics: TripMetrics)
    suspend fun renameTrip(id: String, name: String)
    suspend fun deleteTrip(id: String)
    suspend fun getActiveTrip(): Trip?
}
