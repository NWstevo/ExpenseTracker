package com.expensetracker.app.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.expensetracker.app.data.Expense
import com.expensetracker.app.util.DateUtils
import java.util.Locale

private val dateWeight = 1f
private val categoryWeight = 1.1f
private val amountWeight = 0.9f

private fun money(v: Double) = "$" + String.format(Locale.US, "%.2f", v)

private fun displayName(name: String) = name.lowercase().replaceFirstChar { it.uppercase() }

@Composable
fun ExpenseTableHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Text("Date", modifier = Modifier.weight(dateWeight), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        Text("Category", modifier = Modifier.weight(categoryWeight), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        Text(
            "Amount",
            modifier = Modifier.weight(amountWeight),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.size(40.dp))
    }
    HorizontalDivider()
}

@Composable
fun ExpenseTableRow(expense: Expense, onDelete: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            DateUtils.formatDate(expense.dateMillis),
            modifier = Modifier.weight(dateWeight),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(modifier = Modifier.weight(categoryWeight), verticalAlignment = Alignment.CenterVertically) {
            Text(
                displayName(expense.category.name),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (expense.photoPath != null) {
                Icon(
                    Icons.Filled.PhotoCamera,
                    contentDescription = "Has receipt photo",
                    modifier = Modifier.padding(start = 4.dp).size(16.dp)
                )
            }
        }
        Text(
            money(expense.amount),
            modifier = Modifier.weight(amountWeight),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.End
        )
        IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete expense", modifier = Modifier.size(18.dp))
        }
    }
    HorizontalDivider()
}
