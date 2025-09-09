package com.example.agenda11

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotaDao {

    @Query("SELECT * FROM notas ORDER BY id DESC")
    fun getAllNotas(): LiveData<List<Nota>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(nota: Nota)

    @Update
    suspend fun update(nota: Nota)

    @Delete
    suspend fun delete(nota: Nota)

    // Este es el método de búsqueda correcto
    @Query("SELECT * FROM notas WHERE titulo LIKE :nombre ORDER BY id DESC")
    fun searchNotas(nombre: String): LiveData<List<Nota>>

    @Query("SELECT * FROM notas WHERE id = :id")
    fun getNotaById(id: Int): LiveData<Nota>
}
