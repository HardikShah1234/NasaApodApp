package com.sap.nasaapodapp.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sap.nasaapodapp.base.BaseFragment
import com.sap.nasaapodapp.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val description = arguments?.getString("DESCRIPTION")
        val date = arguments?.getString("DATE")
        val copyright = arguments?.getString("COPYRIGHT")
        binding.apply {
            txtDate.text = "Date = ${date.orEmpty()}"
            txtDate.setTextColor(Color.BLACK)

            txtDescription.text = "Description = ${description.orEmpty()}"
            txtDescription.setTextColor(Color.BLACK)

            txtCopyRight.text = copyright.orEmpty()
            txtCopyRight.setTextColor(Color.BLACK)

        }
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailBinding {
        return FragmentDetailBinding.inflate(inflater, container, false)
    }
}