package com.fieldlab.labmanager.data.local.dao

import androidx.room.*
import com.fieldlab.labmanager.data.local.entity.Patient
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {
    @Query("SELECT * FROM patients ORDER BY createdDate DESC") fun observeAll(): Flow<List<Patient>>
    @Query("SELECT * FROM patients WHERE fullName LIKE '%' || :query || '%' OR patientNumber LIKE '%' || :query || '%' ORDER BY createdDate DESC") fun search(query: String): Flow<List<Patient>>
    @Query("SELECT * FROM patients WHERE id = :id LIMIT 1") suspend fun getById(id: Long): Patient?
    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insert(patient: Patient): Long
    @Update suspend fun update(patient: Patient)
    @Delete suspend fun delete(patient: Patient)
    @Query("SELECT COUNT(*) FROM patients") fun count(): Flow<Int>
}
