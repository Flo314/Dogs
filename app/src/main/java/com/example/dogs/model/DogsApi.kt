package com.example.dogs.model

import io.reactivex.Single
import retrofit2.http.GET

/**
 * url = https://raw.githubusercontent.com/DevTides/DogsApi/master/dogs.json
 */
interface DogsApi {
    @GET("DevTides/DogsApi/master/dogs.json")
    fun getDogs(): Single<List<DogBreed>>
}