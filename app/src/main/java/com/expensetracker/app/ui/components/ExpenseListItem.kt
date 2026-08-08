package com.expensetracker.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expensetracker.app.data.Expense
import com.expensetracker.app.util.DateUtils
import java.util.Locale

@Composable
fun ExpenseListItem(expense: Expense, onDelete: () -> Unit) {
    ListItem(
        headlineContent = {
            Text("${expense.category.name.lowercase().replaceFirstChar { it.uppercase() }}")
        },
        supportingContent = {
            Column {
                if (expense.note.isNotBlank()) {
                    Text(expense.note, style = MaterialTheme.typography.bodyMedium)
                }
                Text(DateUtils.formatDateTime(expense.dateMillis), style = MaterialTheme.typography.bodyMedium)
            }
        },
        trailingContent = {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                if (expense.photoPath != null) {
                    Icon(Icons.Filled.PhotoCamera, contentDescription = "Has receipt photo")
                }
                Text(
                    "$" + String.format(Locale.US, "%.2f", expense.amount),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete expense")
                }
            }
        }
    )
}
