package com.expensetracker.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expensetracker.app.ui.components.BudgetProgressCard
import com.expensetracker.app.ui.components.CategoryBreakdown
import com.expensetracker.app.ui.components.ExpenseTableHeader
import com.expensetracker.app.ui.components.ExpenseTableRow
import com.expensetracker.app.viewmodel.ExpenseViewModel

@Composable
fun DashboardScreen(viewModel: ExpenseViewModel) {
    val weeklyTotal by viewModel.weeklyTotal.collectAsState()
    val monthlyTotal by viewModel.monthlyTotal.collectAsState()
    val budget by viewModel.budget.collectAsState()
    val monthlyCategoryTotals by viewModel.monthlyCategoryTotals.collectAsState()
    val allExpenses by viewModel.allExpenses.collectAsState()

    val weeklyStatus = viewModel.weeklyStatus(weeklyTotal, budget)
    val monthlyStatus = viewModel.monthlyStatus(monthlyTotal, budget)

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Text("This Week", style = MaterialTheme.typography.titleLarge) }
        item { BudgetProgressCard(title = "Weekly spending", status = weeklyStatus) }

        item { Text("This Month", style = MaterialTheme.typography.titleLarge) }
        item { BudgetProgressCard(title = "Monthly spending", status = monthlyStatus) }

        item { Text("Spending by category (this month)", style = MaterialTheme.typography.titleLarge) }
        item { CategoryBreakdown(totals = monthlyCategoryTotals) }

        item { Text("Recent expenses", style = MaterialTheme.typography.titleLarge) }
        if (allExpenses.isEmpty()) {
            item { Text("Nothing recorded yet. Tap + to add your first expense.") }
        } else {
            item { ExpenseTableHeader() }
            items(allExpenses.take(5), key = { it.id }) { expense ->
                ExpenseTableRow(expense = expense, onDelete = { viewModel.deleteExpense(expense) })
            }
        }
    }
}
