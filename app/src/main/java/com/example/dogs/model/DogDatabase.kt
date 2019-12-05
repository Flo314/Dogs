package com.example.dogs.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * class pour l'accès à la DB
 */
@Database(entities = arrayOf(DogBreed::class), version = 1, exportSchema = false)
abstract class DogDatabase : RoomDatabase() {
    abstract fun dogDao(): DogDao

    companion object {
        @Volatile private var instance: DogDatabase? = null
        private val LOCK = Any()
        // synchronized = signifie que si plusieurs threads tentent d’accéder à ce bloc de code, un seul pourra le faire
        // pour éviter les conflits
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            DogDatabase::class.java,
            "dogdatabase"
        ).build()
    }
}