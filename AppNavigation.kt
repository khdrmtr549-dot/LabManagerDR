package com.fieldlab.labmanager.ui.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.fieldlab.labmanager.data.local.entity.*
import com.fieldlab.labmanager.ui.screens.*
import com.fieldlab.labmanager.viewmodel.*

sealed class Route(val value:String){data object Dashboard:Route("dashboard");data object Patients:Route("patients");data object PatientAdd:Route("patient_add");data object PatientEdit:Route("patient_edit/{id}"){fun create(id:Long)="patient_edit/$id"};data object Profile:Route("profile/{id}"){fun create(id:Long)="profile/$id"};data object SampleAdd:Route("sample_add?patientId={patientId}"){fun create(id:Long?=null)="sample_add?patientId=${id?:-1}"};data object Tests:Route("tests");data object Results:Route("results");data object ResultAdd:Route("result_add?patientId={patientId}&sampleId={sampleId}"){fun create(p:Long,s:Long)="result_add?patientId=$p&sampleId=$s"};data object ResultEdit:Route("result_edit/{id}"){fun create(id:Long)="result_edit/$id"};data object Search:Route("search");data object Settings:Route("settings")}

@Composable fun AppNavigation(repo: com.fieldlab.labmanager.data.repository.LabRepository, dashboardVm: DashboardViewModel, patientsVm: PatientsViewModel, testsVm: TestsViewModel, dataVm: LabDataViewModel){ val nav=rememberNavController(); NavHost(nav, startDestination=Route.Dashboard.value){
    composable(Route.Dashboard.value){DashboardScreen(dashboardVm,{nav.navigate(Route.PatientAdd.value)},{nav.navigate(Route.SampleAdd.create())},{nav.navigate(Route.Patients.value)},{nav.navigate(Route.Search.value)})}
    composable(Route.Patients.value){PatientsScreen(patientsVm,{nav.navigate(Route.Profile.create(it))},{nav.navigate(Route.PatientEdit.create(it))},{nav.navigate(Route.PatientAdd.value)})}
    composable(Route.PatientAdd.value){PatientFormScreen(null,{p->patientsVm.save(p){nav.popBackStack()}},{nav.popBackStack()})}
    composable(Route.PatientEdit.value,arguments=listOf(navArgument("id"){type=NavType.LongType})){back->val id=back.arguments!!.getLong("id"); val p=remember { mutableStateOf<Patient?>(null) }; LaunchedEffect(id){p.value=repo.getPatient(id)}; PatientFormScreen(p.value,{x->patientsVm.save(x){nav.popBackStack()}},{nav.popBackStack()})}
    composable(Route.Profile.value,arguments=listOf(navArgument("id"){type=NavType.LongType})){back->val id=back.arguments!!.getLong("id");val vm:PatientProfileViewModel= viewModel(key="profile-$id",factory=SimpleVmFactory{PatientProfileViewModel(repo,id)});PatientProfileScreen(vm,{nav.popBackStack()},{nav.navigate(Route.SampleAdd.create(it))},{p,s->nav.navigate(Route.ResultAdd.create(p,s))},{r->nav.navigate(Route.ResultEdit.create(r.id))})}
    composable(Route.SampleAdd.value,arguments=listOf(navArgument("patientId"){type=NavType.LongType;defaultValue=-1L})){back->val patients by patientsVm.patients.collectAsState();val pid=back.arguments!!.getLong("patientId").takeIf{it>0};SampleFormScreen(patients,null,pid,{s->dataVm.addSample(s){nav.popBackStack()}},{nav.popBackStack()})}
    composable(Route.Tests.value){TestsScreen(testsVm)}
    composable(Route.Results.value){ResultsScreen(dataVm,{r->nav.navigate(Route.ResultEdit.create(r.id))})}
    composable(Route.Search.value){SearchScreen(dataVm,{nav.navigate(Route.Profile.create(it))},{r->nav.navigate(Route.ResultEdit.create(r.id))})}
    composable(Route.Settings.value){SettingsScreen()}
    composable(Route.ResultAdd.value,arguments=listOf(navArgument("patientId"){type=NavType.LongType},navArgument("sampleId"){type=NavType.LongType})){back->val p=back.arguments!!.getLong("patientId");val s=back.arguments!!.getLong("sampleId");val patients by patientsVm.patients.collectAsState();val samples by dataVm.samples.collectAsState();val tests by dataVm.tests.collectAsState();ResultFormScreen(patients,samples,tests,null,p,s,{r->dataVm.updateResult(r);nav.popBackStack()},{nav.popBackStack()})}
    composable(Route.ResultEdit.value,arguments=listOf(navArgument("id"){type=NavType.LongType})){back->val id=back.arguments!!.getLong("id");var existing by remember{mutableStateOf<LaboratoryResult?>(null)};LaunchedEffect(id){existing=repo.getResult(id)};val e=existing;val patients by patientsVm.patients.collectAsState();val samples by dataVm.samples.collectAsState();val tests by dataVm.tests.collectAsState();if(e!=null)ResultFormScreen(patients,samples,tests,e,e.patientId,e.sampleId,{r->dataVm.updateResult(r);nav.popBackStack()},{nav.popBackStack()})}
}}

