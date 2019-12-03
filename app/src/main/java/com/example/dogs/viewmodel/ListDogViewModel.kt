package com.example.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogs.model.DogBreed
import com.example.dogs.model.DogsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

/**
 * Appel le service Api pour obtenir les data
 */
class ListDogViewModel : ViewModel() {

    private val dogsService = DogsApiService()
    private val disposable = CompositeDisposable()

    // liste actuelle qui sera récup du backend
    val dogs = MutableLiveData<List<DogBreed>>()
    // erreur lors de l'extraction des données
    val dogsLoadError = MutableLiveData<Boolean>()
    // données en chargement
    val loading = MutableLiveData<Boolean>()

    // déterminer si on veut les data du réseau ou de la database
    fun refresh() {
        fetchFromRemote()
    }

    // retourne les data du service Api
    private fun fetchFromRemote() {
        loading.value = true
        // définir cet appel sur un thread séparé pour pas bloquer l'app
        disposable.add(
            dogsService.getDogs()
                    // on veut que ce processus soit effectué en tâche de fond
                .subscribeOn(Schedulers.newThread())
                // le résultat de ce processus doit être traité sur le thread principal
                .observeOn(AndroidSchedulers.mainThread())
                    // ici, nous devons passer l'observateur que nous voulons observer le "single"
                .subscribeWith(object: DisposableSingleObserver<List<DogBreed>>() {

                    override fun onSuccess(dogList: List<DogBreed>) {
                        dogs.value = dogList
                        dogsLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                }))

    }

    // pour la fuite de mémoire
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}