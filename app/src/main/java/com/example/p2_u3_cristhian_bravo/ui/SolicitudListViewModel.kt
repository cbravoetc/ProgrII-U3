package com.example.p2_u3_cristhian_bravo.ui

import android.Manifest
import android.content.Context
import android.location.Location
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.p2_u3_cristhian_bravo.Aplicacion
import com.example.p2_u3_cristhian_bravo.data.Solicitud
import com.example.p2_u3_cristhian_bravo.data.SolicitudDao
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
// import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class SolicitudListViewModel(
    private val solicitudDao: SolicitudDao,
    private val context: Context?
) : ViewModel(){

    private var fusedLocationClient: FusedLocationProviderClient? = null

    init {
        if (context != null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        }
    }
    var lastLocation = mutableStateOf<Location?>(null)

    var solicitudes by mutableStateOf(listOf<Solicitud>())

    fun agregaSolicitud(solicitud: Solicitud) {
        viewModelScope.launch(Dispatchers.IO) {
            solicitudDao.insertSolicitud(solicitud)
            obtieneSolicitudes()
        }
    }

    fun obtieneSolicitudes(): List<Solicitud> {
        viewModelScope.launch(Dispatchers.IO) {
            solicitudes = solicitudDao.getAllSolicitudes()
        }
        return solicitudes
    }

    open fun getLocation() {
        context?.let { nonNullContext ->
            if (ActivityCompat.checkSelfPermission(nonNullContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(nonNullContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient?.lastLocation?.addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        lastLocation.value = task.result
                    }
                }
            }
        }
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val aplicacion = (this[APPLICATION_KEY] as Aplicacion)
                SolicitudListViewModel(aplicacion.solitudDao, aplicacion.applicationContext)
            }
        }
    }
}