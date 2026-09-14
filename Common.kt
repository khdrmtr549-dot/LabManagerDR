package com.fieldlab.labmanager.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable fun SectionCard(title: String, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Card(modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) { Column(Modifier.padding(16.dp)) { Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold); Spacer(Modifier.height(10.dp)); content() } }
}

@Composable fun EmptyState(text: String, modifier: Modifier = Modifier) { Box(modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) { Text(text, color = MaterialTheme.colorScheme.onSurfaceVariant) } }

@Composable fun SearchField(value: String, onValueChange: (String) -> Unit, placeholder: String) { OutlinedTextField(value, onValueChange, modifier = Modifier.fillMaxWidth(), singleLine = true, leadingIcon = { Icon(Icons.Default.Search, null) }, placeholder = { Text(placeholder) }) }

@Composable fun ConfirmDeleteDialog(title: String, text: String, onConfirm: () -> Unit, onDismiss: () -> Unit) { AlertDialog(onDismissRequest = onDismiss, title = { Text(title) }, text = { Text(text) }, confirmButton = { TextButton(onClick = onConfirm) { Text("حذف") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("إلغاء") } }) }

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun <T> DropdownField(label: String, value: String, options: List<T>, labelOf: (T) -> String, onSelected: (T) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(value, { }, label = { Text(label) }, readOnly = true, modifier = Modifier.menuAnchor().fillMaxWidth(), trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) })
        ExposedDropdownMenu(expanded, onDismissRequest = { expanded = false }) { options.forEach { option -> DropdownMenuItem(text = { Text(labelOf(option)) }, onClick = { onSelected(option); expanded = false }) } }
    }
}

@Composable fun TopBar(title: String, onBack: (() -> Unit)? = null) { TopAppBar(title = { Text(title) }, navigationIcon = { if (onBack != null) IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "رجوع") } }) }
