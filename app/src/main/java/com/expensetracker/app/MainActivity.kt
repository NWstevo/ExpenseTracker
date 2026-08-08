package com.expensetracker.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.expensetracker.app.ui.navigation.ExpenseTrackerNavGraph
import com.expensetracker.app.ui.theme.ExpenseTrackerTheme
import com.expensetracker.app.viewmodel.ExpenseViewModel

class MainActivity : ComponentActivity() {

    // `by viewModels()` survives configuration changes (e.g. screen rotation)
    // so in-progress state isn't lost when the Activity is recreated.
    private val viewModel: ExpenseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseTrackerTheme {
                ExpenseTrackerNavGraph(viewModel = viewModel)
            }
        }
    }
}
