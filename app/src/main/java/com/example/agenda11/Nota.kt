package com.example.agenda11

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notas")
data class Nota(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val nombre: String,
    val telefono: String,
    val descripcion: String,
    val correoElectronico: String // Nuevo campo
)
