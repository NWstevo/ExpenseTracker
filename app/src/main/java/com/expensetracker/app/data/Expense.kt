package com.expensetracker.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The categories a user can file an expense under. Kept as an enum (rather
 * than a free-text string) so totals can be grouped reliably in SQL queries.
 */
enum class ExpenseCategory {
    FOOD, TRANSPORT, HOUSING, UTILITIES, ENTERTAINMENT, HEALTH, EDUCATION, SHOPPING, OTHER
}

/**
 * One row in the "expenses" table = one recorded purchase.
 *
 * @param photoPath absolute path to a receipt photo on-device, or null if
 *   the expense was entered manually without a photo.
 */
@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val category: ExpenseCategory,
    val note: String,
    val dateMillis: Long,
    val photoPath: String? = null
)
