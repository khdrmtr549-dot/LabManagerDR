package com.fieldlab.labmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fieldlab.labmanager.data.local.entity.*
import com.fieldlab.labmanager.ui.components.*

@Composable fun ResultFormScreen(patients: List<Patient>, samples: List<Sample>, tests: List<TestDefinition>, existing: LaboratoryResult?, preselectedPatientId: Long, preselectedSampleId: Long, onSave: (LaboratoryResult) -> Unit, onBack: () -> Unit) {
    val patientSamples = samples.filter { it.patientId == preselectedPatientId || existing?.patientId == it.patientId }; var sampleId by remember { mutableStateOf(existing?.sampleId ?: preselectedSampleId) }; var test by remember { mutableStateOf(tests.firstOrNull { it.testName == existing?.testName } ?: tests.firstOrNull()) }; var value by remember { mutableStateOf(existing?.resultValue ?: "") }; var unit by remember { mutableStateOf(existing?.unit ?: "") }; var reference by remember { mutableStateOf(existing?.referenceRange ?: "") }; var status by remember { mutableStateOf(existing?.resultStatus ?: "طبيعي") }; var notes by remember { mutableStateOf(existing?.notes ?: "") }; var error by remember { mutableStateOf("") }
    LaunchedEffect(tests) { if (test == null && tests.isNotEmpty()) test = tests.first() }
    LaunchedEffect(test) { if(test != null && existing == null){unit=test!!.unit;reference=test!!.referenceRange} }
    val sampleLabel = samples.firstOrNull{it.id==sampleId}?.sampleNumber ?: ""
    Scaffold(topBar={TopBar(if(existing==null)"إضافة نتيجة" else "تعديل نتيجة",onBack)}){pad->Column(Modifier.padding(pad).padding(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
        if(patientSamples.isEmpty()) EmptyState("لا توجد عينة مرتبطة") else { DropdownField("العينة",sampleLabel,patientSamples,{it.sampleNumber+" — "+it.sampleType},{sampleId=it.id}); DropdownField("الاختبار",test?.testName?:"",tests,{it.testName},{test=it}); OutlinedTextField(value,{value=it},Modifier.fillMaxWidth(),label={Text("النتيجة *")},singleLine=true); OutlinedTextField(unit,{unit=it},Modifier.fillMaxWidth(),label={Text("الوحدة")},singleLine=true); OutlinedTextField(reference,{reference=it},Modifier.fillMaxWidth(),label={Text("المجال المرجعي")},singleLine=true); DropdownField("حالة النتيجة",status,listOf("طبيعي","غير طبيعي","حرج","مقبول"),{it},{status=it}); OutlinedTextField(notes,{notes=it},Modifier.fillMaxWidth(),label={Text("ملاحظات")},minLines=3); if(error.isNotBlank()) Text(error,color=MaterialTheme.colorScheme.error); Button(onClick={ if(sampleId==0L||test==null||value.isBlank())error="العينة والاختبار والنتيجة مطلوبة" else onSave(LaboratoryResult(existing?.id?:0,preselectedPatientId,sampleId,test!!.testName,value.trim(),unit.trim(),reference.trim(),status,notes.trim(),existing?.dateTime?:System.currentTimeMillis())) },Modifier.fillMaxWidth()){Text("حفظ النتيجة")}}
    }}
}
