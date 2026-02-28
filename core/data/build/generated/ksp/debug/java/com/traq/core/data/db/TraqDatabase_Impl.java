package com.traq.core.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.traq.core.data.db.dao.TrackPointDao;
import com.traq.core.data.db.dao.TrackPointDao_Impl;
import com.traq.core.data.db.dao.TripDao;
import com.traq.core.data.db.dao.TripDao_Impl;
import com.traq.core.data.db.dao.TripSegmentDao;
import com.traq.core.data.db.dao.TripSegmentDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TraqDatabase_Impl extends TraqDatabase {
  private volatile TripDao _tripDao;

  private volatile TrackPointDao _trackPointDao;

  private volatile TripSegmentDao _tripSegmentDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `trips` (`id` TEXT NOT NULL, `name` TEXT, `startTime` INTEGER NOT NULL, `endTime` INTEGER, `status` TEXT NOT NULL, `dominantMode` TEXT, `totalDistanceMeters` REAL NOT NULL, `totalDurationMs` INTEGER NOT NULL, `movingDurationMs` INTEGER NOT NULL, `avgSpeedMps` REAL NOT NULL, `maxSpeedMps` REAL NOT NULL, `totalAscentMeters` REAL NOT NULL, `totalDescentMeters` REAL NOT NULL, `startLatitude` REAL NOT NULL, `startLongitude` REAL NOT NULL, `endLatitude` REAL, `endLongitude` REAL, `batteryStartPercent` INTEGER NOT NULL, `batteryEndPercent` INTEGER, `pointCount` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `track_points` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `tripId` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `altitude` REAL, `speed` REAL, `bearing` REAL, `horizontalAccuracy` REAL, `verticalAccuracy` REAL, `provider` TEXT NOT NULL, `transportMode` TEXT, `isInterpolated` INTEGER NOT NULL, `batteryPercent` INTEGER, `accelerometerMagnitude` REAL, `segmentIndex` INTEGER NOT NULL, FOREIGN KEY(`tripId`) REFERENCES `trips`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_track_points_tripId_timestamp` ON `track_points` (`tripId`, `timestamp`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_track_points_tripId_segmentIndex` ON `track_points` (`tripId`, `segmentIndex`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `trip_segments` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `tripId` TEXT NOT NULL, `segmentIndex` INTEGER NOT NULL, `startTime` INTEGER NOT NULL, `endTime` INTEGER, `transportMode` TEXT, `distanceMeters` REAL NOT NULL, `reason` TEXT NOT NULL, FOREIGN KEY(`tripId`) REFERENCES `trips`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '58ca80eb73eb1cf8feae8966f569e357')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `trips`");
        db.execSQL("DROP TABLE IF EXISTS `track_points`");
        db.execSQL("DROP TABLE IF EXISTS `trip_segments`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsTrips = new HashMap<String, TableInfo.Column>(20);
        _columnsTrips.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("startTime", new TableInfo.Column("startTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("endTime", new TableInfo.Column("endTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("dominantMode", new TableInfo.Column("dominantMode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("totalDistanceMeters", new TableInfo.Column("totalDistanceMeters", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("totalDurationMs", new TableInfo.Column("totalDurationMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("movingDurationMs", new TableInfo.Column("movingDurationMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("avgSpeedMps", new TableInfo.Column("avgSpeedMps", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("maxSpeedMps", new TableInfo.Column("maxSpeedMps", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("totalAscentMeters", new TableInfo.Column("totalAscentMeters", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("totalDescentMeters", new TableInfo.Column("totalDescentMeters", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("startLatitude", new TableInfo.Column("startLatitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("startLongitude", new TableInfo.Column("startLongitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("endLatitude", new TableInfo.Column("endLatitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("endLongitude", new TableInfo.Column("endLongitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("batteryStartPercent", new TableInfo.Column("batteryStartPercent", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("batteryEndPercent", new TableInfo.Column("batteryEndPercent", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrips.put("pointCount", new TableInfo.Column("pointCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTrips = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTrips = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTrips = new TableInfo("trips", _columnsTrips, _foreignKeysTrips, _indicesTrips);
        final TableInfo _existingTrips = TableInfo.read(db, "trips");
        if (!_infoTrips.equals(_existingTrips)) {
          return new RoomOpenHelper.ValidationResult(false, "trips(com.traq.core.data.db.entity.TripEntity).\n"
                  + " Expected:\n" + _infoTrips + "\n"
                  + " Found:\n" + _existingTrips);
        }
        final HashMap<String, TableInfo.Column> _columnsTrackPoints = new HashMap<String, TableInfo.Column>(16);
        _columnsTrackPoints.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackPoints.put("tripId", new TableInfo.Column("tripId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackPoints.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackPoints.put("latitude", new TableInfo.Column("latitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackPoints.put("longitude", new TableInfo.Column("longitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackPoints.put("altitude", new TableInfo.Column("altitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackPoints.put("speed", new TableInfo.Column("speed", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackPoints.put("bearing", new TableInfo.Column("bearing", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackPoints.put("horizontalAccuracy", new TableInfo.Column("horizontalAccuracy", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackPoints.put("verticalAccuracy", new TableInfo.Column("verticalAccuracy", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackPoints.put("provider", new TableInfo.Column("provider", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackPoints.put("transportMode", new TableInfo.Column("transportMode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackPoints.put("isInterpolated", new TableInfo.Column("isInterpolated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackPoints.put("batteryPercent", new TableInfo.Column("batteryPercent", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackPoints.put("accelerometerMagnitude", new TableInfo.Column("accelerometerMagnitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrackPoints.put("segmentIndex", new TableInfo.Column("segmentIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTrackPoints = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTrackPoints.add(new TableInfo.ForeignKey("trips", "CASCADE", "NO ACTION", Arrays.asList("tripId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTrackPoints = new HashSet<TableInfo.Index>(2);
        _indicesTrackPoints.add(new TableInfo.Index("index_track_points_tripId_timestamp", false, Arrays.asList("tripId", "timestamp"), Arrays.asList("ASC", "ASC")));
        _indicesTrackPoints.add(new TableInfo.Index("index_track_points_tripId_segmentIndex", false, Arrays.asList("tripId", "segmentIndex"), Arrays.asList("ASC", "ASC")));
        final TableInfo _infoTrackPoints = new TableInfo("track_points", _columnsTrackPoints, _foreignKeysTrackPoints, _indicesTrackPoints);
        final TableInfo _existingTrackPoints = TableInfo.read(db, "track_points");
        if (!_infoTrackPoints.equals(_existingTrackPoints)) {
          return new RoomOpenHelper.ValidationResult(false, "track_points(com.traq.core.data.db.entity.TrackPointEntity).\n"
                  + " Expected:\n" + _infoTrackPoints + "\n"
                  + " Found:\n" + _existingTrackPoints);
        }
        final HashMap<String, TableInfo.Column> _columnsTripSegments = new HashMap<String, TableInfo.Column>(8);
        _columnsTripSegments.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTripSegments.put("tripId", new TableInfo.Column("tripId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTripSegments.put("segmentIndex", new TableInfo.Column("segmentIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTripSegments.put("startTime", new TableInfo.Column("startTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTripSegments.put("endTime", new TableInfo.Column("endTime", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTripSegments.put("transportMode", new TableInfo.Column("transportMode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTripSegments.put("distanceMeters", new TableInfo.Column("distanceMeters", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTripSegments.put("reason", new TableInfo.Column("reason", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTripSegments = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTripSegments.add(new TableInfo.ForeignKey("trips", "CASCADE", "NO ACTION", Arrays.asList("tripId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTripSegments = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTripSegments = new TableInfo("trip_segments", _columnsTripSegments, _foreignKeysTripSegments, _indicesTripSegments);
        final TableInfo _existingTripSegments = TableInfo.read(db, "trip_segments");
        if (!_infoTripSegments.equals(_existingTripSegments)) {
          return new RoomOpenHelper.ValidationResult(false, "trip_segments(com.traq.core.data.db.entity.TripSegmentEntity).\n"
                  + " Expected:\n" + _infoTripSegments + "\n"
                  + " Found:\n" + _existingTripSegments);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "58ca80eb73eb1cf8feae8966f569e357", "07781fac9378a37ce2183747a964abe0");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "trips","track_points","trip_segments");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `trips`");
      _db.execSQL("DELETE FROM `track_points`");
      _db.execSQL("DELETE FROM `trip_segments`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TripDao.class, TripDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TrackPointDao.class, TrackPointDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TripSegmentDao.class, TripSegmentDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public TripDao tripDao() {
    if (_tripDao != null) {
      return _tripDao;
    } else {
      synchronized(this) {
        if(_tripDao == null) {
          _tripDao = new TripDao_Impl(this);
        }
        return _tripDao;
      }
    }
  }

  @Override
  public TrackPointDao trackPointDao() {
    if (_trackPointDao != null) {
      return _trackPointDao;
    } else {
      synchronized(this) {
        if(_trackPointDao == null) {
          _trackPointDao = new TrackPointDao_Impl(this);
        }
        return _trackPointDao;
      }
    }
  }

  @Override
  public TripSegmentDao tripSegmentDao() {
    if (_tripSegmentDao != null) {
      return _tripSegmentDao;
    } else {
      synchronized(this) {
        if(_tripSegmentDao == null) {
          _tripSegmentDao = new TripSegmentDao_Impl(this);
        }
        return _tripSegmentDao;
      }
    }
  }
}
