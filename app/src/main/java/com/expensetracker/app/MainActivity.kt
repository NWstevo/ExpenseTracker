package com.expensetracker.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.expensetracker.app.notification.ReminderScheduler
import com.expensetracker.app.ui.navigation.ExpenseTrackerNavGraph
import com.expensetracker.app.ui.theme.ExpenseTrackerTheme
import com.expensetracker.app.viewmodel.ExpenseViewModel

class MainActivity : ComponentActivity() {

    // `by viewModels()` survives configuration changes (e.g. screen rotation)
    // so in-progress state isn't lost when the Activity is recreated.
    private val viewModel: ExpenseViewModel by viewModels()

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { ReminderScheduler.schedule(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseTrackerTheme {
                ExpenseTrackerNavGraph(viewModel = viewModel)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            ReminderScheduler.schedule(this)
        }
    }
}
