package com.example.food_ordering_app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    private lateinit var category: ImageView
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var foodAdapter: FoodAdapter
    private lateinit var recyclerViewSearchResults: RecyclerView
    private lateinit var editTextSearch: EditText
    private lateinit var acc: ImageView
    private lateinit var order: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        category = findViewById(R.id.categoryIcon)
        acc = findViewById(R.id.accountIcon)
        order = findViewById(R.id.Order)



        databaseHelper = DatabaseHelper(this)
        recyclerViewSearchResults = findViewById(R.id.recyclerViewSearchResults)
        editTextSearch = findViewById(R.id.editTextSearch)

        foodAdapter = FoodAdapter(this)
        recyclerViewSearchResults.layoutManager = LinearLayoutManager(this)
        recyclerViewSearchResults.adapter = foodAdapter

        loadAllFoods()

        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                foodAdapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable?) {}
        })
        category.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        acc.setOnClickListener {
            startActivity(Intent(this, MoreActivity::class.java))
        }
        order.setOnClickListener {
            startActivity(Intent(this, CustomerOrdersActivity::class.java))
        }
    }

    private fun loadAllFoods() {
        val foodList = databaseHelper.getAllFood()
        foodAdapter.submitList(foodList)
    }

}
