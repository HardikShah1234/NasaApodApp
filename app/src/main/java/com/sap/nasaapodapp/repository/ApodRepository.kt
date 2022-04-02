package com.sap.nasaapodapp.repository

import com.sap.nasaapodapp.data.api.NasaApi
import com.sap.nasaapodapp.data.model.NasaPhotos
import retrofit2.Response
import javax.inject.Inject

class ApodRepository @Inject constructor(
    private val nasaApi: NasaApi
) {
    suspend fun getData(count: Int): Response<NasaPhotos> =
        nasaApi.getRandomApodData(count)
}