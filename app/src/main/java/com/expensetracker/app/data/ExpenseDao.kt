package com.expensetracker.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/** Data Access Object: the only place raw SQL for expenses lives. */
@Dao
interface ExpenseDao {

    @Insert
    suspend fun insert(expense: Expense): Long

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY dateMillis DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE dateMillis BETWEEN :start AND :end ORDER BY dateMillis DESC")
    fun getExpensesBetween(start: Long, end: Long): Flow<List<Expense>>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM expenses WHERE dateMillis BETWEEN :start AND :end")
    fun getTotalBetween(start: Long, end: Long): Flow<Double>

    @Query(
        "SELECT category AS category, COALESCE(SUM(amount), 0.0) AS total " +
        "FROM expenses WHERE dateMillis BETWEEN :start AND :end GROUP BY category"
    )
    fun getCategoryTotalsBetween(start: Long, end: Long): Flow<List<CategoryTotal>>
}

/** Row shape returned by the GROUP BY category query above. */
data class CategoryTotal(
    val category: ExpenseCategory,
    val total: Double
)
