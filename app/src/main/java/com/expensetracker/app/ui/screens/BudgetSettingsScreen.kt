package com.expensetracker.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.expensetracker.app.ui.LocalCurrency
import com.expensetracker.app.ui.theme.GreenPrimary
import com.expensetracker.app.viewmodel.ExpenseViewModel
import kotlinx.coroutines.delay

@Composable
fun BudgetSettingsScreen(viewModel: ExpenseViewModel) {
    val budget by viewModel.budget.collectAsState()
    val currency = LocalCurrency.current

    var weekly by remember { mutableStateOf(budget.weeklyLimit.toString()) }
    var monthly by remember { mutableStateOf(budget.monthlyLimit.toString()) }
    var threshold by remember { mutableStateOf(budget.warningThresholdPercent.toString()) }
    var overdraft by remember { mutableStateOf(budget.overdraftAllowance.toString()) }

    // Once the real budget loads from the database, sync the text fields to it
    // (they start at "0.0" / defaults before the Flow emits its first value).
    var initialized by remember { mutableStateOf(false) }
    LaunchedEffect(budget) {
        if (!initialized) {
            weekly = if (budget.weeklyLimit > 0) budget.weeklyLimit.toString() else ""
            monthly = if (budget.monthlyLimit > 0) budget.monthlyLimit.toString() else ""
            threshold = budget.warningThresholdPercent.toString()
            overdraft = if (budget.overdraftAllowance > 0) budget.overdraftAllowance.toString() else ""
            initialized = true
        }
    }

    var justSaved by remember { mutableStateOf(false) }
    LaunchedEffect(justSaved) {
        if (justSaved) {
            delay(2000)
            justSaved = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Budget Settings", style = MaterialTheme.typography.titleLarge)
        Text(
            "Set spending caps for the week and month. The warning threshold decides " +
                "how early you get a heads-up; the overdraft allowance is how far past " +
                "the cap you're still willing to let spending go before it's flagged as exceeded.",
            style = MaterialTheme.typography.bodyMedium
        )

        OutlinedTextField(
            value = weekly,
            onValueChange = { weekly = it },
            label = { Text("Weekly budget") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            prefix = if (currency.prefixed) { { Text(currency.symbol) } } else null,
            suffix = if (!currency.prefixed) { { Text(currency.symbol) } } else null
        )

        OutlinedTextField(
            value = monthly,
            onValueChange = { monthly = it },
            label = { Text("Monthly budget") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            prefix = if (currency.prefixed) { { Text(currency.symbol) } } else null,
            suffix = if (!currency.prefixed) { { Text(currency.symbol) } } else null
        )

        OutlinedTextField(
            value = threshold,
            onValueChange = { threshold = it },
            label = { Text("Warning threshold (% of budget)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = overdraft,
            onValueChange = { overdraft = it },
            label = { Text("Overdraft allowance (amount past budget)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth(),
            prefix = if (currency.prefixed) { { Text(currency.symbol) } } else null,
            suffix = if (!currency.prefixed) { { Text(currency.symbol) } } else null
        )

        Button(
            onClick = {
                viewModel.saveBudget(
                    weeklyLimit = weekly.toDoubleOrNull() ?: 0.0,
                    monthlyLimit = monthly.toDoubleOrNull() ?: 0.0,
                    warningThresholdPercent = threshold.toIntOrNull() ?: 80,
                    overdraftAllowance = overdraft.toDoubleOrNull() ?: 0.0
                )
                justSaved = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Budget")
        }

        if (justSaved) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = GreenPrimary)
                Text("Budget saved", color = GreenPrimary, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
