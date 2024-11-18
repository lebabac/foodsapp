package com.example.food_ordering_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartFragment : Fragment(), CartAdapter.OnCartItemChangeListener {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var totalPriceTextView: TextView
    private lateinit var checkoutButton: Button
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        dbHelper = DatabaseHelper(requireContext())

        // Ánh xạ các thành phần giao diện
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView)
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView)
        checkoutButton = view.findViewById(R.id.checkoutButton)

        // Thiết lập RecyclerView
        cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        cartAdapter = CartAdapter(getCartItems(), dbHelper, this) // Truyền listener vào adapter
        cartRecyclerView.adapter = cartAdapter

        // Cập nhật tổng tiền
        updateTotalPrice()

        // Xử lý sự kiện khi nhấn nút Checkout
        checkoutButton.setOnClickListener {
            // Thực hiện các hành động cần thiết khi nhấn nút Checkout, như thanh toán đơn hàng
            // ở đây bạn có thể gọi hàm xử lý thanh toán hoặc chuyển đến màn hình thanh toán
        }

        return view
    }



    private fun getCartItems(): List<CartItem> {
        // Lấy danh sách các món trong giỏ hàng từ cơ sở dữ liệu
        return dbHelper.getAllCartItems()
    }

    fun updateTotalPrice() {
        val totalPrice = cartAdapter.getTotalPrice()
        totalPriceTextView.text = "Total: $$totalPrice"
    }

    override fun onCartItemChanged() {
        updateTotalPrice()
    }
}
