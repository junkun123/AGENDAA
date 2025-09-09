package com.example.agenda11

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Nota::class], version = 2, exportSchema = false) // VERSIÓN INCREMENTADA A 2
abstract class NotaDatabase : RoomDatabase() {
    abstract fun notaDao(): NotaDao

    companion object {
        @Volatile
        private var INSTANCE: NotaDatabase? = null

        // MIGRACIÓN DE VERSIÓN 1 A 2
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Añade la columna correoElectronico a la tabla notas.
                // Usamos TEXT para el tipo de columna y NOT NULL con un valor DEFAULT vacío.
                // Si correoElectronico puede ser nulo en la base de datos, puedes cambiarlo a "ADD COLUMN correoElectronico TEXT"
                db.execSQL("ALTER TABLE notas ADD COLUMN correoElectronico TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getDatabase(context: Context): NotaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotaDatabase::class.java,
                    "agenda11_db" // Nombre de tu base de datos
                )
                .addMigrations(MIGRATION_1_2) // MIGRACIÓN AÑADIDA
                // Si prefieres destruir y recrear la base de datos en caso de error de migración (¡PERDERÁS DATOS!):
                // .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
