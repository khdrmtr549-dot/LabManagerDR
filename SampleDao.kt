package com.fieldlab.labmanager.data.local.dao

import androidx.room.*
import com.fieldlab.labmanager.data.local.entity.Sample
import kotlinx.coroutines.flow.Flow

@Dao
interface SampleDao {
    @Query("SELECT * FROM samples WHERE patientId = :patientId ORDER BY collectionDateTime DESC") fun observeForPatient(patientId: Long): Flow<List<Sample>>
    @Query("SELECT * FROM samples ORDER BY collectionDateTime DESC") fun observeAll(): Flow<List<Sample>>
    @Query("SELECT * FROM samples WHERE sampleNumber LIKE '%' || :query || '%' OR sampleType LIKE '%' || :query || '%' ORDER BY collectionDateTime DESC") fun search(query: String): Flow<List<Sample>>
    @Query("SELECT * FROM samples WHERE id = :id LIMIT 1") suspend fun getById(id: Long): Sample?
    @Insert(onConflict = OnConflictStrategy.ABORT) suspend fun insert(sample: Sample): Long
    @Update suspend fun update(sample: Sample)
    @Delete suspend fun delete(sample: Sample)
    @Query("UPDATE samples SET status = :status WHERE id = :id") suspend fun updateStatus(id: Long, status: String)
    @Query("SELECT COUNT(*) FROM samples WHERE status != 'مكتملة'") fun pendingCount(): Flow<Int>
    @Query("SELECT COUNT(*) FROM samples") fun count(): Flow<Int>
}
