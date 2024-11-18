package com.example.food_ordering_app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ManageOrdersActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderAdapter
    private var orderList = mutableListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_orders)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.rvOrders)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = OrderAdapter(this, orderList, this::onApproveClick)
        recyclerView.adapter = adapter

        // Load orders from database
        loadOrders()
    }

    private fun loadOrders() {
        orderList.clear()
        orderList.addAll(dbHelper.getPendingOrders())
        adapter.notifyDataSetChanged()
    }
    private fun onApproveClick(order: Order) {
        if (dbHelper.updateOrderStatus(order.id, "approved")) {
            loadOrders()
        } else {
            Toast.makeText(this, "Không thể cập nhật trạng thái đơn hàng", Toast.LENGTH_SHORT).show()
        }
    }


    // Add menu to navigate to order history
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_manage_orders, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_view_approved_orders -> {
                startActivity(Intent(this, ApprovedOrdersActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
