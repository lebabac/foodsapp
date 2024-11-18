package com.example.food_ordering_app

data class Food(
    val id: Int,
    val name: String,
    val details: String,
    val price: Double,
    val location: String,
    val image: ByteArray

)

