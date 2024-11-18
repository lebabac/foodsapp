package com.example.food_ordering_app

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class OrderActivity : AppCompatActivity() {
    private lateinit var editTextFullName: EditText
    private lateinit var editTextPhoneNumber: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var editTextQuantity: EditText
    private lateinit var radioGroupPaymentMethod: RadioGroup
    private lateinit var buttonPlaceOrder: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        dbHelper = DatabaseHelper(this)

        editTextFullName = findViewById(R.id.editTextFullName)
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber)
        editTextAddress = findViewById(R.id.editTextAddress)
        editTextQuantity = findViewById(R.id.editTextQuantity)
        radioGroupPaymentMethod = findViewById(R.id.radioGroupPaymentMethod)
        buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder)

        buttonPlaceOrder.setOnClickListener {
            val fullName = editTextFullName.text.toString()
            val phoneNumber = editTextPhoneNumber.text.toString()
            val address = editTextAddress.text.toString()
            val quantity = editTextQuantity.text.toString().toInt()

            val paymentMethodId = radioGroupPaymentMethod.checkedRadioButtonId
            val radioButtonSelected: RadioButton = findViewById(paymentMethodId)
            val paymentMethod = radioButtonSelected.text.toString()



            val userId = SharedPreferencesHelper.getCurrentUserId(this)


            val foodId = intent.getIntExtra("foodId", -1)


            val foodPrice = dbHelper.getFoodPrice(foodId)

            val selectedItems = intent.getParcelableArrayListExtra<CartItem>("selectedItems")
            val orders = mutableListOf<Order>()
            if (selectedItems != null && selectedItems.isNotEmpty()) {
                for (item in selectedItems) {
                    val foodId = item.foodId
                    val foodPrice = dbHelper.getFoodPrice(foodId)
                    val totalPrice = item.quantity * foodPrice
                    val order = Order(
                        0,
                        fullName,
                        phoneNumber,
                        address,
                        item.quantity,
                        paymentMethod,
                        totalPrice,
                        userId.toInt(),
                        foodId,
                        "pending"
                    )
                    orders.add(order)
                }
            }
            placeOrders(orders)
            if (selectedItems != null && selectedItems.isNotEmpty()) {
                val cartItemIds = selectedItems.map { it.id }
                dbHelper.deleteCartItems(cartItemIds)}
            // Tính tổng giá trị đơn hàng
            val totalPrice = quantity * foodPrice
            val status="pending"

            if (fullName.isNotEmpty() && phoneNumber.isNotEmpty() && address.isNotEmpty() && quantity > 0) {
                placeOrder(fullName, phoneNumber, address, quantity, paymentMethod, totalPrice,
                    userId.toInt(), foodId,status)
            } else {
                Toast.makeText(this, "Please fill in all fields with valid information", Toast.LENGTH_SHORT).show()
            }
        }




    }

    private fun placeOrder(fullName: String, phoneNumber: String, address: String, quantity: Int, paymentMethod: String, totalPrice: Double, userId: Int, foodId: Int,status: String) {
        val order = Order(0, fullName, phoneNumber, address, quantity, paymentMethod, totalPrice, userId, foodId,status)
        val result = dbHelper.addOrder(order)

        if (result != -1L) {
            Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show()
        }
    }
    private fun placeOrders(orders: List<Order>) {
        for (order in orders) {
            val result = dbHelper.addOrder(order)
            if (result == -1L) {
                Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show()
                return
            }
        }
        Toast.makeText(this, "Orders placed successfully", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

}
