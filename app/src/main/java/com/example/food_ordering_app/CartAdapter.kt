package com.example.food_ordering_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartAdapter(private var cartItems: List<CartItem>, private val dbHelper: DatabaseHelper, private val listener: OnCartItemChangeListener) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    var selectedItems = mutableSetOf<CartItem>()

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemNameTextView: TextView = itemView.findViewById(R.id.itemNameTextView)
        val itemPriceTextView: TextView = itemView.findViewById(R.id.itemPriceTextView)
        val itemQuantityTextView: TextView = itemView.findViewById(R.id.itemQuantityTextView)
        val itemImageView: ImageView = itemView.findViewById(R.id.itemImageView)
        val buttonDecrease: Button = itemView.findViewById(R.id.buttonDecrease)
        val buttonIncrease: Button = itemView.findViewById(R.id.buttonIncrease)
        val buttonDelete: Button = itemView.findViewById(R.id.buttonDelete)
        val itemCheckbox: CheckBox = itemView.findViewById(R.id.itemCheckbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartItems[position]
        val food = dbHelper.getFoodById(currentItem.foodId)

        food?.let {
            holder.itemNameTextView.text = it.name
            holder.itemPriceTextView.text = "$${it.price}"
            holder.itemQuantityTextView.text = currentItem.quantity.toString()

            // Load hình ảnh từ mảng byte bằng Glide
            Glide.with(holder.itemView.context)
                .load(it.image)
                .placeholder(R.drawable.background) // Ảnh placeholder nếu không có hình ảnh
                .into(holder.itemImageView)

            holder.buttonIncrease.setOnClickListener {
                updateQuantity(currentItem, currentItem.quantity + 1)
            }

            holder.buttonDecrease.setOnClickListener {
                if (currentItem.quantity > 0) {
                    updateQuantity(currentItem, currentItem.quantity - 1)
                }
            }

            holder.buttonDelete.setOnClickListener {
                deleteCartItem(currentItem)
            }

            holder.itemCheckbox.setOnCheckedChangeListener(null) // Clear previous listener to prevent unwanted calls
            holder.itemCheckbox.isChecked = selectedItems.contains(currentItem)

            holder.itemCheckbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedItems.add(currentItem)
                } else {
                    selectedItems.remove(currentItem)
                }
                listener.onCartItemChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    fun getSelectedItems(): List<CartItem> {
        return selectedItems.toList() // Chuyển đổi selectedItems từ MutableSet sang List
    }

    fun getTotalPrice(): Double {
        var totalPrice = 0.0
        for (cartItem in cartItems) {
            if (selectedItems.contains(cartItem)) {
                val food = dbHelper.getFoodById(cartItem.foodId)
                food?.let {
                    totalPrice += it.price * cartItem.quantity
                }
            }
        }
        return totalPrice
    }

    fun updateCartItems(newCartItems: List<CartItem>) {
        cartItems = newCartItems
        notifyDataSetChanged()
    }

    private fun updateQuantity(cartItem: CartItem, newQuantity: Int) {
        dbHelper.updateCartItem(cartItem.id, newQuantity)
        cartItem.quantity = newQuantity
        notifyDataSetChanged()
        listener.onCartItemChanged()
    }

    private fun deleteCartItem(cartItem: CartItem) {
        dbHelper.deleteCartItem(cartItem.id)
        cartItems = cartItems.filter { it.id != cartItem.id }
        notifyDataSetChanged()
        listener.onCartItemChanged()
    }

    interface OnCartItemChangeListener {
        fun onCartItemChanged()
    }
}
