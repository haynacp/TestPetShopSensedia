package com.example.petshopsensedia.viewmodel

import androidx.lifecycle.ViewModel
import com.example.petshopsensedia.CartProducts
import com.example.petshopsensedia.Products

class CartViewModel : ViewModel() {

    private val _productList = mutableListOf<Products>()
    var productList: List<CartProducts> = listOf()
        get() = _productList.map {
            CartProducts(
                id = it.id,
                description = it.description,
                weight = it.weight,
                amount = it.amount,
                totalAmount = it.amount,
                quantitySelected = it.quantitySelected,
                imageUrl = it.imageUrl
            )
        }

    fun addProductToCart(product: Products) {
        val existingProduct = _productList.find { it.id == product.id }
        if (existingProduct != null) {
            existingProduct.quantitySelected += product.quantitySelected
        } else {
            _productList.add(product)
        }
    }

    fun clearList() {
        _productList.clear()
    }
}

