package com.waseefakhtar.marsphotosapp.presentation.photo_info_list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.waseefakhtar.marsphotosapp.R
import com.waseefakhtar.marsphotosapp.domain.model.PhotoInfo

class PhotoInfoAdapter(
    private val layoutInflater: LayoutInflater,
    private val onPhotoInfoClick: (id: Int, rover: String) -> Unit
) : RecyclerView.Adapter<PhotoInfoViewHolder>() {

    private var photoInfoList = mutableListOf<PhotoInfo>()

    override fun getItemCount(): Int = photoInfoList.size
    override fun onBindViewHolder(holder: PhotoInfoViewHolder, position: Int) = holder.bind(photoInfoList[position])
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoInfoViewHolder = PhotoInfoViewHolder(layoutInflater, parent, onPhotoInfoClick)

    fun add(weatherList: List<PhotoInfo>) {
        this.photoInfoList.addAll(weatherList)
        notifyDataSetChanged()
    }

    fun clear() {
        this.photoInfoList.clear()
        notifyDataSetChanged()
    }
}

class PhotoInfoViewHolder(
    layoutInflater: LayoutInflater,
    parentView: ViewGroup,
    private val onWeatherClick: (id: Int, rover: String) -> Unit
) : RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.item_photo_info, parentView, false)) {

    private val roverTextView: TextView = itemView.findViewById(R.id.roverValue)
    private val earthDateTextView: TextView = itemView.findViewById(R.id.earthDateValue)
    private val cameraValue: TextView = itemView.findViewById(R.id.cameraValue)

    fun bind(photoInfo: PhotoInfo) {
        itemView.setOnClickListener { onWeatherClick(photoInfo.id, photoInfo.rover) }
        roverTextView.text = photoInfo.rover
        earthDateTextView.text = photoInfo.earthDate
        cameraValue.text = photoInfo.camera
    }
}