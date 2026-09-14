package com.fieldlab.labmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fieldlab.labmanager.data.local.AppDatabase
import com.fieldlab.labmanager.data.repository.LabRepository
import com.fieldlab.labmanager.ui.navigation.AppNavigation
import com.fieldlab.labmanager.viewmodel.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getInstance(applicationContext)

        val repo = LabRepository(
            db.patientDao(),
            db.sampleDao(),
            db.laboratoryResultDao(),
            db.testDefinitionDao()
        )

        setContent {
            AppTheme {

                val dashboardVm: DashboardViewModel =
                    viewModel(factory = SimpleFactory { DashboardViewModel(repo) })

                val patientsVm: PatientsViewModel =
                    viewModel(factory = SimpleFactory { PatientsViewModel(repo) })

                val testsVm: TestsViewModel =
                    viewModel(factory = SimpleFactory { TestsViewModel(repo) })

                val dataVm: LabDataViewModel =
                    viewModel(factory = SimpleFactory { LabDataViewModel(repo) })

                AppNavigation(
                    repo = repo,
                    dashboardVm = dashboardVm,
                    patientsVm = patientsVm,
                    testsVm = testsVm,
                    dataVm = dataVm
                )
            }
        }
    }
}

class SimpleFactory<T : androidx.lifecycle.ViewModel>(
    private val block: () -> T
) : androidx.lifecycle.ViewModelProvider.Factory {

    override fun <VM : androidx.lifecycle.ViewModel>
            create(modelClass: Class<VM>): VM {

        @Suppress("UNCHECKED_CAST")
        return block() as VM
    }
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {

    val scheme = lightColorScheme(
        primary = Color(0xFF123B59),
        secondary = Color(0xFF2B6B93),
        background = Color(0xFFF4F7FA),
        surface = Color.White
    )

    MaterialTheme(
        colorScheme = scheme,
        content = content
    )
}
