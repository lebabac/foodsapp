package com.example.food_ordering_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FoodListActivity : AppCompatActivity() {

    private lateinit var recyclerViewFood: RecyclerView
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

        recyclerViewFood = findViewById(R.id.recyclerViewFood)
        dbHelper = DatabaseHelper(this)

        setupRecyclerView()
        loadFoodData()
    }

    private fun setupRecyclerView() {
        recyclerViewFood.layoutManager = GridLayoutManager(this, 2) // 2 columns
        foodAdapter = FoodAdapter(this)
        recyclerViewFood.adapter = foodAdapter
    }

    private fun loadFoodData() {
        val foodList = fetchFoodList()
        foodAdapter.submitList(foodList)
    }

    private fun fetchFoodList(): List<Food> {
        val db = dbHelper.readableDatabase
        val cursor = db.query("food", null, null, null, null, null, null)

        val foodList = ArrayList<Food>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val details = cursor.getString(cursor.getColumnIndexOrThrow("details"))
                val price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                val image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"))
                val location = cursor.getString(cursor.getColumnIndexOrThrow("location"))

                val food = Food(id, name, details, price, location, image)
                foodList.add(food)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return foodList
    }
}
