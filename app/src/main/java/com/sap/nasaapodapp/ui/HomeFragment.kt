package com.sap.nasaapodapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sap.nasaapodapp.R
import com.sap.nasaapodapp.base.BaseFragment
import com.sap.nasaapodapp.databinding.FragmentHomeBinding
import com.sap.nasaapodapp.repository.ApodAdapter
import com.sap.nasaapodapp.utils.NetworkResult
import com.sap.nasaapodapp.utils.RecyclerViewInterface
import com.sap.nasaapodapp.viewModel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), RecyclerViewInterface {

    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var apodAdapter: ApodAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridLayoutManager = object : GridLayoutManager(this.context, 1) {
            override fun canScrollHorizontally(): Boolean {
                return !binding.rvPhotos.layoutManager!!.isSmoothScrolling
            }
        }
        apodAdapter = ApodAdapter(
            { findNavController().navigate(R.id.action_homeFragment_to_detailFragment2) },
            {
                val imageCurrentPosition =
                    (binding.rvPhotos.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                if (imageCurrentPosition < apodAdapter.itemCount - 1) {
                    binding.rvPhotos.smoothScrollToPosition(imageCurrentPosition + 1)
                }
            }, {
                val imageCurrentPosition =
                    (binding.rvPhotos.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                if (imageCurrentPosition > 0) {
                    binding.rvPhotos.smoothScrollToPosition(imageCurrentPosition - 1)
                }
            },
            this
        )
        bind(binding, gridLayoutManager)
    }

    private fun bind(binding: FragmentHomeBinding, gridLayoutManager: GridLayoutManager) {
        binding.apply {
            rvPhotos.setHasFixedSize(true)
            rvPhotos.adapter = apodAdapter
            rvPhotos.layoutManager = gridLayoutManager
        }
        viewModel.apodImageResponse.observe(this as LifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    val responseBody = it.data
                    apodAdapter.submitList(responseBody)
                }
                is NetworkResult.Error -> {
                    binding.apply {
                        pbLoadingItems.isVisible = false
                        rvPhotos.isVisible = false
                    }
                }
                is NetworkResult.Loading -> {
                    binding.apply {
                        pbLoadingItems.isVisible = true
                        rvPhotos.isVisible = false
                    }
                }
                else -> Unit
            }
        }
    }


    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onItemClick(position: Int) {
        lifecycleScope.launch(Dispatchers.Unconfined) {
            viewModel.apodData.observe(viewLifecycleOwner) {
                val bundle = Bundle()
                bundle.putInt("POS", position)
                bundle.putString("DATE", it[position].date)
                bundle.putString("DESCRIPTION", it[position].explanation)
                bundle.putString("COPYRIGHT", it[position].copyright)

                val transaction = parentFragmentManager.beginTransaction()
                val detailFragment = DetailFragment()
                detailFragment.arguments = bundle

                transaction.replace(R.id.rootLayout, detailFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }.isCompleted
    }

}
