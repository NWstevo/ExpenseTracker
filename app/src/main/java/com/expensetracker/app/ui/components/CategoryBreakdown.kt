package com.expensetracker.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.expensetracker.app.data.CategoryTotal
import com.expensetracker.app.ui.theme.GreenPrimary
import java.util.Locale

/** Simple horizontal-bar breakdown by category — no charting library needed. */
@Composable
fun CategoryBreakdown(totals: List<CategoryTotal>, modifier: Modifier = Modifier) {
    if (totals.isEmpty()) {
        Text("No expenses recorded yet for this period.", style = MaterialTheme.typography.bodyMedium, modifier = modifier)
        return
    }
    val max = totals.maxOf { it.total }.coerceAtLeast(0.01)
    Column(modifier = modifier.fillMaxWidth()) {
        totals.sortedByDescending { it.total }.forEach { ct ->
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text(
                    ct.category.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth((ct.total / max).toFloat().coerceIn(0.02f, 1f))
                    .height(10.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(GreenPrimary)
            )
            Text(
                "$" + String.format(Locale.US, "%.2f", ct.total),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}
