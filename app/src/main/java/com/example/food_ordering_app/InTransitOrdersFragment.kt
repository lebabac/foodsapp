package com.example.food_ordering_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InTransitOrdersFragment : Fragment(R.layout.fragment_in_transit_orders) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: InTransitOrderAdapter
    private var orderList = listOf<Order>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerViewInTransitOrders)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val userId = SharedPreferencesHelper.getCurrentUserId(requireContext())
        if (userId != -1L) {
            // Lấy danh sách đơn hàng của người dùng hiện tại với trạng thái 'approved'
            orderList = DatabaseHelper(requireContext()).getOrdersByStatusAndUserId("approved", userId)
        }

        // Thiết lập adapter cho RecyclerView
        adapter = InTransitOrderAdapter(orderList,
            onItemClick = { order ->
                val intent = Intent(requireContext(), OrderDetailActivity::class.java)
                intent.putExtra("ORDER_KEY", order)
                startActivity(intent)
            },
            onReceivedClick = { order ->
                // Cập nhật trạng thái của đơn hàng thành 'delivered' trong cơ sở dữ liệu
                DatabaseHelper(requireContext()).updateOrderStatus(order.id, "delivered")

                // Cập nhật lại trạng thái trong orderList
                val index = orderList.indexOfFirst { it.id == order.id }
                if (index != -1) {
                    orderList[index].status = "delivered"
                }

                val confirmationMessage = "Đơn hàng ${order.id} đã được giao thành công."
                Toast.makeText(context, confirmationMessage, Toast.LENGTH_SHORT).show()

                // Cập nhật RecyclerView để hiển thị trạng thái mới
                adapter.notifyDataSetChanged()
            },
            notifyDataSetChangedListener = {
                adapter.notifyDataSetChanged() // Cập nhật RecyclerView
            }
        )
        recyclerView.adapter = adapter
    }
}
