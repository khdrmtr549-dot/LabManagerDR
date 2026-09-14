package com.fieldlab.labmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fieldlab.labmanager.data.local.entity.Patient
import com.fieldlab.labmanager.ui.components.*

@Composable fun PatientFormScreen(existing: Patient?, onSave: (Patient) -> Unit, onBack: () -> Unit) {
    var number by remember { mutableStateOf(existing?.patientNumber ?: "") }; var name by remember { mutableStateOf(existing?.fullName ?: "") }; var age by remember { mutableStateOf(existing?.age?.toString() ?: "") }; var gender by remember { mutableStateOf(existing?.gender ?: "ذكر") }; var phone by remember { mutableStateOf(existing?.phone ?: "") }; var notes by remember { mutableStateOf(existing?.notes ?: "") }; var error by remember { mutableStateOf("") }
    Scaffold(topBar = { TopBar(if(existing == null) "إضافة مريض" else "تعديل مريض", onBack) }) { pad -> Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(number,{number=it},Modifier.fillMaxWidth(),label={Text("رقم المريض *")},singleLine=true); OutlinedTextField(name,{name=it},Modifier.fillMaxWidth(),label={Text("الاسم الكامل *")},singleLine=true); OutlinedTextField(age,{age=it.filter(Char::isDigit)},Modifier.fillMaxWidth(),label={Text("العمر")},singleLine=true); DropdownField("الجنس", gender, listOf("ذكر","أنثى"), {it}, {gender=it}); OutlinedTextField(phone,{phone=it},Modifier.fillMaxWidth(),label={Text("الهاتف")},singleLine=true); OutlinedTextField(notes,{notes=it},Modifier.fillMaxWidth(),label={Text("ملاحظات")},minLines=3); if(error.isNotBlank()) Text(error,color=MaterialTheme.colorScheme.error); Button(onClick={ if(number.isBlank()||name.isBlank()) error="رقم المريض والاسم مطلوبان" else onSave(Patient(existing?.id ?: 0,number.trim(),name.trim(),age=age.toIntOrNull(),gender=gender,phone=phone.trim(),notes=notes.trim(),createdDate=existing?.createdDate ?: System.currentTimeMillis())) },Modifier.fillMaxWidth()){Text("حفظ")}
    } }
}
