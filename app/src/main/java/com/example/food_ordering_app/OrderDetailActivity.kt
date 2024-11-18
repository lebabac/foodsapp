package com.example.food_ordering_app

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OrderDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        val textViewOrderId = findViewById<TextView>(R.id.textViewOrderId)
        val textViewOrderDetails = findViewById<TextView>(R.id.textViewOrderDetails)
        val textViewTotalPrice = findViewById<TextView>(R.id.textViewTotalPrice)
        val textViewFullName = findViewById<TextView>(R.id.textViewFullName)
        val textViewPhoneNumber = findViewById<TextView>(R.id.textViewPhoneNumber)
        val textViewAddress = findViewById<TextView>(R.id.textViewAddress)
        val textViewPaymentMethod = findViewById<TextView>(R.id.textViewPaymentMethod)
        val imageViewFood = findViewById<ImageView>(R.id.imageViewFood)

        // Lấy dữ liệu từ Intent (nếu có)
        val order = intent.getParcelableExtra<Order>("ORDER_KEY")

        // Kiểm tra xem order có null không
        if (order != null) {
            // Hiển thị dữ liệu từ đối tượng Order lên các TextView
            textViewOrderId.text = "Order ID: ${order.id}"
            textViewOrderDetails.text = "Details: ${order.foodName} x ${order.quantity}"
            textViewTotalPrice.text = "Total Price: ${order.totalPrice}"
            textViewFullName.text = "Full Name: ${order.fullName}"
            textViewPhoneNumber.text = "Phone Number: ${order.phoneNumber}"
            textViewAddress.text = "Address: ${order.address}"
            textViewPaymentMethod.text = "Payment Method: ${order.paymentMethod}"

            // Hiển thị ảnh món ăn
            order.foodImage?.let {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                imageViewFood.setImageBitmap(bitmap)
            }
        } else {
            // Nếu order là null, hiển thị thông báo hoặc xử lý khác tùy ý
            textViewOrderId.text = "Order ID: Not Available"
            textViewOrderDetails.text = "Details: Not Available"
            textViewTotalPrice.text = "Total Price: Not Available"
            textViewFullName.text = "Full Name: Not Available"
            textViewPhoneNumber.text = "Phone Number: Not Available"
            textViewAddress.text = "Address: Not Available"
            textViewPaymentMethod.text = "Payment Method: Not Available"
        }
    }
}
