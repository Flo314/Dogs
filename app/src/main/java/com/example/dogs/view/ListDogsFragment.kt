package com.example.dogs.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.dogs.R
import com.example.dogs.viewmodel.ListDogViewModel
import kotlinx.android.synthetic.main.fragment_list_dogs.*

/**
 * Liste des chiens
 */
class ListDogsFragment : Fragment() {

    private lateinit var viewModel: ListDogViewModel
    private val dogsListAdapter = DogsListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_dogs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // instance du viewModel dans le fragment
        viewModel = ViewModelProviders.of(this).get(ListDogViewModel::class.java)
        viewModel.refresh()

        // recyclerview
        dogsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogsListAdapter
        }

        // refresh vers le bas
        refreshLayout.setOnRefreshListener {
            dogsList.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            viewModel.refresh()
            refreshLayout.isRefreshing = false
        }

        observeViewModel()
    }

    // Observe les données renvoyées du viewModel
    fun observeViewModel() {
        viewModel.dogs.observe(this, Observer { dogs ->
            // check si la liste n'est pas vide
            dogs?.let {
                dogsList.visibility = View.VISIBLE
                dogsListAdapter.updateDogList(dogs)
            }
        })
        // message erreur
        viewModel.dogsLoadError.observe(this, Observer { isError ->
            isError?.let {
                listError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
        // spinner
        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                // cacher la liste pendant le chargement
                if (it) {
                    dogsList.visibility = View.GONE
                    listError.visibility = View.GONE
                }
            }
        })
    }

}
