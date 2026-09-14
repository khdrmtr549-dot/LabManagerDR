package com.fieldlab.labmanager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fieldlab.labmanager.data.local.dao.*
import com.fieldlab.labmanager.data.local.entity.*

@Database(entities = [Patient::class, Sample::class, LaboratoryResult::class, TestDefinition::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao
    abstract fun sampleDao(): SampleDao
    abstract fun laboratoryResultDao(): LaboratoryResultDao
    abstract fun testDefinitionDao(): TestDefinitionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "laboratory.db").build().also { INSTANCE = it }
        }
    }
}
