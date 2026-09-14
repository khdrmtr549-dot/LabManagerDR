package com.fieldlab.labmanager.data.repository

import com.fieldlab.labmanager.data.local.dao.*
import com.fieldlab.labmanager.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class LabRepository(
    private val patientDao: PatientDao,
    private val sampleDao: SampleDao,
    private val resultDao: LaboratoryResultDao,
    private val testDao: TestDefinitionDao
) {
    val patients = patientDao.observeAll()
    val samples = sampleDao.observeAll()
    val results = resultDao.observeAll()
    val tests = testDao.observeAll()
    val patientCount = patientDao.count()
    val pendingSamples = sampleDao.pendingCount()
    val resultCount = resultDao.count()

    fun searchPatients(query: String): Flow<List<Patient>> = patientDao.search(query)
    fun searchSamples(query: String): Flow<List<Sample>> = sampleDao.search(query)
    fun searchResults(query: String): Flow<List<LaboratoryResult>> = resultDao.search(query)
    fun patientSamples(id: Long): Flow<List<Sample>> = sampleDao.observeForPatient(id)
    fun patientResults(id: Long): Flow<List<LaboratoryResult>> = resultDao.observeForPatient(id)
    fun sampleResults(id: Long): Flow<List<LaboratoryResult>> = resultDao.observeForSample(id)
    suspend fun getPatient(id: Long) = patientDao.getById(id)
    suspend fun getSample(id: Long) = sampleDao.getById(id)
    suspend fun getResult(id: Long) = resultDao.getById(id)
    suspend fun addPatient(p: Patient) = patientDao.insert(p)
    suspend fun updatePatient(p: Patient) = patientDao.update(p)
    suspend fun deletePatient(p: Patient) = patientDao.delete(p)
    suspend fun addSample(s: Sample) = sampleDao.insert(s)
    suspend fun updateSample(s: Sample) = sampleDao.update(s)
    suspend fun deleteSample(s: Sample) = sampleDao.delete(s)
    suspend fun addResult(r: LaboratoryResult) = resultDao.insert(r)
    suspend fun updateResult(r: LaboratoryResult) = resultDao.update(r)
    suspend fun deleteResult(r: LaboratoryResult) = resultDao.delete(r)
    suspend fun updateSampleStatus(id: Long, status: String) = sampleDao.updateStatus(id, status)
    suspend fun seedTests() = testDao.insertAll(defaultTests())

    companion object {
        fun defaultTests() = listOf(
            TestDefinition(testName="CBC / تعداد الدم الكامل", category="أمراض الدم", unit="", referenceRange="حسب المختبر", normalInformation="قيم مرجعية تعتمد على العمر والجنس والمختبر"),
            TestDefinition(testName="Hb / الهيموغلوبين", category="أمراض الدم", unit="g/dL", referenceRange="12–17.5", normalInformation="مرجع تقريبي للبالغين"),
            TestDefinition(testName="Glucose / سكر الدم", category="الكيمياء الحيوية", unit="mg/dL", referenceRange="70–99 صائم", normalInformation="يُفسّر حسب حالة الصيام"),
            TestDefinition(testName="Creatinine / كرياتينين", category="الكيمياء الحيوية", unit="mg/dL", referenceRange="0.6–1.3", normalInformation="مرجع تقريبي للبالغين"),
            TestDefinition(testName="CRP", category="المناعة", unit="mg/L", referenceRange="<5", normalInformation="يجب تفسيره سريريًا"),
            TestDefinition(testName="TSH", category="الهرمونات", unit="mIU/L", referenceRange="0.4–4.0", normalInformation="قد تختلف حسب الطريقة والمختبر"),
            TestDefinition(testName="Urinalysis", category="تحليل البول", unit="", referenceRange="سلبي/طبيعي", normalInformation="يتضمن الفحص الفيزيائي والكيميائي والرسوبي"),
            TestDefinition(testName="Culture", category="الأحياء الدقيقة", unit="", referenceRange="لا نمو", normalInformation="تحديد الجرثوم والحساسية عند وجود نمو")
        )
    }
}
