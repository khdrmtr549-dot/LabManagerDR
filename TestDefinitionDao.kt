package com.fieldlab.labmanager.data.local.dao

import androidx.room.*
import com.fieldlab.labmanager.data.local.entity.TestDefinition
import kotlinx.coroutines.flow.Flow

@Dao
interface TestDefinitionDao {
    @Query("SELECT * FROM test_definitions ORDER BY category, testName") fun observeAll(): Flow<List<TestDefinition>>
    @Query("SELECT * FROM test_definitions WHERE category = :category ORDER BY testName") fun observeByCategory(category: String): Flow<List<TestDefinition>>
    @Query("SELECT * FROM test_definitions WHERE testName LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' ORDER BY category, testName") fun search(query: String): Flow<List<TestDefinition>>
    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insertAll(items: List<TestDefinition>)
    @Update suspend fun update(item: TestDefinition)
    @Delete suspend fun delete(item: TestDefinition)
}
