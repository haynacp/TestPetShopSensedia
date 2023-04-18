package com.example.petshopsensedia.data

import com.example.petshopsensedia.Product
import retrofit2.Call
import retrofit2.http.GET

interface PetShopApi {

    @GET("159ec295-7c6d-43d9-a48f-7023198f5145")
    fun getProducts(): Call<Product>
}