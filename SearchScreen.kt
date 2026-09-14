package com.fieldlab.labmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fieldlab.labmanager.ui.components.*
import com.fieldlab.labmanager.viewmodel.LabDataViewModel

@Composable fun SearchScreen(vm: LabDataViewModel, onPatient: (Long)->Unit, onEditResult: (com.fieldlab.labmanager.data.local.entity.LaboratoryResult)->Unit) { val q by vm.searchQuery.collectAsStateWithLifecycle(); val data by vm.searchResults.collectAsStateWithLifecycle(); Scaffold(topBar={TopBar("بحث")}){pad->Column(Modifier.padding(pad).padding(16.dp)){SearchField(q,vm::setQuery,"اسم المريض أو رقم المريض أو رقم العينة أو اسم الاختبار");Spacer(Modifier.height(12.dp));if(q.isBlank())EmptyState("اكتب كلمة للبحث")else LazyColumn(verticalArrangement=Arrangement.spacedBy(8.dp)){items(data.first){p->Card(Modifier.fillMaxWidth()){ListItem(headlineContent={Text(p.fullName)},supportingContent={Text("مريض: ${p.patientNumber}")},modifier=Modifier.padding(4.dp),trailingContent={TextButton(onClick={onPatient(p.id)}){Text("فتح")}})}};items(data.second){s->Card(Modifier.fillMaxWidth()){ListItem(headlineContent={Text(s.sampleNumber)},supportingContent={Text("عينة: ${s.sampleType} • ${s.status}")})}};items(data.third){r->Card(Modifier.fillMaxWidth()){ListItem(headlineContent={Text(r.testName)},supportingContent={Text("${r.resultValue} ${r.unit} • ${r.resultStatus}")},trailingContent={TextButton(onClick={onEditResult(r)}){Text("تعديل")}})}}}}}}
}
