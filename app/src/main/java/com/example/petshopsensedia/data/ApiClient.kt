package com.example.petshopsensedia.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory;

object ApiClient {
    private const val BASE_URL = "https://run.mocky.io/v3/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val petShopApi: PetShopApi by lazy {
        retrofit.create(PetShopApi::class.java)
    }
}