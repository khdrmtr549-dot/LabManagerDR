package com.fieldlab.labmanager.ui.screens

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fieldlab.labmanager.data.local.entity.LaboratoryResult
import com.fieldlab.labmanager.ui.components.TopBar
import com.fieldlab.labmanager.viewmodel.LabDataViewModel

@Composable fun ResultsScreen(vm: LabDataViewModel, onEdit: (LaboratoryResult) -> Unit) {
    val results by vm.results.collectAsStateWithLifecycle()
    Scaffold(topBar = { TopBar("النتائج") }) { pad ->
        if (results.isEmpty()) Box(Modifier.padding(pad).fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) { Text("لا توجد نتائج بعد") }
        else LazyColumn(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(results, key = { it.id }) { r ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp)) {
                        Row(Modifier.fillMaxWidth()) { Text(r.testName, style = MaterialTheme.typography.titleMedium, Modifier.weight(1f)); AssistChip(onClick={}, label={Text(r.resultStatus)}) }
                        Spacer(Modifier.height(4.dp))
                        Text("النتيجة: ${r.resultValue} ${r.unit}".trim())
                        Text("المجال المرجعي: ${r.referenceRange}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("التاريخ: ${SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date(r.dateTime))}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (r.notes.isNotBlank()) Text("ملاحظات: ${r.notes}")
                        TextButton(onClick={onEdit(r)}) { Text("تعديل") }
                    }
                }
            }
        }
    }
}
