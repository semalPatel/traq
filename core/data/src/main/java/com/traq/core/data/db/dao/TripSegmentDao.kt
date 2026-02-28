package com.traq.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.traq.core.data.db.entity.TripSegmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripSegmentDao {
    @Query("SELECT * FROM trip_segments WHERE tripId = :tripId ORDER BY segmentIndex ASC")
    fun getSegmentsForTrip(tripId: String): Flow<List<TripSegmentEntity>>

    @Insert
    suspend fun insert(segment: TripSegmentEntity)

    @Update
    suspend fun update(segment: TripSegmentEntity)

    @Query("DELETE FROM trip_segments WHERE tripId = :tripId")
    suspend fun deleteForTrip(tripId: String)
}
