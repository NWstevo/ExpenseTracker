package com.expensetracker.app.data

import java.util.Locale

/**
 * The currencies a user can pick for displaying amounts throughout the app.
 * FCFA is conventionally written after the number (e.g. "1000 FCFA"), while
 * USD/EUR are written before it — `prefixed` controls that placement.
 */
enum class Currency(val code: String, val symbol: String, val displayName: String, val prefixed: Boolean) {
    USD("USD", "$", "US Dollar", true),
    EUR("EUR", "€", "Euro", true),
    FCFA("FCFA", "FCFA", "CFA Franc", false);

    fun format(amount: Double): String {
        val value = String.format(Locale.US, "%.2f", amount)
        return if (prefixed) "$symbol$value" else "$value $symbol"
    }

    companion object {
        fun fromCode(code: String?): Currency = values().find { it.code == code } ?: USD
    }
}
