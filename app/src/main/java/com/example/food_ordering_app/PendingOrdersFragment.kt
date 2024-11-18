package com.example.food_ordering_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PendingOrdersFragment : Fragment(R.layout.fragment_pending_orders) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PendingOrdersAdapter
    private var orderList = listOf<Order>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewPendingOrders)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Lấy userId từ SharedPreferences
        val userId = SharedPreferencesHelper.getCurrentUserId(requireContext())

        if (userId != -1L) {
            // Lấy danh sách đơn hàng của người dùng hiện tại với trạng thái 'pending'
            orderList = DatabaseHelper(requireContext()).getOrdersByStatusAndUserId("pending", userId)


        }

        adapter = PendingOrdersAdapter(orderList) { order ->
            val intent = Intent(requireContext(), OrderDetailActivity::class.java)
            intent.putExtra("ORDER_KEY", order)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }
}
