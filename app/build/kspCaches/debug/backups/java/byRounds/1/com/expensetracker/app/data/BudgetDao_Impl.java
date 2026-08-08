package com.expensetracker.app.data;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BudgetDao_Impl implements BudgetDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Budget> __insertionAdapterOfBudget;

  public BudgetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBudget = new EntityInsertionAdapter<Budget>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `budget` (`id`,`weeklyLimit`,`monthlyLimit`,`warningThresholdPercent`,`overdraftAllowance`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Budget entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getWeeklyLimit());
        statement.bindDouble(3, entity.getMonthlyLimit());
        statement.bindLong(4, entity.getWarningThresholdPercent());
        statement.bindDouble(5, entity.getOverdraftAllowance());
      }
    };
  }

  @Override
  public Object upsert(final Budget budget, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBudget.insert(budget);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Budget> getBudget() {
    final String _sql = "SELECT * FROM budget WHERE id = 0 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"budget"}, new Callable<Budget>() {
      @Override
      @Nullable
      public Budget call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWeeklyLimit = CursorUtil.getColumnIndexOrThrow(_cursor, "weeklyLimit");
          final int _cursorIndexOfMonthlyLimit = CursorUtil.getColumnIndexOrThrow(_cursor, "monthlyLimit");
          final int _cursorIndexOfWarningThresholdPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "warningThresholdPercent");
          final int _cursorIndexOfOverdraftAllowance = CursorUtil.getColumnIndexOrThrow(_cursor, "overdraftAllowance");
          final Budget _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final double _tmpWeeklyLimit;
            _tmpWeeklyLimit = _cursor.getDouble(_cursorIndexOfWeeklyLimit);
            final double _tmpMonthlyLimit;
            _tmpMonthlyLimit = _cursor.getDouble(_cursorIndexOfMonthlyLimit);
            final int _tmpWarningThresholdPercent;
            _tmpWarningThresholdPercent = _cursor.getInt(_cursorIndexOfWarningThresholdPercent);
            final double _tmpOverdraftAllowance;
            _tmpOverdraftAllowance = _cursor.getDouble(_cursorIndexOfOverdraftAllowance);
            _result = new Budget(_tmpId,_tmpWeeklyLimit,_tmpMonthlyLimit,_tmpWarningThresholdPercent,_tmpOverdraftAllowance);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
