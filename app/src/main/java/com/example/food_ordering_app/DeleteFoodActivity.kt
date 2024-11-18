// DeleteFoodActivity.kt
package com.example.food_ordering_app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DeleteFoodActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etFoodId: EditText
    private lateinit var btnDeleteFood: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_food)

        dbHelper = DatabaseHelper(this)
        etFoodId = findViewById(R.id.etFoodId)
        btnDeleteFood = findViewById(R.id.btnDeleteFood)

        btnDeleteFood.setOnClickListener {
            val id = etFoodId.text.toString().toIntOrNull()
            if (id == null) {
                Toast.makeText(this, "ID món ăn không hợp lệ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = dbHelper.deleteFood(id)
            if (result > 0) {
                Toast.makeText(this, "Xóa món ăn thành công!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Xóa món ăn thất bại!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
