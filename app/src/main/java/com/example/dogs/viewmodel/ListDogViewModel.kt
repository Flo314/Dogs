package com.example.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogs.model.DogBreed

class ListDogViewModel : ViewModel() {

    // liste actuelle qui sera récup du backend
    val dogs = MutableLiveData<List<DogBreed>>()
    // erreur lors de l'extraction des données
    val dogsLoadError = MutableLiveData<Boolean>()
    // données en chargement
    val loading = MutableLiveData<Boolean>()

    // création de données fictives pour test
    fun refresh() {
        val dog1 = DogBreed(
            "1",
            "Staff",
            "15 years",
            "breedGroup",
            "bredfor",
            "temperament",
            "")

        val dog2 = DogBreed(
            "2",
            "Labrador",
            "10 years",
            "breedGroup",
            "bredfor",
            "temperament",
            "")

        val dog3 = DogBreed(
            "3",
            "Bichon",
            "20 years",
            "breedGroup",
            "bredfor",
            "temperament",
            "")

        val dogList = arrayListOf<DogBreed>(dog1, dog2, dog3)
        // transmettre les infos au liveData
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }
}