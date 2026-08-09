package com.expensetracker.app.ui

import androidx.compose.runtime.compositionLocalOf
import com.expensetracker.app.data.Currency

val LocalCurrency = compositionLocalOf { Currency.USD }
