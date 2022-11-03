package com.testtask.giphy.giffer.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.testtask.giphy.giffer.data.models.ImageData
import com.testtask.giphy.giffer.databinding.GifItemBinding
import com.testtask.giphy.giffer.viewmodels.GalleryViewModel

class GifAdapter (
    private val listener: OnItemClickListener
) :
    PagingDataAdapter<ImageData, GifAdapter.GifViewHolder>(UserComparator) {

//    private lateinit var binding: GifItemBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GifViewHolder {
      val  binding =
            GifItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GifViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    inner class GifViewHolder(val binding: GifItemBinding) : RecyclerView.ViewHolder(binding.root) {
        //Click on recyclerView item
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)

                    }
                }
            }
        }

        fun bind(image: ImageData) {
            binding.apply {
                Glide.with(itemView)
                    .load(image.images.fixedHeightSmall.fixedHeightSmallUrl)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .error(R.drawable.ic_error)
                    .into(imageView)

                tvGif.text = image.title
            }
        }
    }

    fun getGifId(position: Int) = getItem(position)?.id

    object UserComparator : DiffUtil.ItemCallback<ImageData>() {
        override fun areItemsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
            return oldItem == newItem
        }
    }

    interface OnItemClickListener {
        fun onItemClick(image: ImageData)
    }
}