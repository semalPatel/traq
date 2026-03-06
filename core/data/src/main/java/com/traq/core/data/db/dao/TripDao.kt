package com.traq.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.traq.core.data.db.entity.TripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Query("SELECT * FROM trips ORDER BY startTime DESC")
    fun getAllTrips(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE id = :id")
    fun getTripById(id: String): Flow<TripEntity?>

    @Query("SELECT * FROM trips WHERE status = :status")
    fun getTripsByStatus(status: String): Flow<List<TripEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trip: TripEntity)

    @Update
    suspend fun update(trip: TripEntity)

    @Query("UPDATE trips SET name = :name WHERE id = :id")
    suspend fun updateName(id: String, name: String)

    @Query("DELETE FROM trips WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM trips WHERE status IN ('ACTIVE', 'PAUSED') ORDER BY startTime DESC LIMIT 1")
    suspend fun getActiveTrip(): TripEntity?

    @Query("SELECT COUNT(*) FROM trips")
    suspend fun getTripCount(): Int

    @Query("""
        SELECT SUM(totalDistanceMeters) as totalDistance,
               SUM(totalDurationMs) as totalDuration,
               COUNT(*) as tripCount
        FROM trips
        WHERE startTime >= :sinceTimestamp
    """)
    suspend fun getAggregateStats(sinceTimestamp: Long): AggregateStats?
}

data class AggregateStats(
    val totalDistance: Double?,
    val totalDuration: Long?,
    val tripCount: Int
)
