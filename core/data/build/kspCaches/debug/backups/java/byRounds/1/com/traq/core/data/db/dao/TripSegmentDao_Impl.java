package com.traq.core.data.db.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.traq.core.data.db.entity.TripSegmentEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class TripSegmentDao_Impl implements TripSegmentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TripSegmentEntity> __insertionAdapterOfTripSegmentEntity;

  private final EntityDeletionOrUpdateAdapter<TripSegmentEntity> __updateAdapterOfTripSegmentEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteForTrip;

  public TripSegmentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTripSegmentEntity = new EntityInsertionAdapter<TripSegmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `trip_segments` (`id`,`tripId`,`segmentIndex`,`startTime`,`endTime`,`transportMode`,`distanceMeters`,`reason`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TripSegmentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTripId());
        statement.bindLong(3, entity.getSegmentIndex());
        statement.bindLong(4, entity.getStartTime());
        if (entity.getEndTime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getEndTime());
        }
        if (entity.getTransportMode() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getTransportMode());
        }
        statement.bindDouble(7, entity.getDistanceMeters());
        statement.bindString(8, entity.getReason());
      }
    };
    this.__updateAdapterOfTripSegmentEntity = new EntityDeletionOrUpdateAdapter<TripSegmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `trip_segments` SET `id` = ?,`tripId` = ?,`segmentIndex` = ?,`startTime` = ?,`endTime` = ?,`transportMode` = ?,`distanceMeters` = ?,`reason` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TripSegmentEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTripId());
        statement.bindLong(3, entity.getSegmentIndex());
        statement.bindLong(4, entity.getStartTime());
        if (entity.getEndTime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getEndTime());
        }
        if (entity.getTransportMode() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getTransportMode());
        }
        statement.bindDouble(7, entity.getDistanceMeters());
        statement.bindString(8, entity.getReason());
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteForTrip = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM trip_segments WHERE tripId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final TripSegmentEntity segment,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTripSegmentEntity.insert(segment);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final TripSegmentEntity segment,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTripSegmentEntity.handle(segment);
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
  public Flow<List<TripSegmentEntity>> getSegmentsForTrip(final String tripId) {
    final String _sql = "SELECT * FROM trip_segments WHERE tripId = ? ORDER BY segmentIndex ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, tripId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"trip_segments"}, new Callable<List<TripSegmentEntity>>() {
      @Override
      @NonNull
      public List<TripSegmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTripId = CursorUtil.getColumnIndexOrThrow(_cursor, "tripId");
          final int _cursorIndexOfSegmentIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "segmentIndex");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfTransportMode = CursorUtil.getColumnIndexOrThrow(_cursor, "transportMode");
          final int _cursorIndexOfDistanceMeters = CursorUtil.getColumnIndexOrThrow(_cursor, "distanceMeters");
          final int _cursorIndexOfReason = CursorUtil.getColumnIndexOrThrow(_cursor, "reason");
          final List<TripSegmentEntity> _result = new ArrayList<TripSegmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TripSegmentEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTripId;
            _tmpTripId = _cursor.getString(_cursorIndexOfTripId);
            final int _tmpSegmentIndex;
            _tmpSegmentIndex = _cursor.getInt(_cursorIndexOfSegmentIndex);
            final long _tmpStartTime;
            _tmpStartTime = _cursor.getLong(_cursorIndexOfStartTime);
            final Long _tmpEndTime;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmpEndTime = null;
            } else {
              _tmpEndTime = _cursor.getLong(_cursorIndexOfEndTime);
            }
            final String _tmpTransportMode;
            if (_cursor.isNull(_cursorIndexOfTransportMode)) {
              _tmpTransportMode = null;
            } else {
              _tmpTransportMode = _cursor.getString(_cursorIndexOfTransportMode);
            }
            final double _tmpDistanceMeters;
            _tmpDistanceMeters = _cursor.getDouble(_cursorIndexOfDistanceMeters);
            final String _tmpReason;
            _tmpReason = _cursor.getString(_cursorIndexOfReason);
            _item = new TripSegmentEntity(_tmpId,_tmpTripId,_tmpSegmentIndex,_tmpStartTime,_tmpEndTime,_tmpTransportMode,_tmpDistanceMeters,_tmpReason);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
