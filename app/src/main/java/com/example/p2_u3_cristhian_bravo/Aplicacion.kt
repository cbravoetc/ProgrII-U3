package com.example.p2_u3_cristhian_bravo

import android.app.Application
import androidx.room.Room
import com.example.p2_u3_cristhian_bravo.data.AppDatabase

class Aplicacion : Application(){

    val db by lazy { Room.databaseBuilder(this, AppDatabase::class.java, "solicitudes.db").build() }
    val solitudDao by lazy { db.solicitudDao() }
}