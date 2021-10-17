package com.waseefakhtar.marsphotosapp.presentation.photo_info_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private fun initViews() {
        with(activity as AppCompatActivity) {
            supportActionBar?.setTitle(R.string.mars_photos_app)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.show()
        }

        binding.recyclerView.adapter = adapter
    }

    private fun onObserve() {
        viewModel.photoInfoListState().observe(viewLifecycleOwner, { photoInfoListState -> refreshState(photoInfoListState) })
        viewModel.onLoad()
    }

    private fun refreshState(photoInfoListState: Resource<List<PhotoInfo>>) {
        when (photoInfoListState) {
            is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
            is Resource.Success -> {
                binding.progressBar.visibility = View.GONE
                adapter.add(photoInfoListState.data ?: listOf())
            }
            is Resource.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, photoInfoListState.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onPhotoInfoClick(id: Int) {
        val action = PhotoInfoListFragmentDirections.actionPhotoInfoListFragmentToPhotoDetailFragment(id)
        view?.findNavController()?.navigate(action)
    }
}