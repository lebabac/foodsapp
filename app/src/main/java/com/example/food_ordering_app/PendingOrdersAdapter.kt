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

class PendingOrdersAdapter(
    private val orderList: List<Order>,
    private val onItemClick: (Order) -> Unit
) : RecyclerView.Adapter<PendingOrdersAdapter.PendingOrderViewHolder>() {

    inner class PendingOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewFood: ImageView = itemView.findViewById(R.id.imageViewFood)
        val textViewFoodName: TextView = itemView.findViewById(R.id.textViewFoodName)
        val textViewFoodPrice: TextView = itemView.findViewById(R.id.textViewFoodPrice)
        val textViewQuantity: TextView = itemView.findViewById(R.id.textViewQuantity)
        val buttonDetails: Button = itemView.findViewById(R.id.buttonDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pending_order, parent, false)
        return PendingOrderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PendingOrderViewHolder, position: Int) {
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

        // Xử lý sự kiện click cho nút "Chi tiết"
        holder.buttonDetails.setOnClickListener {
            onItemClick(currentItem)
        }

    }

    override fun getItemCount() = orderList.size
}