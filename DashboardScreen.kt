package com.fieldlab.labmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fieldlab.labmanager.ui.components.SectionCard
import com.fieldlab.labmanager.viewmodel.DashboardViewModel

@Composable fun DashboardScreen(vm: DashboardViewModel, onAddPatient: () -> Unit, onAddSample: () -> Unit, onPatients: () -> Unit, onSearch: () -> Unit) {
    val patients by vm.patientCount.collectAsStateWithLifecycle(0); val pending by vm.pendingSamples.collectAsStateWithLifecycle(0); val results by vm.completedResults.collectAsStateWithLifecycle(0)
    Scaffold(topBar = { TopAppBar(title = { Text("لوحة التحكم") }) }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("مختبر — نظام إدارة المختبر المحلي", style = MaterialTheme.typography.headlineSmall)
            Text("يعمل دون اتصال بالإنترنت ويحفظ البيانات محليًا على الجهاز.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) { StatCard("المرضى", patients.toString(), Modifier.weight(1f)); StatCard("عينات معلّقة", pending.toString(), Modifier.weight(1f)); StatCard("النتائج", results.toString(), Modifier.weight(1f)) }
            SectionCard("إجراءات سريعة") {
                Button(onClick = onAddPatient, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.PersonAdd, null); Spacer(Modifier.width(8.dp)); Text("إضافة مريض") }
                Spacer(Modifier.height(8.dp)); OutlinedButton(onClick = onAddSample, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.Science, null); Spacer(Modifier.width(8.dp)); Text("تسجيل عينة") }
                Spacer(Modifier.height(8.dp)); OutlinedButton(onClick = onPatients, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.People, null); Spacer(Modifier.width(8.dp)); Text("فتح سجل المرضى") }
                Spacer(Modifier.height(8.dp)); OutlinedButton(onClick = onSearch, modifier = Modifier.fillMaxWidth()) { Icon(Icons.Default.Search, null); Spacer(Modifier.width(8.dp)); Text("بحث شامل") }
            }
            SectionCard("حالة النظام") { Text("قاعدة البيانات: Room / SQLite"); Text("الوضع: Offline-first"); Text("المصدر الحقيقي للبيانات: قاعدة البيانات المحلية") }
        }
    }
}
@Composable private fun StatCard(label: String, value: String, modifier: Modifier) { Card(modifier) { Column(Modifier.padding(12.dp)) { Text(label, style = MaterialTheme.typography.labelMedium); Text(value, style = MaterialTheme.typography.headlineSmall) } } }
