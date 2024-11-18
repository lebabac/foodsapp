// AdminActivity.kt
package com.example.food_ordering_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {

    private lateinit var btnAddFood: Button
    private lateinit var btnUpdateFood: Button
    private lateinit var btnDeleteFood: Button
    private lateinit var btnManageOrders: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        btnAddFood = findViewById(R.id.btnAddFood)
        btnUpdateFood = findViewById(R.id.btnUpdateFood)
        btnDeleteFood = findViewById(R.id.btnDeleteFood)
        btnManageOrders = findViewById(R.id.btnManageOrders)



        btnAddFood.setOnClickListener {
            startActivity(Intent(this, AddFoodActivity::class.java))
        }

        btnUpdateFood.setOnClickListener {
            startActivity(Intent(this, UpdateFoodActivity::class.java))
        }

        btnDeleteFood.setOnClickListener {
            startActivity(Intent(this, DeleteFoodActivity::class.java))
        }

        btnManageOrders.setOnClickListener {
            startActivity(Intent(this, ManageOrdersActivity::class.java))
        }


    }
}
