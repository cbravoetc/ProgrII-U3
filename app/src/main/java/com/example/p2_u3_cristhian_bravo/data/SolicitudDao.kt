package com.example.p2_u3_cristhian_bravo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

//¿Si tengo que importar 5 por que no mejor importar 1 con *?

@Dao
interface SolicitudDao {
    @Query("SELECT * FROM solicitudes ORDER BY fechaCreacion DESC")
    fun getAllSolicitudes(): List<Solicitud>

    @Query("SELECT * FROM solicitudes WHERE id = :id")
    fun getSolicitudById(id: Int): Solicitud

    @Insert
    fun insertSolicitud(solicitud: Solicitud)

    @Delete
    fun deleteSolicitud(solicitud: Solicitud)

    @Update
    fun updateSolicitud(solicitud: Solicitud)
}
