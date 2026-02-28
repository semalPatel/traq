package com.traq.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.traq.core.data.db.dao.TrackPointDao
import com.traq.core.data.db.dao.TripDao
import com.traq.core.data.db.dao.TripSegmentDao
import com.traq.core.data.db.entity.TrackPointEntity
import com.traq.core.data.db.entity.TripEntity
import com.traq.core.data.db.entity.TripSegmentEntity

@Database(
    entities = [TripEntity::class, TrackPointEntity::class, TripSegmentEntity::class],
    version = 1,
    exportSchema = true
)
abstract class TraqDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun trackPointDao(): TrackPointDao
    abstract fun tripSegmentDao(): TripSegmentDao
}
