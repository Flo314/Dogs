package com.example.dogs.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Cette interface définie donc le type de fonctions que nous pouvons exécuter sur la base de données.
 */
@Dao
interface DogDao {

    /**
     * Insère dans la DB
     * vararg = veut dire qu'on peut passé plusieurs argument de type DogBreed
     * @return retournera autant de Dogbreed que le nombre d'id (primary Key) dans la DB
     * la taille de liste correspond aux nombres d'éléments dans la DB
     */
    @Insert
    // suspend = devra être faite à partir d'un thread séparé
    suspend fun insertAll(vararg dogs: DogBreed): List<Long>

    @Query("SELECT * from dogbreed")
    suspend fun getAllDogs(): List<DogBreed>

    @Query("SELECT * from dogbreed WHERE uuid = :dogId")
    suspend fun getDog(dogId: Int): DogBreed

    @Query("DELETE from dogbreed")
    suspend fun deleteAllDogs()
}