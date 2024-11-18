package com.example.food_ordering_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartActivity : AppCompatActivity(), CartAdapter.OnCartItemChangeListener {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var totalPriceTextView: TextView
    private lateinit var checkoutButton: Button
    private lateinit var selectAllButton: Button
    private lateinit var cartAdapter: CartAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        dbHelper = DatabaseHelper(this)

        // Khởi tạo userId


        // Ánh xạ các thành phần giao diện
        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        totalPriceTextView = findViewById(R.id.totalPriceTextView)
        checkoutButton = findViewById(R.id.checkoutButton)
        selectAllButton = findViewById(R.id.selectAllButton)

        // Thiết lập RecyclerView
        cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter(getCartItems(), dbHelper, this) // Truyền listener vào adapter
        cartRecyclerView.adapter = cartAdapter

        // Cập nhật tổng tiền
        updateTotalPrice()

        // Xử lý sự kiện khi nhấn nút Checkout
        checkoutButton.setOnClickListener {
            val selectedItems = cartAdapter.getSelectedItems()

            if (selectedItems.isNotEmpty()) {
                // Chuyển sang OrderActivity và truyền danh sách các món hàng đã chọn
                val intent = Intent(this, OrderActivity::class.java)
                intent.putParcelableArrayListExtra("selectedItems", ArrayList(selectedItems))
                startActivity(intent)

            } else {
                // Xử lý khi không có món hàng nào được chọn
                Toast.makeText(this, "Vui lòng chọn sản phẩm để thanh toán.", Toast.LENGTH_SHORT).show()
            }
        }

        // Xử lý sự kiện khi nhấn nút Chọn Tất cả
        selectAllButton.setOnClickListener {
            selectAllItems()
        }
    }

    private fun getCartItems(): List<CartItem> {
        // Lấy danh sách các món trong giỏ hàng từ cơ sở dữ liệu
        return dbHelper.getAllCartItems()
    }

    private fun updateTotalPrice() {
        val totalPrice = cartAdapter.getTotalPrice()
        totalPriceTextView.text = "Tổng cộng: $$totalPrice"
    }

    private fun selectAllItems() {
        val allItems = dbHelper.getAllCartItems()
        cartAdapter.selectedItems.clear()
        cartAdapter.selectedItems.addAll(allItems)
        cartAdapter.notifyDataSetChanged()
        updateTotalPrice()
    }

    override fun onCartItemChanged() {
        updateTotalPrice()
    }
}
