package com.example.petshopsensedia

import com.example.petshopsensedia.data.PetShopApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class PetShopApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var productApi: PetShopApi

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Configura o Retrofit para utilizar o servidor mock
        val gson: Gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        // Cria a instância da interface da API
        productApi = retrofit.create(PetShopApi::class.java)
    }

    @After
    fun teardown() {
        // Encerra o servidor mock
        mockWebServer.shutdown()
    }

    @Test
    fun testGetProducts() {
        // Configura a resposta mock da API
        val response = MockResponse()
            .setResponseCode(200)
            .setBody("{\"productList\":[{\"id\":1,\"description\":\"Product 1\",\"amount\":\"10,00\",\"weight\":\"1kg\",\"imageUrl\":\"https://via.placeholder.com/150\",\"quantity\":10}]}")
            .setBodyDelay(500, TimeUnit.MILLISECONDS)
        mockWebServer.enqueue(response)

        // Executa o método da API e verifica se a resposta é vazia
        val call: Call<Product> = productApi.getProducts()
        val result = call.execute().body()
        assert(result != null && !result.productList.isNullOrEmpty())

    }
}
