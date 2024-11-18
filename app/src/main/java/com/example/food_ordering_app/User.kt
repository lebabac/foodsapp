package com.example.food_ordering_app
data class User(
    val id: Long = 0,
    val username: String,
    val phone: String,
    val email: String,
    val address: String,
    val password: String,
    val role:String
)
