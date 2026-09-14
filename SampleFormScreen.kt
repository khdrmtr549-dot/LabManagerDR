package com.fieldlab.labmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fieldlab.labmanager.data.local.entity.*
import com.fieldlab.labmanager.ui.components.*

@Composable fun SampleFormScreen(patients: List<Patient>, existing: Sample?, preselectedPatientId: Long? = null, onSave: (Sample) -> Unit, onBack: () -> Unit) {
    var patientId by remember { mutableStateOf(existing?.patientId ?: preselectedPatientId ?: patients.firstOrNull()?.id ?: 0L) }; var number by remember { mutableStateOf(existing?.sampleNumber ?: "") }; var type by remember { mutableStateOf(existing?.sampleType ?: "دم") }; var status by remember { mutableStateOf(existing?.status ?: "معلّقة") }; var notes by remember { mutableStateOf(existing?.notes ?: "") }; var error by remember { mutableStateOf("") }
    val patientName = patients.firstOrNull { it.id == patientId }?.fullName ?: ""
    Scaffold(topBar={TopBar(if(existing==null)"تسجيل عينة" else "تعديل عينة",onBack)}){pad->Column(Modifier.padding(pad).padding(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
        if(patients.isEmpty()) EmptyState("أضف مريضًا أولًا") else { DropdownField("المريض",patientName,patients,{it.fullName},{patientId=it.id}); OutlinedTextField(number,{number=it},Modifier.fillMaxWidth(),label={Text("رقم العينة *")},singleLine=true); DropdownField("نوع العينة",type,listOf("دم","بول","مصل","بلازما","مسحة","براز","أخرى"),{it},{type=it}); DropdownField("الحالة",status,listOf("معلّقة","قيد الفحص","مكتملة"),{it},{status=it}); OutlinedTextField(notes,{notes=it},Modifier.fillMaxWidth(),label={Text("ملاحظات")},minLines=3); if(error.isNotBlank()) Text(error,color=MaterialTheme.colorScheme.error); Button(onClick={if(patientId==0L||number.isBlank())error="المريض ورقم العينة مطلوبان"else onSave(Sample(existing?.id?:0,patientId,number.trim(),type,System.currentTimeMillis(),status,notes.trim()))},Modifier.fillMaxWidth()){Text("حفظ العينة")}}
    }}
}
