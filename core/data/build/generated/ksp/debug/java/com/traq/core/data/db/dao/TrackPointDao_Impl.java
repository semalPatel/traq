package com.traq.core.data.db.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.traq.core.data.db.entity.TrackPointEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Float;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TrackPointDao_Impl implements TrackPointDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TrackPointEntity> __insertionAdapterOfTrackPointEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteForTrip;

  public TrackPointDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTrackPointEntity = new EntityInsertionAdapter<TrackPointEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `track_points` (`id`,`tripId`,`timestamp`,`latitude`,`longitude`,`altitude`,`speed`,`bearing`,`horizontalAccuracy`,`verticalAccuracy`,`provider`,`transportMode`,`isInterpolated`,`batteryPercent`,`accelerometerMagnitude`,`segmentIndex`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TrackPointEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTripId());
        statement.bindLong(3, entity.getTimestamp());
        statement.bindDouble(4, entity.getLatitude());
        statement.bindDouble(5, entity.getLongitude());
        if (entity.getAltitude() == null) {
          statement.bindNull(6);
        } else {
          statement.bindDouble(6, entity.getAltitude());
        }
        if (entity.getSpeed() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getSpeed());
        }
        if (entity.getBearing() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getBearing());
        }
        if (entity.getHorizontalAccuracy() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getHorizontalAccuracy());
        }
        if (entity.getVerticalAccuracy() == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.getVerticalAccuracy());
        }
        statement.bindString(11, entity.getProvider());
        if (entity.getTransportMode() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getTransportMode());
        }
        final int _tmp = entity.isInterpolated() ? 1 : 0;
        statement.bindLong(13, _tmp);
        if (entity.getBatteryPercent() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getBatteryPercent());
        }
        if (entity.getAccelerometerMagnitude() == null) {
          statement.bindNull(15);
        } else {
          statement.bindDouble(15, entity.getAccelerometerMagnitude());
        }
        statement.bindLong(16, entity.getSegmentIndex());
      }
    };
    this.__preparedStmtOfDeleteForTrip = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM track_points WHERE tripId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final TrackPointEntity point, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTrackPointEntity.insert(point);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<TrackPointEntity> points,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTrackPointEntity.insert(points);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteForTrip(final String tripId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteForTrip.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, tripId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteForTrip.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TrackPointEntity>> getTrackPointsForTrip(final String tripId) {
    final String _sql = "SELECT * FROM track_points WHERE tripId = ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, tripId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"track_points"}, new Callable<List<TrackPointEntity>>() {
      @Override
      @NonNull
      public List<TrackPointEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "altitude");
          final int _cursorIndexOfSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "speed");
          final int _cursorIndexOfBearing = CursorUtil.getColumnIndexOrThrow(_cursor, "bearing");
          final int _cursorIndexOfHorizontalAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "horizontalAccuracy");
          final int _cursorIndexOfVerticalAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "verticalAccuracy");
          final int _cursorIndexOfProvider = CursorUtil.getColumnIndexOrThrow(_cursor, "provider");
          final int _cursorIndexOfTransportMode = CursorUtil.getColumnIndexOrThrow(_cursor, "transportMode");
          final int _cursorIndexOfIsInterpolated = CursorUtil.getColumnIndexOrThrow(_cursor, "isInterpolated");
          final int _cursorIndexOfBatteryPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "batteryPercent");
          final int _cursorIndexOfAccelerometerMagnitude = CursorUtil.getColumnIndexOrThrow(_cursor, "accelerometerMagnitude");
          final int _cursorIndexOfSegmentIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "segmentIndex");
          final List<TrackPointEntity> _result = new ArrayList<TrackPointEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TrackPointEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTripId;
            _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final Double _tmpAltitude;
            if (_cursor.isNull(_cursorIndexOfAltitude)) {
              _tmpAltitude = null;
            } else {
              _tmpAltitude = _cursor.getDouble(_cursorIndexOfAltitude);
            }
            final Float _tmpSpeed;
            if (_cursor.isNull(_cursorIndexOfSpeed)) {
              _tmpSpeed = null;
            } else {
              _tmpSpeed = _cursor.getFloat(_cursorIndexOfSpeed);
            }
            final Float _tmpBearing;
            if (_cursor.isNull(_cursorIndexOfBearing)) {
              _tmpBearing = null;
            } else {
              _tmpBearing = _cursor.getFloat(_cursorIndexOfBearing);
            }
            final Float _tmpHorizontalAccuracy;
            if (_cursor.isNull(_cursorIndexOfHorizontalAccuracy)) {
              _tmpHorizontalAccuracy = null;
            } else {
              _tmpHorizontalAccuracy = _cursor.getFloat(_cursorIndexOfHorizontalAccuracy);
            }
            final Float _tmpVerticalAccuracy;
            if (_cursor.isNull(_cursorIndexOfVerticalAccuracy)) {
              _tmpVerticalAccuracy = null;
            } else {
              _tmpVerticalAccuracy = _cursor.getFloat(_cursorIndexOfVerticalAccuracy);
            }
            final String _tmpProvider;
            _tmpProvider = _cursor.getString(_cursorIndexOfProvider);
            final String _tmpTransportMode;
            if (_cursor.isNull(_cursorIndexOfTransportMode)) {
              _tmpTransportMode = null;
            } else {
              _tmpTransportMode = _cursor.getString(_cursorIndexOfTransportMode);
            }
            final boolean _tmpIsInterpolated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsInterpolated);
            _tmpIsInterpolated = _tmp != 0;
            final Integer _tmpBatteryPercent;
            if (_cursor.isNull(_cursorIndexOfBatteryPercent)) {
              _tmpBatteryPercent = null;
            } else {
              _tmpBatteryPercent = _cursor.getInt(_cursorIndexOfBatteryPercent);
            }
            final Float _tmpAccelerometerMagnitude;
            if (_cursor.isNull(_cursorIndexOfAccelerometerMagnitude)) {
              _tmpAccelerometerMagnitude = null;
            } else {
              _tmpAccelerometerMagnitude = _cursor.getFloat(_cursorIndexOfAccelerometerMagnitude);
            }
            final int _tmpSegmentIndex;
            _tmpSegmentIndex = _cursor.getInt(_cursorIndexOfSegmentIndex);
            _item = new TrackPointEntity(_tmpId,_tmpTripId,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpAltitude,_tmpSpeed,_tmpBearing,_tmpHorizontalAccuracy,_tmpVerticalAccuracy,_tmpProvider,_tmpTransportMode,_tmpIsInterpolated,_tmpBatteryPercent,_tmpAccelerometerMagnitude,_tmpSegmentIndex);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TrackPointEntity>> getTrackPointsPaginated(final String tripId, final int limit,
      final int offset) {
    final String _sql = "SELECT * FROM track_points WHERE tripId = ? ORDER BY timestamp ASC LIMIT ? OFFSET ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindString(_argIndex, tripId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    _argIndex = 3;
    _statement.bindLong(_argIndex, offset);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"track_points"}, new Callable<List<TrackPointEntity>>() {
      @Override
      @NonNull
      public List<TrackPointEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "altitude");
          final int _cursorIndexOfSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "speed");
          final int _cursorIndexOfBearing = CursorUtil.getColumnIndexOrThrow(_cursor, "bearing");
          final int _cursorIndexOfHorizontalAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "horizontalAccuracy");
          final int _cursorIndexOfVerticalAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "verticalAccuracy");
          final int _cursorIndexOfProvider = CursorUtil.getColumnIndexOrThrow(_cursor, "provider");
          final int _cursorIndexOfTransportMode = CursorUtil.getColumnIndexOrThrow(_cursor, "transportMode");
          final int _cursorIndexOfIsInterpolated = CursorUtil.getColumnIndexOrThrow(_cursor, "isInterpolated");
          final int _cursorIndexOfBatteryPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "batteryPercent");
          final int _cursorIndexOfAccelerometerMagnitude = CursorUtil.getColumnIndexOrThrow(_cursor, "accelerometerMagnitude");
          final int _cursorIndexOfSegmentIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "segmentIndex");
          final List<TrackPointEntity> _result = new ArrayList<TrackPointEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TrackPointEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTripId;
            _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final Double _tmpAltitude;
            if (_cursor.isNull(_cursorIndexOfAltitude)) {
              _tmpAltitude = null;
            } else {
              _tmpAltitude = _cursor.getDouble(_cursorIndexOfAltitude);
            }
            final Float _tmpSpeed;
            if (_cursor.isNull(_cursorIndexOfSpeed)) {
              _tmpSpeed = null;
            } else {
              _tmpSpeed = _cursor.getFloat(_cursorIndexOfSpeed);
            }
            final Float _tmpBearing;
            if (_cursor.isNull(_cursorIndexOfBearing)) {
              _tmpBearing = null;
            } else {
              _tmpBearing = _cursor.getFloat(_cursorIndexOfBearing);
            }
            final Float _tmpHorizontalAccuracy;
            if (_cursor.isNull(_cursorIndexOfHorizontalAccuracy)) {
              _tmpHorizontalAccuracy = null;
            } else {
              _tmpHorizontalAccuracy = _cursor.getFloat(_cursorIndexOfHorizontalAccuracy);
            }
            final Float _tmpVerticalAccuracy;
            if (_cursor.isNull(_cursorIndexOfVerticalAccuracy)) {
              _tmpVerticalAccuracy = null;
            } else {
              _tmpVerticalAccuracy = _cursor.getFloat(_cursorIndexOfVerticalAccuracy);
            }
            final String _tmpProvider;
            _tmpProvider = _cursor.getString(_cursorIndexOfProvider);
            final String _tmpTransportMode;
            if (_cursor.isNull(_cursorIndexOfTransportMode)) {
              _tmpTransportMode = null;
            } else {
              _tmpTransportMode = _cursor.getString(_cursorIndexOfTransportMode);
            }
            final boolean _tmpIsInterpolated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsInterpolated);
            _tmpIsInterpolated = _tmp != 0;
            final Integer _tmpBatteryPercent;
            if (_cursor.isNull(_cursorIndexOfBatteryPercent)) {
              _tmpBatteryPercent = null;
            } else {
              _tmpBatteryPercent = _cursor.getInt(_cursorIndexOfBatteryPercent);
            }
            final Float _tmpAccelerometerMagnitude;
            if (_cursor.isNull(_cursorIndexOfAccelerometerMagnitude)) {
              _tmpAccelerometerMagnitude = null;
            } else {
              _tmpAccelerometerMagnitude = _cursor.getFloat(_cursorIndexOfAccelerometerMagnitude);
            }
            final int _tmpSegmentIndex;
            _tmpSegmentIndex = _cursor.getInt(_cursorIndexOfSegmentIndex);
            _item = new TrackPointEntity(_tmpId,_tmpTripId,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpAltitude,_tmpSpeed,_tmpBearing,_tmpHorizontalAccuracy,_tmpVerticalAccuracy,_tmpProvider,_tmpTransportMode,_tmpIsInterpolated,_tmpBatteryPercent,_tmpAccelerometerMagnitude,_tmpSegmentIndex);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getLastTrackPoint(final String tripId,
      final Continuation<? super TrackPointEntity> $completion) {
    final String _sql = "SELECT * FROM track_points WHERE tripId = ? ORDER BY timestamp DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, tripId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TrackPointEntity>() {
      @Override
      @Nullable
      public TrackPointEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfAltitude = CursorUtil.getColumnIndexOrThrow(_cursor, "altitude");
          final int _cursorIndexOfSpeed = CursorUtil.getColumnIndexOrThrow(_cursor, "speed");
          final int _cursorIndexOfBearing = CursorUtil.getColumnIndexOrThrow(_cursor, "bearing");
          final int _cursorIndexOfHorizontalAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "horizontalAccuracy");
          final int _cursorIndexOfVerticalAccuracy = CursorUtil.getColumnIndexOrThrow(_cursor, "verticalAccuracy");
          final int _cursorIndexOfProvider = CursorUtil.getColumnIndexOrThrow(_cursor, "provider");
          final int _cursorIndexOfTransportMode = CursorUtil.getColumnIndexOrThrow(_cursor, "transportMode");
          final int _cursorIndexOfIsInterpolated = CursorUtil.getColumnIndexOrThrow(_cursor, "isInterpolated");
          final int _cursorIndexOfBatteryPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "batteryPercent");
          final int _cursorIndexOfAccelerometerMagnitude = CursorUtil.getColumnIndexOrThrow(_cursor, "accelerometerMagnitude");
          final int _cursorIndexOfSegmentIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "segmentIndex");
          final TrackPointEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTripId;
            _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final double _tmpLatitude;
            _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            final double _tmpLongitude;
            _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            final Double _tmpAltitude;
            if (_cursor.isNull(_cursorIndexOfAltitude)) {
              _tmpAltitude = null;
            } else {
              _tmpAltitude = _cursor.getDouble(_cursorIndexOfAltitude);
            }
            final Float _tmpSpeed;
            if (_cursor.isNull(_cursorIndexOfSpeed)) {
              _tmpSpeed = null;
            } else {
              _tmpSpeed = _cursor.getFloat(_cursorIndexOfSpeed);
            }
            final Float _tmpBearing;
            if (_cursor.isNull(_cursorIndexOfBearing)) {
              _tmpBearing = null;
            } else {
              _tmpBearing = _cursor.getFloat(_cursorIndexOfBearing);
            }
            final Float _tmpHorizontalAccuracy;
            if (_cursor.isNull(_cursorIndexOfHorizontalAccuracy)) {
              _tmpHorizontalAccuracy = null;
            } else {
              _tmpHorizontalAccuracy = _cursor.getFloat(_cursorIndexOfHorizontalAccuracy);
            }
            final Float _tmpVerticalAccuracy;
            if (_cursor.isNull(_cursorIndexOfVerticalAccuracy)) {
              _tmpVerticalAccuracy = null;
            } else {
              _tmpVerticalAccuracy = _cursor.getFloat(_cursorIndexOfVerticalAccuracy);
            }
            final String _tmpProvider;
            _tmpProvider = _cursor.getString(_cursorIndexOfProvider);
            final String _tmpTransportMode;
            if (_cursor.isNull(_cursorIndexOfTransportMode)) {
              _tmpTransportMode = null;
            } else {
              _tmpTransportMode = _cursor.getString(_cursorIndexOfTransportMode);
            }
            final boolean _tmpIsInterpolated;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsInterpolated);
            _tmpIsInterpolated = _tmp != 0;
            final Integer _tmpBatteryPercent;
            if (_cursor.isNull(_cursorIndexOfBatteryPercent)) {
              _tmpBatteryPercent = null;
            } else {
              _tmpBatteryPercent = _cursor.getInt(_cursorIndexOfBatteryPercent);
            }
            final Float _tmpAccelerometerMagnitude;
            if (_cursor.isNull(_cursorIndexOfAccelerometerMagnitude)) {
              _tmpAccelerometerMagnitude = null;
            } else {
              _tmpAccelerometerMagnitude = _cursor.getFloat(_cursorIndexOfAccelerometerMagnitude);
            }
            final int _tmpSegmentIndex;
            _tmpSegmentIndex = _cursor.getInt(_cursorIndexOfSegmentIndex);
            _result = new TrackPointEntity(_tmpId,_tmpTripId,_tmpTimestamp,_tmpLatitude,_tmpLongitude,_tmpAltitude,_tmpSpeed,_tmpBearing,_tmpHorizontalAccuracy,_tmpVerticalAccuracy,_tmpProvider,_tmpTransportMode,_tmpIsInterpolated,_tmpBatteryPercent,_tmpAccelerometerMagnitude,_tmpSegmentIndex);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPointCount(final String tripId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM track_points WHERE tripId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, tripId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getBoundsForTrip(final String tripId,
      final Continuation<? super TripBounds> $completion) {
    final String _sql = "\n"
            + "        SELECT MIN(latitude) as minLat, MAX(latitude) as maxLat,\n"
            + "               MIN(longitude) as minLon, MAX(longitude) as maxLon\n"
            + "        FROM track_points WHERE tripId = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, tripId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TripBounds>() {
      @Override
      @Nullable
      public TripBounds call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMinLat = 0;
          final int _cursorIndexOfMaxLat = 1;
          final int _cursorIndexOfMinLon = 2;
          final int _cursorIndexOfMaxLon = 3;
          final TripBounds _result;
          if (_cursor.moveToFirst()) {
            final Double _tmpMinLat;
            if (_cursor.isNull(_cursorIndexOfMinLat)) {
              _tmpMinLat = null;
            } else {
              _tmpMinLat = _cursor.getDouble(_cursorIndexOfMinLat);
            }
            final Double _tmpMaxLat;
            if (_cursor.isNull(_cursorIndexOfMaxLat)) {
              _tmpMaxLat = null;
            } else {
              _tmpMaxLat = _cursor.getDouble(_cursorIndexOfMaxLat);
            }
            final Double _tmpMinLon;
            if (_cursor.isNull(_cursorIndexOfMinLon)) {
              _tmpMinLon = null;
            } else {
              _tmpMinLon = _cursor.getDouble(_cursorIndexOfMinLon);
            }
            final Double _tmpMaxLon;
            if (_cursor.isNull(_cursorIndexOfMaxLon)) {
              _tmpMaxLon = null;
            } else {
              _tmpMaxLon = _cursor.getDouble(_cursorIndexOfMaxLon);
            }
            _result = new TripBounds(_tmpMinLat,_tmpMaxLat,_tmpMinLon,_tmpMaxLon);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
