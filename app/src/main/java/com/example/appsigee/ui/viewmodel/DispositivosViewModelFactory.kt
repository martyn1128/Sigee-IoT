package com.example.appsigee.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appsigee.data.local.dao.AlertaDao
import com.example.appsigee.data.local.dao.ConfiguracionConsumoDao
import com.example.appsigee.data.local.dao.GrupoDao
import com.example.appsigee.data.repository.DispositivoRepository

class DispositivosViewModelFactory(
    private val repository: DispositivoRepository,
    private val grupoDao: GrupoDao,
    private val configuracionDao: ConfiguracionConsumoDao,
    private val alertaDao: AlertaDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DispositivosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DispositivosViewModel(repository, grupoDao, configuracionDao, alertaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
