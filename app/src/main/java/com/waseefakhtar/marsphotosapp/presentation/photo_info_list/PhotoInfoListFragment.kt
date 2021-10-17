package com.waseefakhtar.marsphotosapp.presentation.photo_info_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.waseefakhtar.marsphotosapp.R
import com.waseefakhtar.marsphotosapp.databinding.FragmentPhotoInfoListBinding
import com.waseefakhtar.marsphotosapp.presentation.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity
import com.waseefakhtar.marsphotosapp.common.Resource
import com.waseefakhtar.marsphotosapp.domain.model.PhotoInfo


@AndroidEntryPoint
class PhotoInfoListFragment : BindingFragment<FragmentPhotoInfoListBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPhotoInfoListBinding::inflate

    private val viewModel: PhotoInfoListViewModel by viewModels()
    private val adapter: PhotoInfoAdapter by lazy { PhotoInfoAdapter(layoutInflater, ::onPhotoInfoClick) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        onObserve()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.onLoad(
            when (item.itemId) {
                R.id.curiosity_item -> Rover.Curiosity
                R.id.opportunity_item -> Rover.Opportunity
                R.id.spirit_item -> Rover.Spirit
                else -> viewModel.currentRover
            }
        )
        return true
    }

    private fun initViews() {
        with(activity as AppCompatActivity) {
            supportActionBar?.setTitle(R.string.mars_photos_app)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.show()
        }
        setHasOptionsMenu(true)

        binding.recyclerView.adapter = adapter
    }

    private fun onObserve() {
        viewModel.photoInfoListState().observe(viewLifecycleOwner, { photoInfoListState -> refreshState(photoInfoListState) })
        viewModel.onLoad(viewModel.currentRover)
    }

    private fun refreshState(photoInfoListState: Resource<List<PhotoInfo>>) {
        when (photoInfoListState) {
            is Resource.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                adapter.clear()
            }
            is Resource.Success -> {
                binding.progressBar.visibility = View.GONE
                adapter.add(photoInfoListState.data)
            }
            is Resource.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, photoInfoListState.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onPhotoInfoClick(id: Int, rover: String) {
        val action = PhotoInfoListFragmentDirections.actionPhotoInfoListFragmentToPhotoDetailFragment(id, rover)
        view?.findNavController()?.navigate(action)
    }
}