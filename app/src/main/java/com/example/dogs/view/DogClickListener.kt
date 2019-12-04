package com.example.dogs.view

import android.view.View

/**
 * écouteur : interface qui écoute les événements et produit un résultat
 * lorsqu’un événement de click est déclenché sur un item de la liste
 */
interface DogClickListener {
    fun onDogClicked(v: View)
}