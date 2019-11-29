package com.example.dogs.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.example.dogs.R
import kotlinx.android.synthetic.main.fragment_detail_dog.*


/**
 * Détail du chien
 */
class DetailDogFragment : Fragment() {

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

    }


}
