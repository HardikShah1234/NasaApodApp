package com.sap.nasaapodapp.data.api

import com.sap.nasaapodapp.data.model.NasaPhotos
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApi {
    @GET("planetary/apod?api_key=Yelp93s0xpEjPOQ6If03aTsIUyHT4sdD46Jwe5gC")
    suspend fun getRandomApodData(
        @Query("count") count: Int,
    ): Response<NasaPhotos>
}