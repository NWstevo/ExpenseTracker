package com.expensetracker.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.expensetracker.app.data.Currency

@Composable
fun CurrencySelector(currency: Currency, onSelect: (Currency) -> Unit, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    TextButton(onClick = { expanded = true }, modifier = modifier) {
        Text(currency.symbol, style = MaterialTheme.typography.titleMedium)
        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Select currency", modifier = Modifier.padding(start = 2.dp))
    }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        Currency.values().forEach { option ->
            DropdownMenuItem(
                text = { Text("${option.symbol}  ${option.displayName}") },
                onClick = {
                    onSelect(option)
                    expanded = false
                }
            )
        }
    }
}
