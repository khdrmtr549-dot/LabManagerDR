package com.fieldlab.labmanager.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SimpleVmFactory<T : ViewModel>(private val createBlock: () -> T) : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        @Suppress("UNCHECKED_CAST")
        return createBlock() as VM
    }
}
