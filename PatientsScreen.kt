package com.fieldlab.labmanager.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fieldlab.labmanager.data.local.entity.Patient
import com.fieldlab.labmanager.ui.components.*
import com.fieldlab.labmanager.viewmodel.PatientsViewModel

@Composable fun PatientsScreen(vm: PatientsViewModel, onPatient: (Long) -> Unit, onEdit: (Long) -> Unit, onAdd: () -> Unit) {
    val patients by vm.patients.collectAsStateWithLifecycle(); val q by vm.searchQuery.collectAsStateWithLifecycle(); var deleting by remember { mutableStateOf<Patient?>(null) }
    Scaffold(topBar = { TopBar("المرضى") }, floatingActionButton = { FloatingActionButton(onClick = onAdd) { Icon(Icons.Default.Add, "إضافة") } }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) { SearchField(q, vm::setQuery, "ابحث بالاسم أو رقم المريض"); Spacer(Modifier.height(12.dp));
            if (patients.isEmpty()) EmptyState(if(q.isBlank()) "لا يوجد مرضى بعد" else "لا توجد نتائج مطابقة") else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(patients, key = { it.id }) { p -> Card(Modifier.fillMaxWidth().clickable { onPatient(p.id) }) { Row(Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) { Column(Modifier.weight(1f)) { Text(p.fullName, style = MaterialTheme.typography.titleMedium); Text("رقم: ${p.patientNumber} • ${p.gender}", color = MaterialTheme.colorScheme.onSurfaceVariant); if (p.phone.isNotBlank()) Text(p.phone) }; IconButton(onClick = { onEdit(p.id) }) { Icon(Icons.Default.Edit, "تعديل") }; IconButton(onClick = { deleting = p }) { Icon(Icons.Default.Delete, "حذف") } } } } }
        }
    }
    deleting?.let { p -> ConfirmDeleteDialog("حذف المريض", "سيتم حذف المريض وجميع عيناته ونتائجه المرتبطة به.", { vm.delete(p); deleting = null }, { deleting = null }) }
}
