package com.fieldlab.labmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fieldlab.labmanager.ui.components.SectionCard

@Composable fun SettingsScreen(){Scaffold(topBar={TopAppBar(title={Text("الإعدادات")})}){pad->Column(Modifier.padding(pad).padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){SectionCard("معلومات التطبيق"){Text("مختبر — الإصدار 1.0");Text("تطبيق إدارة مختبر أصلي لنظام Android")};SectionCard("قاعدة البيانات"){Text("Room / SQLite");Text("التخزين محلي على الجهاز");Text("لا يتم حذف أو تصفير البيانات تلقائيًا")};SectionCard("إعدادات المختبر"){Text("يمكن تخصيص اسم المختبر وإعدادات التقرير في الإصدارات التالية.")}}}}
