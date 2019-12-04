package com.example.dogs.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.dogs.R
import com.example.dogs.model.DogBreed
import com.example.dogs.util.getProgessDrawable
import com.example.dogs.util.loadImage
import kotlinx.android.synthetic.main.item_dog.view.*

class DogsListAdapter(val dogsList: ArrayList<DogBreed>): RecyclerView.Adapter<DogsListAdapter.DogViewHolder>() {

    fun updateDogList(newDogsList: List<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    // retourne la taille de la liste
    override fun getItemCount(): Int = dogsList.size


    // transforme les éléments de la liste en vue
    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.name.text = dogsList[position].dogBreed
        holder.view.lifespan.text = dogsList[position].lifespan
        // click pour avoir le detail d'un élément de la liste
        holder.view.setOnClickListener {
            val action = ListDogsFragmentDirections.actionDetailDogFragment()
            action.dogUuid = dogsList[position].uuid
            Navigation.findNavController(it).navigate(action)
        }
        holder.view.imageView.loadImage(dogsList[position].imageUrl, getProgessDrawable(holder.view.imageView.context))
    }

    class DogViewHolder(var view: View): RecyclerView.ViewHolder(view)


}