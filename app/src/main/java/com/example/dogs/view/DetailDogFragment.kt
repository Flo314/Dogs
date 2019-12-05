package com.example.dogs.view


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import com.example.dogs.R
import com.example.dogs.databinding.FragmentDetailDogBinding
import com.example.dogs.model.DogPalette
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

    private lateinit var dataBinding: FragmentDetailDogBinding
    private var sendSmsStrarted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // menu
        setHasOptionsMenu(true)
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_dog, container, false)
        //return inflater.inflate(R.layout.fragment_detail_dog, container, false)
        return dataBinding.root
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
                dataBinding.detailDog = dog

                it.imageUrl?.let {
                    setupBackgroundColor(it)
                }
            }
        })
    }

    private fun setupBackgroundColor(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object: CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate { palette ->
                            val intColor = palette?.lightMutedSwatch?.rgb ?: 0
                            val myPalette = DogPalette(intColor)
                            dataBinding.palette = myPalette
                        }
                }

            })
    }

    // menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send_sms -> {
                sendSmsStrarted = true
                (activity as MainActivity).checkSmsPermission()
            }
            R.id.action_share -> {

            }
        }

        return super.onOptionsItemSelected(item)
    }

    // méthode qui sera appelée quand l'activity sera terminé et qui donnera un résultat
    fun onPermissionResult(permissionGranted: Boolean) {

    }


}
