package com.expensetracker.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Single-row table (id is always 0) holding the user's budget settings.
 *
 * - weeklyLimit / monthlyLimit: the target spending caps.
 * - warningThresholdPercent: once spend reaches this % of a limit, the UI
 *   switches to a "warning" state (e.g. 80 means warn at 80% spent).
 * - overdraftAllowance: how much *beyond* the limit the user is still
 *   willing to tolerate before it's flagged as a hard overdraft breach.
 *   Example: monthlyLimit=500, overdraftAllowance=100 means spending up to
 *   600 shows as "over budget" (still tracked), but anything past 600 is
 *   flagged as "overdraft exceeded".
 */
@Entity(tableName = "budget")
data class Budget(
    @PrimaryKey val id: Int = 0,
    val weeklyLimit: Double = 0.0,
    val monthlyLimit: Double = 0.0,
    val warningThresholdPercent: Int = 80,
    val overdraftAllowance: Double = 0.0
)
