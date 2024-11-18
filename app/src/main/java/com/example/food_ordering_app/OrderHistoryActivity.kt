package com.example.food_ordering_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderAdapter
    private var orderList = mutableListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewOrderHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = OrderAdapter(this, orderList, {})
        recyclerView.adapter = adapter

        // Load approved orders from database
        loadApprovedOrders()
    }

    private fun loadApprovedOrders() {
        orderList.clear()
        orderList.addAll(dbHelper.getApprovedOrders())
        adapter.notifyDataSetChanged()
    }
}
