package com.fieldlab.labmanager.ui.screens

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fieldlab.labmanager.data.local.entity.*
import com.fieldlab.labmanager.ui.components.*
import com.fieldlab.labmanager.viewmodel.PatientProfileViewModel

@Composable fun PatientProfileScreen(vm: PatientProfileViewModel, onBack: () -> Unit, onAddSample: (Long) -> Unit, onAddResult: (Long, Long) -> Unit, onEditResult: (LaboratoryResult) -> Unit) {
    val patient by vm.patient.collectAsStateWithLifecycle(); val samples by vm.samples.collectAsStateWithLifecycle(); val results by vm.results.collectAsStateWithLifecycle(); var deleting by remember { mutableStateOf<LaboratoryResult?>(null) }
    Scaffold(topBar = { TopBar("ملف المريض", onBack) }) { pad -> if(patient == null) EmptyState("جاري تحميل بيانات المريض…", Modifier.padding(pad)) else LazyColumn(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { SectionCard("معلومات المريض") { Text(patient!!.fullName, style = MaterialTheme.typography.titleLarge); Text("رقم المريض: ${patient!!.patientNumber}"); Text("الجنس: ${patient!!.gender}${patient!!.age?.let{" • العمر: $it"} ?: ""}"); if(patient!!.phone.isNotBlank()) Text("الهاتف: ${patient!!.phone}"); if(patient!!.notes.isNotBlank()) Text("الملاحظات: ${patient!!.notes}") } }
        item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) { Button(onClick={onAddSample(patient!!.id)},Modifier.weight(1f)){Text("إضافة عينة")}; OutlinedButton(onClick={ if(samples.isNotEmpty()) onAddResult(patient!!.id,samples.first().id) },Modifier.weight(1f),enabled=samples.isNotEmpty()){Text("إضافة نتيجة")}} }
        item { Text("العينات", style=MaterialTheme.typography.titleMedium) }
        if(samples.isEmpty()) item { EmptyState("لا توجد عينات") } else items(samples,key={it.id}) { s -> SectionCard("${s.sampleNumber} — ${s.sampleType}") { Text("الحالة: ${s.status}"); if(s.notes.isNotBlank()) Text(s.notes); TextButton(onClick={onAddResult(patient!!.id,s.id)}){Text("إضافة نتيجة لهذه العينة")}; samples.let { } } }
        item { Text("النتائج المخبرية", style=MaterialTheme.typography.titleMedium) }
        if(results.isEmpty()) item { EmptyState("لا توجد نتائج") } else items(results,key={it.id}) { r -> Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(14.dp)) { Row(Modifier.fillMaxWidth()){Text(r.testName,style=MaterialTheme.typography.titleMedium,Modifier.weight(1f)); AssistChip(onClick={},label={Text(r.resultStatus)})}; Text("النتيجة: ${r.resultValue} ${r.unit}".trim()); Text("المرجع: ${r.referenceRange}"); if(r.notes.isNotBlank()) Text("ملاحظات: ${r.notes}"); Row{TextButton(onClick={onEditResult(r)}){Text("تعديل")}; TextButton(onClick={deleting=r}){Text("حذف")}} } } }
    } }
    deleting?.let{r->ConfirmDeleteDialog("حذف النتيجة","هل تريد حذف هذه النتيجة؟",{vm.deleteResult(r);deleting=null},{deleting=null})}
}

private fun formatDateTime(value: Long): String = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date(value))
