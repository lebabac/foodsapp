// UserOrderAdapter.kt
package com.example.food_ordering_app

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserOrderAdapter(
    private val orderList: List<Order>,
    private val onItemClick: (Order) -> Unit,
    private val onReceivedClick: (Order) -> Unit
) : RecyclerView.Adapter<UserOrderAdapter.UserOrderViewHolder>() {

    inner class UserOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewFood: ImageView = itemView.findViewById(R.id.imageViewFood)
        val textViewFoodName: TextView = itemView.findViewById(R.id.textViewFoodName)
        val textViewFoodPrice: TextView = itemView.findViewById(R.id.textViewFoodPrice)
        val textViewQuantity: TextView = itemView.findViewById(R.id.textViewQuantity)
        val buttonReceived: Button = itemView.findViewById(R.id.buttonReceived)
        val buttonDetails: Button = itemView.findViewById(R.id.buttonDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserOrderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user_order, parent, false)
        return UserOrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserOrderViewHolder, position: Int) {
        val currentItem = orderList[position]


        // Hiển thị thông tin đơn hàng
        holder.textViewFoodName.text = currentItem.foodName
        holder.textViewFoodPrice.text = "Price: ${currentItem.foodPrice}"
        holder.textViewQuantity.text = "Quantity: ${currentItem.quantity}"

        // Hiển thị ảnh món ăn
        currentItem.foodImage?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            holder.imageViewFood.setImageBitmap(bitmap)
        }

        // Xử lý sự kiện click cho nút "Đã nhận được hàng"
        holder.buttonReceived.setOnClickListener {
            onReceivedClick(currentItem)
        }

        // Xử lý sự kiện click cho nút "Chi tiết"
        holder.buttonDetails.setOnClickListener {
            onItemClick(currentItem)
        }

    }

    override fun getItemCount() = orderList.size
}
