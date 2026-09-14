package com.fieldlab.labmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fieldlab.labmanager.data.local.entity.*
import com.fieldlab.labmanager.data.repository.LabRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DashboardViewModel(repo: LabRepository): ViewModel() {
    val patientCount = repo.patientCount
    val pendingSamples = repo.pendingSamples
    val completedResults = repo.resultCount
}

class PatientsViewModel(private val repo: LabRepository): ViewModel() {
    private val query = MutableStateFlow("")
    val searchQuery: StateFlow<String> = query.asStateFlow()
    val patients: StateFlow<List<Patient>> = query.flatMapLatest { if (it.isBlank()) repo.patients else repo.searchPatients(it) }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    fun setQuery(v: String) { query.value = v }
    fun delete(p: Patient) = viewModelScope.launch { repo.deletePatient(p) }
    fun save(p: Patient, onDone: (Long) -> Unit = {}) = viewModelScope.launch { val id = if (p.id == 0L) repo.addPatient(p) else { repo.updatePatient(p); p.id }; onDone(id) }
}

class PatientProfileViewModel(private val repo: LabRepository, private val patientId: Long): ViewModel() {
    val patient = flow { emit(repo.getPatient(patientId)) }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val samples = repo.patientSamples(patientId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val results = repo.patientResults(patientId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    fun deletePatient(onDone: () -> Unit) = viewModelScope.launch { patient.value?.let { repo.deletePatient(it); onDone() } }
    fun addSample(sample: Sample, onDone: (Long) -> Unit = {}) = viewModelScope.launch { onDone(repo.addSample(sample)) }
    fun addResult(result: LaboratoryResult, onDone: (Long) -> Unit = {}) = viewModelScope.launch { onDone(repo.addResult(result)) }
    fun updateResult(result: LaboratoryResult) = viewModelScope.launch { repo.updateResult(result) }
    fun deleteResult(result: LaboratoryResult) = viewModelScope.launch { repo.deleteResult(result) }
    fun updateSample(sample: Sample) = viewModelScope.launch { repo.updateSample(sample) }
}

class LabDataViewModel(private val repo: LabRepository): ViewModel() {
    val patients = repo.patients.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val samples = repo.samples.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val results = repo.results.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val tests = repo.tests.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    private val query = MutableStateFlow("")
    val searchQuery = query.asStateFlow()
    val searchResults = query.flatMapLatest { q ->
        combine(repo.searchPatients(q), repo.searchSamples(q), repo.searchResults(q)) { p, s, r -> Triple(p, s, r) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Triple(emptyList(), emptyList(), emptyList()))
    init { viewModelScope.launch { repo.seedTests() } }
    fun setQuery(q: String) { query.value = q }
    fun updateResult(r: LaboratoryResult) = viewModelScope.launch { repo.updateResult(r) }
    fun deleteResult(r: LaboratoryResult) = viewModelScope.launch { repo.deleteResult(r) }
    fun updateSample(s: Sample) = viewModelScope.launch { repo.updateSample(s) }
    fun addSample(s: Sample, onDone: (Long) -> Unit = {}) = viewModelScope.launch { onDone(repo.addSample(s)) }
}

class TestsViewModel(private val repo: LabRepository): ViewModel() {
    val tests = repo.tests.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val categories = tests.map { it.map(TestDefinition::category).distinct().sorted() }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
