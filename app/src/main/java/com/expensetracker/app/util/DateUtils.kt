package com.expensetracker.app.util

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.temporal.TemporalAdjusters

/**
 * All the "which millisecond range counts as this week / this month" math
 * lives here so the rest of the app never has to think about calendars.
 * Weeks are treated as Monday -> Sunday.
 */
object DateUtils {
    private val zone: ZoneId = ZoneId.systemDefault()

    fun startOfWeekMillis(reference: LocalDate = LocalDate.now()): Long =
        reference.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .atStartOfDay(zone).toInstant().toEpochMilli()

    fun endOfWeekMillis(reference: LocalDate = LocalDate.now()): Long =
        reference.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
            .atTime(LocalTime.MAX).atZone(zone).toInstant().toEpochMilli()

    fun startOfMonthMillis(reference: LocalDate = LocalDate.now()): Long =
        reference.with(TemporalAdjusters.firstDayOfMonth())
            .atStartOfDay(zone).toInstant().toEpochMilli()

    fun endOfMonthMillis(reference: LocalDate = LocalDate.now()): Long =
        reference.with(TemporalAdjusters.lastDayOfMonth())
            .atTime(LocalTime.MAX).atZone(zone).toInstant().toEpochMilli()

    fun formatDate(millis: Long): String =
        Instant.ofEpochMilli(millis).atZone(zone).toLocalDate().toString()

    fun formatDateTime(millis: Long): String {
        val dt = Instant.ofEpochMilli(millis).atZone(zone).toLocalDateTime()
        return "%04d-%02d-%02d %02d:%02d".format(
            dt.year, dt.monthValue, dt.dayOfMonth, dt.hour, dt.minute
        )
    }

    fun nowMillis(): Long = System.currentTimeMillis()

    // Material3's DatePicker reports the selected day as UTC midnight
    // regardless of device time zone. Re-anchor that day to the current
    // time-of-day in the device's zone so it sorts/behaves like any other
    // expense timestamp.
    fun fromPickerUtcMillis(pickerUtcMillis: Long): Long =
        Instant.ofEpochMilli(pickerUtcMillis).atZone(ZoneOffset.UTC).toLocalDate()
            .atTime(LocalTime.now()).atZone(zone).toInstant().toEpochMilli()
}
