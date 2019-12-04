package com.example.dogs.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation

import com.example.dogs.R
import com.example.dogs.util.getProgessDrawable
import com.example.dogs.util.loadImage
import com.example.dogs.viewmodel.DetailDogViewModel
import kotlinx.android.synthetic.main.fragment_detail_dog.*


/**
 * Détail du chien
 */
class DetailDogFragment : Fragment() {

    private lateinit var viewModel: DetailDogViewModel
    private var dogUuid = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_dog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrouver l'argument passé Si les arguments sont non null
        arguments?.let {
            dogUuid = DetailDogFragmentArgs.fromBundle(it).dogUuid
        }
        viewModel = ViewModelProviders.of(this).get(DetailDogViewModel::class.java)
        viewModel.fetch(dogUuid)



        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.dogLiveData.observe(this, Observer { dog ->
            // on récupère les data et on les mets à jour
            dog?.let {
                dogName.text = dog.dogBreed
                dogPurpose.text = dog.bredFor
                dogTemperament.text = dog.temperament
                dogLifespan.text = dog.lifespan
                // charger l'image
                context?.let {
                    dogImage.loadImage(dog.imageUrl, getProgessDrawable(it))
                }
            }
        })
    }


}
