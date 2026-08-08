package com.expensetracker.app.ui.screens

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.expensetracker.app.camera.PhotoUtils
import com.expensetracker.app.data.ExpenseCategory
import com.expensetracker.app.viewmodel.ExpenseViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(viewModel: ExpenseViewModel, onSaved: () -> Unit) {
    val context = LocalContext.current

    var amountText by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf(ExpenseCategory.OTHER) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    var pendingPhotoFile by remember { mutableStateOf<File?>(null) }
    var savedPhotoPath by remember { mutableStateOf<String?>(null) }

    // Launches the system Camera app; it writes the photo to the content
    // Uri we hand it, then reports back true/false on whether it succeeded.
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        savedPhotoPath = if (success) pendingPhotoFile?.absolutePath else null
    }

    val amount = amountText.toDoubleOrNull()
    val isValid = amount != null && amount > 0.0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Add Expense", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text("Amount") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = amountText.isNotEmpty() && !isValid
        )

        ExposedDropdownMenuBox(
            expanded = categoryMenuExpanded,
            onExpandedChange = { categoryMenuExpanded = it }
        ) {
            OutlinedTextField(
                value = category.name.lowercase().replaceFirstChar { it.uppercase() },
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryMenuExpanded) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = categoryMenuExpanded,
                onDismissRequest = { categoryMenuExpanded = false }
            ) {
                ExpenseCategory.values().forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.name.lowercase().replaceFirstChar { it.uppercase() }) },
                        onClick = {
                            category = option
                            categoryMenuExpanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Note (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedButton(
            onClick = {
                val file = PhotoUtils.createImageFile(context)
                pendingPhotoFile = file
                val uri: Uri = PhotoUtils.getUriForFile(context, file)
                takePictureLauncher.launch(uri)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            androidx.compose.material3.Icon(Icons.Filled.CameraAlt, contentDescription = null)
            Text("  " + if (savedPhotoPath == null) "Attach receipt photo" else "Retake receipt photo")
        }

        savedPhotoPath?.let { path ->
            val bitmap = remember(path) { BitmapFactory.decodeFile(path) }
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Receipt photo preview",
                    modifier = Modifier.fillMaxWidth().height(180.dp)
                )
            }
        }

        Button(
            onClick = {
                viewModel.addExpense(
                    amount = amount!!,
                    category = category,
                    note = note.trim(),
                    photoPath = savedPhotoPath
                )
                onSaved()
            },
            enabled = isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Expense")
        }
    }
}
