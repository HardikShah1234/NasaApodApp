package com.sap.nasaapodapp.repository


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sap.nasaapodapp.data.model.NasaPhotosItem
import com.sap.nasaapodapp.databinding.ItemPhotoListBinding
import com.sap.nasaapodapp.utils.RecyclerViewInterface

class ApodAdapter(
    private val onSelect: () -> Unit,
    private val onNextClick: () -> Unit,
    private val onPrevClick: () -> Unit,
    private val recyclerViewInterface: RecyclerViewInterface
) : ListAdapter<NasaPhotosItem, ApodAdapter.PhotoViewHolder>(PhotoDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding =
            ItemPhotoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }


    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class PhotoViewHolder(private val binding: ItemPhotoListBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.apply {
                btnDetailedScreen.setOnClickListener {
                    onSelect.invoke()
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        recyclerViewInterface.onItemClick(position)
                    }
                }
                btnNext.setOnClickListener {
                    onNextClick.invoke()
                }
                btnPrev.setOnClickListener {
                    onPrevClick.invoke()
                }
            }

            itemView.setOnClickListener(this)
        }

        fun bind(nasaPhotosItem: NasaPhotosItem) {
            binding.apply {
                Glide.with(itemView.context)
                    .load(nasaPhotosItem.url)
                    .into(cvIvPhotoPoster)
                textViewTitle.text = nasaPhotosItem.title
            }
        }

        override fun onClick(p0: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                recyclerViewInterface.onItemClick(position)
            }
        }
    }

    class PhotoDiffCallBack : DiffUtil.ItemCallback<NasaPhotosItem>() {
        override fun areItemsTheSame(oldItem: NasaPhotosItem, newItem: NasaPhotosItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NasaPhotosItem, newItem: NasaPhotosItem): Boolean {
            return oldItem.date == newItem.date
        }

    }

}