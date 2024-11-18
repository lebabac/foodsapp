// ApprovedOrdersActivity.kt
package com.example.food_ordering_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ApprovedOrdersActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ApprovedOrderAdapter
    private var orderList = mutableListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approved_orders)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewOrderHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ApprovedOrderAdapter(this, orderList)
        recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        // Load approved orders from database
        loadApprovedOrders()
    }

    private fun loadApprovedOrders() {
        orderList.clear()
        orderList.addAll(dbHelper.getApprovedOrders())
        adapter.notifyDataSetChanged()
    }
}
