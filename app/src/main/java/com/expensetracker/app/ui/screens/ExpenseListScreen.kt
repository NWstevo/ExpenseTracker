package com.expensetracker.app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expensetracker.app.ui.components.ExpenseListItem
import com.expensetracker.app.viewmodel.ExpenseViewModel

@Composable
fun ExpenseListScreen(viewModel: ExpenseViewModel) {
    val expenses by viewModel.allExpenses.collectAsState()

    if (expenses.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No expenses yet.", style = MaterialTheme.typography.bodyLarge)
        }
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)) {
        items(expenses, key = { it.id }) { expense ->
            ExpenseListItem(expense = expense, onDelete = { viewModel.deleteExpense(expense) })
        }
    }
}
