package com.example.petshopsensedia

class CartProduct(
    val productList: List<CartProducts>
)

data class CartProducts(
    val id: Int,
    val description: String,
    val weight: String,
    val amount: String,
    var totalAmount: String,
    var quantitySelected: Int,
    val imageUrl: String
)