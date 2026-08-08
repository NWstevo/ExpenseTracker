package com.expensetracker.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.expensetracker.app.ui.theme.AmberWarning
import com.expensetracker.app.ui.theme.GreenPrimary
import com.expensetracker.app.ui.theme.OrangeOverLimit
import com.expensetracker.app.ui.theme.RedOverdraft
import com.expensetracker.app.util.BudgetLevel
import com.expensetracker.app.util.BudgetStatus
import java.util.Locale

fun BudgetLevel.color(): Color = when (this) {
    BudgetLevel.SAFE -> GreenPrimary
    BudgetLevel.WARNING -> AmberWarning
    BudgetLevel.OVER_LIMIT -> OrangeOverLimit
    BudgetLevel.OVERDRAFT_EXCEEDED -> RedOverdraft
}

fun BudgetLevel.message(): String = when (this) {
    BudgetLevel.SAFE -> "On track"
    BudgetLevel.WARNING -> "Approaching your limit"
    BudgetLevel.OVER_LIMIT -> "Over budget — dipping into overdraft allowance"
    BudgetLevel.OVERDRAFT_EXCEEDED -> "Overdraft allowance exceeded"
}

private fun money(v: Double) = "$" + String.format(Locale.US, "%.2f", v)

@Composable
fun BudgetProgressCard(title: String, status: BudgetStatus, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Icon(
                    imageVector = if (status.level == BudgetLevel.SAFE) Icons.Filled.CheckCircle else Icons.Filled.Warning,
                    contentDescription = null,
                    tint = status.level.color()
                )
            }

            if (status.limit <= 0.0) {
                Text(
                    "No budget set yet — spent ${money(status.spent)}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                LinearProgressIndicator(
                    progress = { status.percentOfLimit.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    color = status.level.color(),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${money(status.spent)} of ${money(status.limit)}", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "${(status.percentOfLimit * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Text(
                    status.level.message(),
                    style = MaterialTheme.typography.labelLarge,
                    color = status.level.color(),
                    modifier = Modifier.padding(top = 6.dp)
                )

                if (status.overdraftAllowance > 0.0) {
                    Text(
                        if (status.remaining >= 0)
                            "${money(status.remaining)} left before overdraft allowance runs out"
                        else
                            "${money(-status.remaining)} past overdraft allowance",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
