package com.example.dogs.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Pareil que le viewModel à la différence que
 * pour accéder à la base de données la DB a besoin d'un contexte
 * il faut un contexte d'application plutôt qu'un contexte d'activité normale,
 * car il est très volatile et peut être détruit sans préavis.
 */
abstract class BaseViewModel(application: Application): AndroidViewModel(application), CoroutineScope {

    private val job = Job()

    // moyen d’exécuter des travaux sur un fil d’arrière-plan.
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}