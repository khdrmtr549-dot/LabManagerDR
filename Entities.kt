package com.fieldlab.labmanager.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(tableName = "patients", indices = [Index(value = ["patientNumber"], unique = true), Index("fullName")])
data class Patient(
    @androidx.room.PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientNumber: String,
    val fullName: String,
    val dateOfBirth: Long? = null,
    val age: Int? = null,
    val gender: String,
    val phone: String = "",
    val notes: String = "",
    val createdDate: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "samples",
    foreignKeys = [ForeignKey(entity = Patient::class, parentColumns = ["id"], childColumns = ["patientId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("patientId"), Index(value = ["sampleNumber"], unique = true), Index("status")]
)
data class Sample(
    @androidx.room.PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientId: Long,
    val sampleNumber: String,
    val sampleType: String,
    val collectionDateTime: Long = System.currentTimeMillis(),
    val status: String = "معلّقة",
    val notes: String = ""
)

@Entity(
    tableName = "laboratory_results",
    foreignKeys = [
        ForeignKey(entity = Patient::class, parentColumns = ["id"], childColumns = ["patientId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Sample::class, parentColumns = ["id"], childColumns = ["sampleId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("patientId"), Index("sampleId"), Index("testName")]
)
data class LaboratoryResult(
    @androidx.room.PrimaryKey(autoGenerate = true) val id: Long = 0,
    val patientId: Long,
    val sampleId: Long,
    val testName: String,
    val resultValue: String,
    val unit: String,
    val referenceRange: String,
    val resultStatus: String = "طبيعي",
    val notes: String = "",
    val dateTime: Long = System.currentTimeMillis()
)

@Entity(tableName = "test_definitions", indices = [Index("category"), Index("testName")])
data class TestDefinition(
    @androidx.room.PrimaryKey(autoGenerate = true) val id: Long = 0,
    val testName: String,
    val category: String,
    val unit: String,
    val referenceRange: String,
    val normalInformation: String = ""
)
