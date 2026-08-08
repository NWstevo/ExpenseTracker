package com.expensetracker.app.util

/**
 * The four states a budget period (week or month) can be in, ordered from
 * best to worst. The UI maps each level to a color.
 */
enum class BudgetLevel {
    SAFE,               // comfortably under the warning threshold
    WARNING,            // past the warning threshold, still under the limit
    OVER_LIMIT,         // past the limit, but still inside the overdraft allowance
    OVERDRAFT_EXCEEDED  // past limit + overdraft allowance entirely
}

/**
 * Computes where a given amount of spend sits relative to a budget limit,
 * a warning threshold (%), and an overdraft allowance (absolute amount).
 *
 * Example: limit=500, overdraftAllowance=100, warningThresholdPercent=80
 *   spent=350 -> SAFE       (70% of limit)
 *   spent=420 -> WARNING    (84% of limit, past the 80% threshold)
 *   spent=550 -> OVER_LIMIT (past 500, but within the 600 overdraft ceiling)
 *   spent=650 -> OVERDRAFT_EXCEEDED (past the 600 ceiling)
 */
data class BudgetStatus(
    val spent: Double,
    val limit: Double,
    val warningThresholdPercent: Int,
    val overdraftAllowance: Double
) {
    /** 1.0 == exactly at the limit. Used to drive a progress bar. */
    val percentOfLimit: Float =
        if (limit <= 0.0) 0f else (spent / limit).toFloat()

    val level: BudgetLevel = when {
        limit <= 0.0 -> BudgetLevel.SAFE
        spent > limit + overdraftAllowance -> BudgetLevel.OVERDRAFT_EXCEEDED
        spent > limit -> BudgetLevel.OVER_LIMIT
        spent >= limit * (warningThresholdPercent / 100.0) -> BudgetLevel.WARNING
        else -> BudgetLevel.SAFE
    }

    /** How much room is left before hitting limit + overdraft allowance. Negative once exceeded. */
    val remaining: Double = (limit + overdraftAllowance) - spent
}
