package com.example.p2_u3_cristhian_bravo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "solicitudes")
data class Solicitud(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var nombreCompleto: String,
    var rut: String,
    var fechaNacimiento: String,
    var email: String,
    var telefono: String,
    var latitud: Double,
    var longitud: Double,
    var imagenCedulaFrontal: String,
    var imagenCedulaTrasera: String,
    var fechaCreacion: Date = Date()
)
