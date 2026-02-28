package com.traq.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.traq.core.data.db.entity.TrackPointEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackPointDao {
    @Query("SELECT * FROM track_points WHERE tripId = :tripId ORDER BY timestamp ASC")
    fun getTrackPointsForTrip(tripId: String): Flow<List<TrackPointEntity>>

    @Query("SELECT * FROM track_points WHERE tripId = :tripId ORDER BY timestamp ASC LIMIT :limit OFFSET :offset")
    fun getTrackPointsPaginated(tripId: String, limit: Int, offset: Int): Flow<List<TrackPointEntity>>

    @Query("SELECT * FROM track_points WHERE tripId = :tripId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastTrackPoint(tripId: String): TrackPointEntity?

    @Insert
    suspend fun insert(point: TrackPointEntity)

    @Insert
    suspend fun insertAll(points: List<TrackPointEntity>)

    @Query("SELECT COUNT(*) FROM track_points WHERE tripId = :tripId")
    suspend fun getPointCount(tripId: String): Int

    @Query("DELETE FROM track_points WHERE tripId = :tripId")
    suspend fun deleteForTrip(tripId: String)

    @Query("""
        SELECT MIN(latitude) as minLat, MAX(latitude) as maxLat,
               MIN(longitude) as minLon, MAX(longitude) as maxLon
        FROM track_points WHERE tripId = :tripId
    """)
    suspend fun getBoundsForTrip(tripId: String): TripBounds?
}

data class TripBounds(
    val minLat: Double?,
    val maxLat: Double?,
    val minLon: Double?,
    val maxLon: Double?
)
