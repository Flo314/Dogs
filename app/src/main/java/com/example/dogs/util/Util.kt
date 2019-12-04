package com.example.dogs.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dogs.R

// fonction qui affiche un spiner quand les images se chargent
fun getProgessDrawable(context: Context) : CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 5f // épaisseur du trait
        centerRadius = 25f // diamètre du cercle
        start()
    }
}

// fonction d'extension pour charger les images
// dans l'adapter on pourra faire appel a cette fonction dans onBindViewholder() pour l'affichage
fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_dog_icon)
    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}

// il faut cleaner et rebuild le projet pour après pouvoir utiliser dans le layout
@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.loadImage(url, getProgessDrawable(view.context))
}