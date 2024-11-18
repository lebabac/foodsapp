package com.example.food_ordering_app

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(
    private val context: Context,
    private val orderList: List<Order>,
    private val onApproveClick: (Order) -> Unit
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewOrderId: TextView = itemView.findViewById(R.id.textViewOrderId)
        val textViewOrderDetails: TextView = itemView.findViewById(R.id.textViewOrderDetails)
        val textViewFullName: TextView = itemView.findViewById(R.id.textViewFullName)
        val textViewPhoneNumber: TextView = itemView.findViewById(R.id.textViewPhoneNumber)
        val textViewAddress: TextView = itemView.findViewById(R.id.textViewAddress)
        val textViewPaymentMethod: TextView = itemView.findViewById(R.id.textViewPaymentMethod)
        val textViewTotalPrice: TextView = itemView.findViewById(R.id.textViewTotalPrice)
        val textViewFoodName: TextView = itemView.findViewById(R.id.textViewFoodName)
        val textViewFoodPrice: TextView = itemView.findViewById(R.id.textViewFoodPrice)
        val textViewQuantity: TextView = itemView.findViewById(R.id.textViewQuantity)
        val textViewStatus: TextView = itemView.findViewById(R.id.textViewStatus)
        val buttonApproveOrder: Button = itemView.findViewById(R.id.buttonApproveOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val currentItem = orderList[position]
        holder.textViewOrderId.text = "Order ID: ${currentItem.id}"
        holder.textViewOrderDetails.text = "User ID: ${currentItem.userId}, Food ID: ${currentItem.foodId}"
        holder.textViewFullName.text = "Full Name: ${currentItem.fullName}"
        holder.textViewPhoneNumber.text = "Phone Number: ${currentItem.phoneNumber}"
        holder.textViewAddress.text = "Address: ${currentItem.address}"
        holder.textViewPaymentMethod.text = "Payment Method: ${currentItem.paymentMethod}"
        holder.textViewTotalPrice.text = "Total Price: ${currentItem.totalPrice}"
        holder.textViewFoodName.text = "Food Name: ${currentItem.foodName}"
        holder.textViewFoodPrice.text = "Food Price: ${currentItem.foodPrice}"
        holder.textViewQuantity.text = "Quantity: ${currentItem.quantity}"
        holder.textViewStatus.text = "Status: ${currentItem.status}"

        holder.buttonApproveOrder.setOnClickListener {
            onApproveClick(currentItem)
        }
    }

    override fun getItemCount() = orderList.size
}