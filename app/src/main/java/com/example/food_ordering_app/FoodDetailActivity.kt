package com.example.food_ordering_app

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FoodDetailActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var btnAddToCart: Button
    private lateinit var btnBuyNow: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_detail)

        dbHelper = DatabaseHelper(this)
        btnAddToCart = findViewById(R.id.btnAddToCart)
        btnBuyNow = findViewById(R.id. btnBuyNow)

        // Lấy dữ liệu của món ăn từ Intent
        val foodId = intent.getIntExtra("food_id", -1)
        val food = dbHelper.getFoodById(foodId)

        // Hiển thị thông tin chi tiết của món ăn
        food?.let { displayFoodDetail(it) }

        btnAddToCart.setOnClickListener {
            if (food == null) {
                Toast.makeText(this, "ID món ăn không hợp lệ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = dbHelper.addToCart(foodId, 1) // Thêm sản phẩm với số lượng là 1
            if (result > 0) {
                Toast.makeText(this, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Thêm vào giỏ hàng thất bại!", Toast.LENGTH_SHORT).show()
            }
        }
        btnBuyNow.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            intent.putExtra("foodId", foodId) // Truyền foodId vào intent
            startActivity(intent)
        }
    }

    private fun displayFoodDetail(food: Food) {
        findViewById<TextView>(R.id.textViewFoodName).text = food.name
        findViewById<TextView>(R.id.textViewFoodDetails).text = food.details
        findViewById<TextView>(R.id.textViewFoodPrice).text = "$${food.price}"
        findViewById<TextView>(R.id.textViewLocation).text = food.location
        // Hiển thị hình ảnh của món ăn (nếu có)
        food.image?.let {
            findViewById<ImageView>(R.id.imageViewFoodDetail).setImageBitmap(BitmapFactory.decodeByteArray(it, 0, it.size))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}
