package com.example.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogs.model.DogBreed

class DetailDogViewModel : ViewModel() {

    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch() {
        val dog = DogBreed(
            "1",
            "Staff",
            "15 years",
            "breedGroup",
            "bredfor",
            "temperament",
            "")
        dogLiveData.value = dog
    }
}