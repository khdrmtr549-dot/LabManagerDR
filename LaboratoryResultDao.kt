package com.fieldlab.labmanager.data.local.dao

import androidx.room.*
import com.fieldlab.labmanager.data.local.entity.LaboratoryResult
import kotlinx.coroutines.flow.Flow

@Dao
interface LaboratoryResultDao {
    @Query("SELECT * FROM laboratory_results ORDER BY dateTime DESC") fun observeAll(): Flow<List<LaboratoryResult>>
    @Query("SELECT * FROM laboratory_results WHERE patientId = :patientId ORDER BY dateTime DESC") fun observeForPatient(patientId: Long): Flow<List<LaboratoryResult>>
    @Query("SELECT * FROM laboratory_results WHERE sampleId = :sampleId ORDER BY dateTime DESC") fun observeForSample(sampleId: Long): Flow<List<LaboratoryResult>>
    @Query("SELECT * FROM laboratory_results WHERE testName LIKE '%' || :query || '%' OR resultValue LIKE '%' || :query || '%' ORDER BY dateTime DESC") fun search(query: String): Flow<List<LaboratoryResult>>
    @Query("SELECT * FROM laboratory_results WHERE id = :id LIMIT 1") suspend fun getById(id: Long): LaboratoryResult?
    @Insert suspend fun insert(result: LaboratoryResult): Long
    @Update suspend fun update(result: LaboratoryResult)
    @Delete suspend fun delete(result: LaboratoryResult)
    @Query("SELECT COUNT(*) FROM laboratory_results WHERE resultStatus NOT IN ('طبيعي','مقبول')") fun abnormalCount(): Flow<Int>
    @Query("SELECT COUNT(*) FROM laboratory_results") fun count(): Flow<Int>
}
