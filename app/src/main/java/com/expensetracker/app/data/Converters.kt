package com.expensetracker.app.data

import androidx.room.TypeConverter

/** Tells Room how to store the ExpenseCategory enum as plain text in SQLite. */
class Converters {
    @TypeConverter
    fun fromCategory(category: ExpenseCategory): String = category.name

    @TypeConverter
    fun toCategory(value: String): ExpenseCategory = ExpenseCategory.valueOf(value)
}
