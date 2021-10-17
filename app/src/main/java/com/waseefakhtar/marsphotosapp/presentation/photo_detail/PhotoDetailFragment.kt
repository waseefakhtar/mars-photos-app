package com.waseefakhtar.marsphotosapp.presentation.photo_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import coil.load
import com.waseefakhtar.marsphotosapp.R
import com.waseefakhtar.marsphotosapp.common.Resource
import com.waseefakhtar.marsphotosapp.databinding.FragmentPhotoDetailBinding
import com.waseefakhtar.marsphotosapp.domain.model.PhotoDetail
import com.waseefakhtar.marsphotosapp.presentation.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoDetailFragment : BindingFragment<FragmentPhotoDetailBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPhotoDetailBinding::inflate

    val args: PhotoDetailFragmentArgs by navArgs()
    private val viewModel: PhotoDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        onObserve()
    }

    private fun initViews() {
        with(activity as AppCompatActivity) {
            supportActionBar?.setTitle(R.string.mars_photos_app)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.show()
        }
    }

    private fun onObserve() {
        viewModel.photoDetailState().observe(viewLifecycleOwner, { photoDetailState -> refreshState(photoDetailState) })
        viewModel.onLoad(args.id, args.rover)
    }

    private fun refreshState(photoDetailState: Resource<PhotoDetail>) {
        when (photoDetailState) {
            is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
            is Resource.Success -> {
                with(binding) {
                    photoDetailState.data?.let { photoDetail ->
                        progressBar.visibility = View.GONE
                        roverValue.text = photoDetail.rover
                        launchDateValue.text = photoDetail.launchDate
                        landingDateValue.text = photoDetail.landingDate
                        statusValue.text = photoDetail.status
                        imageView.load(photoDetail.imgSrc)
                    }
                }

            }
            is Resource.Error -> {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, photoDetailState.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}