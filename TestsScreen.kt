package com.fieldlab.labmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fieldlab.labmanager.ui.components.SectionCard
import com.fieldlab.labmanager.viewmodel.TestsViewModel

@Composable fun TestsScreen(vm: TestsViewModel) { val tests by vm.tests.collectAsStateWithLifecycle(); val cats by vm.categories.collectAsStateWithLifecycle(); Scaffold(topBar={TopAppBar(title={Text("التحاليل المخبرية")})}){pad->LazyColumn(Modifier.padding(pad).padding(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){ items(cats){cat->SectionCard(cat){ tests.filter{it.category==cat}.forEach { t-> Column(Modifier.padding(vertical=6.dp)){Text(t.testName,style=MaterialTheme.typography.titleSmall);Text("الوحدة: ${t.unit.ifBlank{"—"}} • المرجع: ${t.referenceRange}",color=MaterialTheme.colorScheme.onSurfaceVariant);if(t.normalInformation.isNotBlank())Text(t.normalInformation,style=MaterialTheme.typography.bodySmall)}} } } } } }
