package com.example.p2_u3_cristhian_bravo.mock

import com.example.p2_u3_cristhian_bravo.data.Solicitud
import com.example.p2_u3_cristhian_bravo.data.SolicitudDao

class FakeSolicitudDao : SolicitudDao {
    private val solicitudesList = mutableListOf<Solicitud>()

    override fun getAllSolicitudes(): List<Solicitud> {
        return solicitudesList
    }

    override fun getSolicitudById(id: Int): Solicitud {
        return solicitudesList.first { it.id == id }
    }

    override fun insertSolicitud(solicitud: Solicitud) {
        solicitudesList.add(solicitud)
    }

    override fun deleteSolicitud(solicitud: Solicitud) {
        solicitudesList.remove(solicitud)
    }

    override fun updateSolicitud(solicitud: Solicitud) {
        val index = solicitudesList.indexOfFirst { it.id == solicitud.id }
        if (index != -1) {
            solicitudesList[index] = solicitud
        }
    }
}
