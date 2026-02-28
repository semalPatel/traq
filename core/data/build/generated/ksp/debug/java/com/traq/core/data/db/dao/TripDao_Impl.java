package com.traq.core.data.db.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.traq.core.data.db.entity.TripEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
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
public final class TripDao_Impl implements TripDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TripEntity> __insertionAdapterOfTripEntity;

  private final EntityDeletionOrUpdateAdapter<TripEntity> __updateAdapterOfTripEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public TripDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTripEntity = new EntityInsertionAdapter<TripEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `trips` (`id`,`name`,`startTime`,`endTime`,`status`,`dominantMode`,`totalDistanceMeters`,`totalDurationMs`,`movingDurationMs`,`avgSpeedMps`,`maxSpeedMps`,`totalAscentMeters`,`totalDescentMeters`,`startLatitude`,`startLongitude`,`endLatitude`,`endLongitude`,`batteryStartPercent`,`batteryEndPercent`,`pointCount`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TripEntity entity) {
        statement.bindString(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        statement.bindLong(3, entity.getStartTime());
        if (entity.getEndTime() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getEndTime());
        }
        statement.bindString(5, entity.getStatus());
        if (entity.getDominantMode() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDominantMode());
        }
        statement.bindDouble(7, entity.getTotalDistanceMeters());
        statement.bindLong(8, entity.getTotalDurationMs());
        statement.bindLong(9, entity.getMovingDurationMs());
        statement.bindDouble(10, entity.getAvgSpeedMps());
        statement.bindDouble(11, entity.getMaxSpeedMps());
        statement.bindDouble(12, entity.getTotalAscentMeters());
        statement.bindDouble(13, entity.getTotalDescentMeters());
        statement.bindDouble(14, entity.getStartLatitude());
        statement.bindDouble(15, entity.getStartLongitude());
        if (entity.getEndLatitude() == null) {
          statement.bindNull(16);
        } else {
          statement.bindDouble(16, entity.getEndLatitude());
        }
        if (entity.getEndLongitude() == null) {
          statement.bindNull(17);
        } else {
          statement.bindDouble(17, entity.getEndLongitude());
        }
        statement.bindLong(18, entity.getBatteryStartPercent());
        if (entity.getBatteryEndPercent() == null) {
          statement.bindNull(19);
        } else {
          statement.bindLong(19, entity.getBatteryEndPercent());
        }
        statement.bindLong(20, entity.getPointCount());
      }
    };
    this.__updateAdapterOfTripEntity = new EntityDeletionOrUpdateAdapter<TripEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `trips` SET `id` = ?,`name` = ?,`startTime` = ?,`endTime` = ?,`status` = ?,`dominantMode` = ?,`totalDistanceMeters` = ?,`totalDurationMs` = ?,`movingDurationMs` = ?,`avgSpeedMps` = ?,`maxSpeedMps` = ?,`totalAscentMeters` = ?,`totalDescentMeters` = ?,`startLatitude` = ?,`startLongitude` = ?,`endLatitude` = ?,`endLongitude` = ?,`batteryStartPercent` = ?,`batteryEndPercent` = ?,`pointCount` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TripEntity entity) {
        statement.bindString(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        statement.bindLong(3, entity.getStartTime());
        if (entity.getEndTime() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getEndTime());
        }
        statement.bindString(5, entity.getStatus());
        if (entity.getDominantMode() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getDominantMode());
        }
        statement.bindDouble(7, entity.getTotalDistanceMeters());
        statement.bindLong(8, entity.getTotalDurationMs());
        statement.bindLong(9, entity.getMovingDurationMs());
        statement.bindDouble(10, entity.getAvgSpeedMps());
        statement.bindDouble(11, entity.getMaxSpeedMps());
        statement.bindDouble(12, entity.getTotalAscentMeters());
        statement.bindDouble(13, entity.getTotalDescentMeters());
        statement.bindDouble(14, entity.getStartLatitude());
        statement.bindDouble(15, entity.getStartLongitude());
        if (entity.getEndLatitude() == null) {
          statement.bindNull(16);
        } else {
          statement.bindDouble(16, entity.getEndLatitude());
        }
        if (entity.getEndLongitude() == null) {
          statement.bindNull(17);
        } else {
          statement.bindDouble(17, entity.getEndLongitude());
        }
        statement.bindLong(18, entity.getBatteryStartPercent());
        if (entity.getBatteryEndPercent() == null) {
          statement.bindNull(19);
        } else {
          statement.bindLong(19, entity.getBatteryEndPercent());
        }
        statement.bindLong(20, entity.getPointCount());
        statement.bindString(21, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM trips WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final TripEntity trip, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTripEntity.insert(trip);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final TripEntity trip, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTripEntity.handle(trip);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
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
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TripEntity>> getAllTrips() {
    final String _sql = "SELECT * FROM trips ORDER BY startTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"trips"}, new Callable<List<TripEntity>>() {
      @Override
      @NonNull
      public List<TripEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDominantMode = CursorUtil.getColumnIndexOrThrow(_cursor, "dominantMode");
          final int _cursorIndexOfTotalDistanceMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalDistanceMeters");
          final int _cursorIndexOfTotalDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalDurationMs");
          final int _cursorIndexOfMovingDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "movingDurationMs");
          final int _cursorIndexOfAvgSpeedMps = CursorUtil.getColumnIndexOrThrow(_cursor, "avgSpeedMps");
          final int _cursorIndexOfMaxSpeedMps = CursorUtil.getColumnIndexOrThrow(_cursor, "maxSpeedMps");
          final int _cursorIndexOfTotalAscentMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalAscentMeters");
          final int _cursorIndexOfTotalDescentMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalDescentMeters");
          final int _cursorIndexOfStartLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "startLatitude");
          final int _cursorIndexOfStartLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "startLongitude");
          final int _cursorIndexOfEndLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "endLatitude");
          final int _cursorIndexOfEndLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "endLongitude");
          final int _cursorIndexOfBatteryStartPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "batteryStartPercent");
          final int _cursorIndexOfBatteryEndPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "batteryEndPercent");
          final int _cursorIndexOfPointCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pointCount");
          final List<TripEntity> _result = new ArrayList<TripEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TripEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpDominantMode;
            if (_cursor.isNull(_cursorIndexOfDominantMode)) {
              _tmpDominantMode = null;
            } else {
              _tmpDominantMode = _cursor.getString(_cursorIndexOfDominantMode);
            }
            final double _tmpTotalDistanceMeters;
            _tmpTotalDistanceMeters = _cursor.getDouble(_cursorIndexOfTotalDistanceMeters);
            final long _tmpTotalDurationMs;
            _tmpTotalDurationMs = _cursor.getLong(_cursorIndexOfTotalDurationMs);
            final long _tmpMovingDurationMs;
            _tmpMovingDurationMs = _cursor.getLong(_cursorIndexOfMovingDurationMs);
            final double _tmpAvgSpeedMps;
            _tmpAvgSpeedMps = _cursor.getDouble(_cursorIndexOfAvgSpeedMps);
            final double _tmpMaxSpeedMps;
            _tmpMaxSpeedMps = _cursor.getDouble(_cursorIndexOfMaxSpeedMps);
            final double _tmpTotalAscentMeters;
            _tmpTotalAscentMeters = _cursor.getDouble(_cursorIndexOfTotalAscentMeters);
            final double _tmpTotalDescentMeters;
            _tmpTotalDescentMeters = _cursor.getDouble(_cursorIndexOfTotalDescentMeters);
            final double _tmpStartLatitude;
            _tmpStartLatitude = _cursor.getDouble(_cursorIndexOfStartLatitude);
            final double _tmpStartLongitude;
            _tmpStartLongitude = _cursor.getDouble(_cursorIndexOfStartLongitude);
            final Double _tmpEndLatitude;
            if (_cursor.isNull(_cursorIndexOfEndLatitude)) {
              _tmpEndLatitude = null;
            } else {
              _tmpEndLatitude = _cursor.getDouble(_cursorIndexOfEndLatitude);
            }
            final Double _tmpEndLongitude;
            if (_cursor.isNull(_cursorIndexOfEndLongitude)) {
              _tmpEndLongitude = null;
            } else {
              _tmpEndLongitude = _cursor.getDouble(_cursorIndexOfEndLongitude);
            }
            final int _tmpBatteryStartPercent;
            _tmpBatteryStartPercent = _cursor.getInt(_cursorIndexOfBatteryStartPercent);
            final Integer _tmpBatteryEndPercent;
            if (_cursor.isNull(_cursorIndexOfBatteryEndPercent)) {
              _tmpBatteryEndPercent = null;
            } else {
              _tmpBatteryEndPercent = _cursor.getInt(_cursorIndexOfBatteryEndPercent);
            }
            final int _tmpPointCount;
            _tmpPointCount = _cursor.getInt(_cursorIndexOfPointCount);
            _item = new TripEntity(_tmpId,_tmpName,_tmpStartTime,_tmpEndTime,_tmpStatus,_tmpDominantMode,_tmpTotalDistanceMeters,_tmpTotalDurationMs,_tmpMovingDurationMs,_tmpAvgSpeedMps,_tmpMaxSpeedMps,_tmpTotalAscentMeters,_tmpTotalDescentMeters,_tmpStartLatitude,_tmpStartLongitude,_tmpEndLatitude,_tmpEndLongitude,_tmpBatteryStartPercent,_tmpBatteryEndPercent,_tmpPointCount);
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
  public Flow<TripEntity> getTripById(final String id) {
    final String _sql = "SELECT * FROM trips WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"trips"}, new Callable<TripEntity>() {
      @Override
      @Nullable
      public TripEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDominantMode = CursorUtil.getColumnIndexOrThrow(_cursor, "dominantMode");
          final int _cursorIndexOfTotalDistanceMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalDistanceMeters");
          final int _cursorIndexOfTotalDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalDurationMs");
          final int _cursorIndexOfMovingDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "movingDurationMs");
          final int _cursorIndexOfAvgSpeedMps = CursorUtil.getColumnIndexOrThrow(_cursor, "avgSpeedMps");
          final int _cursorIndexOfMaxSpeedMps = CursorUtil.getColumnIndexOrThrow(_cursor, "maxSpeedMps");
          final int _cursorIndexOfTotalAscentMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalAscentMeters");
          final int _cursorIndexOfTotalDescentMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalDescentMeters");
          final int _cursorIndexOfStartLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "startLatitude");
          final int _cursorIndexOfStartLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "startLongitude");
          final int _cursorIndexOfEndLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "endLatitude");
          final int _cursorIndexOfEndLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "endLongitude");
          final int _cursorIndexOfBatteryStartPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "batteryStartPercent");
          final int _cursorIndexOfBatteryEndPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "batteryEndPercent");
          final int _cursorIndexOfPointCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pointCount");
          final TripEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpDominantMode;
            if (_cursor.isNull(_cursorIndexOfDominantMode)) {
              _tmpDominantMode = null;
            } else {
              _tmpDominantMode = _cursor.getString(_cursorIndexOfDominantMode);
            }
            final double _tmpTotalDistanceMeters;
            _tmpTotalDistanceMeters = _cursor.getDouble(_cursorIndexOfTotalDistanceMeters);
            final long _tmpTotalDurationMs;
            _tmpTotalDurationMs = _cursor.getLong(_cursorIndexOfTotalDurationMs);
            final long _tmpMovingDurationMs;
            _tmpMovingDurationMs = _cursor.getLong(_cursorIndexOfMovingDurationMs);
            final double _tmpAvgSpeedMps;
            _tmpAvgSpeedMps = _cursor.getDouble(_cursorIndexOfAvgSpeedMps);
            final double _tmpMaxSpeedMps;
            _tmpMaxSpeedMps = _cursor.getDouble(_cursorIndexOfMaxSpeedMps);
            final double _tmpTotalAscentMeters;
            _tmpTotalAscentMeters = _cursor.getDouble(_cursorIndexOfTotalAscentMeters);
            final double _tmpTotalDescentMeters;
            _tmpTotalDescentMeters = _cursor.getDouble(_cursorIndexOfTotalDescentMeters);
            final double _tmpStartLatitude;
            _tmpStartLatitude = _cursor.getDouble(_cursorIndexOfStartLatitude);
            final double _tmpStartLongitude;
            _tmpStartLongitude = _cursor.getDouble(_cursorIndexOfStartLongitude);
            final Double _tmpEndLatitude;
            if (_cursor.isNull(_cursorIndexOfEndLatitude)) {
              _tmpEndLatitude = null;
            } else {
              _tmpEndLatitude = _cursor.getDouble(_cursorIndexOfEndLatitude);
            }
            final Double _tmpEndLongitude;
            if (_cursor.isNull(_cursorIndexOfEndLongitude)) {
              _tmpEndLongitude = null;
            } else {
              _tmpEndLongitude = _cursor.getDouble(_cursorIndexOfEndLongitude);
            }
            final int _tmpBatteryStartPercent;
            _tmpBatteryStartPercent = _cursor.getInt(_cursorIndexOfBatteryStartPercent);
            final Integer _tmpBatteryEndPercent;
            if (_cursor.isNull(_cursorIndexOfBatteryEndPercent)) {
              _tmpBatteryEndPercent = null;
            } else {
              _tmpBatteryEndPercent = _cursor.getInt(_cursorIndexOfBatteryEndPercent);
            }
            final int _tmpPointCount;
            _tmpPointCount = _cursor.getInt(_cursorIndexOfPointCount);
            _result = new TripEntity(_tmpId,_tmpName,_tmpStartTime,_tmpEndTime,_tmpStatus,_tmpDominantMode,_tmpTotalDistanceMeters,_tmpTotalDurationMs,_tmpMovingDurationMs,_tmpAvgSpeedMps,_tmpMaxSpeedMps,_tmpTotalAscentMeters,_tmpTotalDescentMeters,_tmpStartLatitude,_tmpStartLongitude,_tmpEndLatitude,_tmpEndLongitude,_tmpBatteryStartPercent,_tmpBatteryEndPercent,_tmpPointCount);
          } else {
            _result = null;
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
  public Flow<List<TripEntity>> getTripsByStatus(final String status) {
    final String _sql = "SELECT * FROM trips WHERE status = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, status);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"trips"}, new Callable<List<TripEntity>>() {
      @Override
      @NonNull
      public List<TripEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDominantMode = CursorUtil.getColumnIndexOrThrow(_cursor, "dominantMode");
          final int _cursorIndexOfTotalDistanceMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalDistanceMeters");
          final int _cursorIndexOfTotalDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "totalDurationMs");
          final int _cursorIndexOfMovingDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "movingDurationMs");
          final int _cursorIndexOfAvgSpeedMps = CursorUtil.getColumnIndexOrThrow(_cursor, "avgSpeedMps");
          final int _cursorIndexOfMaxSpeedMps = CursorUtil.getColumnIndexOrThrow(_cursor, "maxSpeedMps");
          final int _cursorIndexOfTotalAscentMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalAscentMeters");
          final int _cursorIndexOfTotalDescentMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalDescentMeters");
          final int _cursorIndexOfStartLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "startLatitude");
          final int _cursorIndexOfStartLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "startLongitude");
          final int _cursorIndexOfEndLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "endLatitude");
          final int _cursorIndexOfEndLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "endLongitude");
          final int _cursorIndexOfBatteryStartPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "batteryStartPercent");
          final int _cursorIndexOfBatteryEndPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "batteryEndPercent");
          final int _cursorIndexOfPointCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pointCount");
          final List<TripEntity> _result = new ArrayList<TripEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TripEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpDominantMode;
            if (_cursor.isNull(_cursorIndexOfDominantMode)) {
              _tmpDominantMode = null;
            } else {
              _tmpDominantMode = _cursor.getString(_cursorIndexOfDominantMode);
            }
            final double _tmpTotalDistanceMeters;
            _tmpTotalDistanceMeters = _cursor.getDouble(_cursorIndexOfTotalDistanceMeters);
            final long _tmpTotalDurationMs;
            _tmpTotalDurationMs = _cursor.getLong(_cursorIndexOfTotalDurationMs);
            final long _tmpMovingDurationMs;
            _tmpMovingDurationMs = _cursor.getLong(_cursorIndexOfMovingDurationMs);
            final double _tmpAvgSpeedMps;
            _tmpAvgSpeedMps = _cursor.getDouble(_cursorIndexOfAvgSpeedMps);
            final double _tmpMaxSpeedMps;
            _tmpMaxSpeedMps = _cursor.getDouble(_cursorIndexOfMaxSpeedMps);
            final double _tmpTotalAscentMeters;
            _tmpTotalAscentMeters = _cursor.getDouble(_cursorIndexOfTotalAscentMeters);
            final double _tmpTotalDescentMeters;
            _tmpTotalDescentMeters = _cursor.getDouble(_cursorIndexOfTotalDescentMeters);
            final double _tmpStartLatitude;
            _tmpStartLatitude = _cursor.getDouble(_cursorIndexOfStartLatitude);
            final double _tmpStartLongitude;
            _tmpStartLongitude = _cursor.getDouble(_cursorIndexOfStartLongitude);
            final Double _tmpEndLatitude;
            if (_cursor.isNull(_cursorIndexOfEndLatitude)) {
              _tmpEndLatitude = null;
            } else {
              _tmpEndLatitude = _cursor.getDouble(_cursorIndexOfEndLatitude);
            }
            final Double _tmpEndLongitude;
            if (_cursor.isNull(_cursorIndexOfEndLongitude)) {
              _tmpEndLongitude = null;
            } else {
              _tmpEndLongitude = _cursor.getDouble(_cursorIndexOfEndLongitude);
            }
            final int _tmpBatteryStartPercent;
            _tmpBatteryStartPercent = _cursor.getInt(_cursorIndexOfBatteryStartPercent);
            final Integer _tmpBatteryEndPercent;
            if (_cursor.isNull(_cursorIndexOfBatteryEndPercent)) {
              _tmpBatteryEndPercent = null;
            } else {
              _tmpBatteryEndPercent = _cursor.getInt(_cursorIndexOfBatteryEndPercent);
            }
            final int _tmpPointCount;
            _tmpPointCount = _cursor.getInt(_cursorIndexOfPointCount);
            _item = new TripEntity(_tmpId,_tmpName,_tmpStartTime,_tmpEndTime,_tmpStatus,_tmpDominantMode,_tmpTotalDistanceMeters,_tmpTotalDurationMs,_tmpMovingDurationMs,_tmpAvgSpeedMps,_tmpMaxSpeedMps,_tmpTotalAscentMeters,_tmpTotalDescentMeters,_tmpStartLatitude,_tmpStartLongitude,_tmpEndLatitude,_tmpEndLongitude,_tmpBatteryStartPercent,_tmpBatteryEndPercent,_tmpPointCount);
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
  public Object getTripCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM trips";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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
  public Object getAggregateStats(final long sinceTimestamp,
      final Continuation<? super AggregateStats> $completion) {
    final String _sql = "\n"
            + "        SELECT SUM(totalDistanceMeters) as totalDistance,\n"
            + "               SUM(totalDurationMs) as totalDuration,\n"
            + "               COUNT(*) as tripCount\n"
            + "        FROM trips\n"
            + "        WHERE startTime >= ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sinceTimestamp);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AggregateStats>() {
      @Override
      @Nullable
      public AggregateStats call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTotalDistance = 0;
          final int _cursorIndexOfTotalDuration = 1;
          final int _cursorIndexOfTripCount = 2;
          final AggregateStats _result;
          if (_cursor.moveToFirst()) {
            final Double _tmpTotalDistance;
            if (_cursor.isNull(_cursorIndexOfTotalDistance)) {
              _tmpTotalDistance = null;
            } else {
              _tmpTotalDistance = _cursor.getDouble(_cursorIndexOfTotalDistance);
            }
            final Long _tmpTotalDuration;
            if (_cursor.isNull(_cursorIndexOfTotalDuration)) {
              _tmpTotalDuration = null;
            } else {
              _tmpTotalDuration = _cursor.getLong(_cursorIndexOfTotalDuration);
            }
            final int _tmpTripCount;
            _tmpTripCount = _cursor.getInt(_cursorIndexOfTripCount);
            _result = new AggregateStats(_tmpTotalDistance,_tmpTotalDuration,_tmpTripCount);
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
