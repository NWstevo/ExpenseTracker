package com.expensetracker.app.notification

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

private const val UNIQUE_WORK_NAME = "daily_expense_reminder"
private val REMINDER_TIME: LocalTime = LocalTime.of(21, 0)

object ReminderScheduler {

    fun schedule(context: Context) {
        ReminderNotifier.ensureChannel(context)

        val initialDelay = delayUntilNextReminder()
        val request = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay.toMillis(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun delayUntilNextReminder(): Duration {
        val zone = ZoneId.systemDefault()
        val now = LocalDateTime.now(zone)
        var target = LocalDateTime.of(LocalDate.now(zone), REMINDER_TIME)
        if (!target.isAfter(now)) {
            target = target.plusDays(1)
        }
        return Duration.between(now, target)
    }
}
