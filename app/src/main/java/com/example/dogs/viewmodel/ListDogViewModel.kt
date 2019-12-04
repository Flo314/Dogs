package com.example.dogs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogs.model.DogBreed
import com.example.dogs.model.DogDatabase
import com.example.dogs.model.DogsApiService
import com.example.dogs.util.NotificationsHelper
import com.example.dogs.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

/**
 * Appel le service Api pour obtenir les data
 */
class ListDogViewModel(application: Application) : BaseViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(getApplication())
    // cela fait cinq minutes en nanosecondes
    // test avec 10 s = private var refreshTime = 10 * 1000 * 1000 * 1000L it's ok
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

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
        // obtient l'heure de la mise à jour à partir des préférences partagées.
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    // permet de contourner le cache (DB) et de récupérer à partir du point final
    fun refreshBypassCache() {
        fetchFromRemote()
    }

    // retourne les data de la DB
    private fun fetchFromDatabase() {
        loading.value = true
        // launch = faire cette opération en tâche de fond grâce aux coroutines
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrived(dogs)
            Toast.makeText(getApplication(), "Dogs retrived from database", Toast.LENGTH_SHORT).show()
        }
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
                        storeDogsLocally(dogList)
                        Toast.makeText(getApplication(), "Dogs retrived from endpoint", Toast.LENGTH_SHORT).show()
                        // notification envoyer automatiquement quand on récupère les données
                        NotificationsHelper(getApplication()).createNotification()
                    }

                    override fun onError(e: Throwable) {
                        dogsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                }))

    }

    private fun dogsRetrived(dogList: List<DogBreed>) {
        dogs.value = dogList
        dogsLoadError.value = false
        loading.value = false
    }

    // stocker les data dans la DB
    private fun storeDogsLocally(list: List<DogBreed>) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            // delete en premier pour ne pas polluer la DB par les informations précédentes
            dao.deleteAllDogs()
            // *list.toTypedArray() = cela donne une liste et la développe en éléments individuels
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++ i
            }
            dogsRetrived(list)
        }
        // cela stocke les informations du moment exact à la nanoseconde la plus proche.
        // au moment où on met à jour la base de données avec les infos
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    // pour la fuite de mémoire
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}