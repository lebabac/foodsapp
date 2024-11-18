package com.example.food_ordering_app

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayOutputStream

class UpdateFoodActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etFoodId: EditText
    private lateinit var etFoodName: EditText
    private lateinit var etFoodDetails: EditText
    private lateinit var etFoodPrice: EditText
    private lateinit var etFoodLocation: EditText
    private lateinit var btnChooseImage: Button
    private lateinit var btnUpdateFood: Button
    private lateinit var imageView: ImageView
    private var foodImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_food)

        dbHelper = DatabaseHelper(this)
        etFoodId = findViewById(R.id.etFoodId)
        etFoodName = findViewById(R.id.etFoodName)
        etFoodDetails = findViewById(R.id.etFoodDetails)
        etFoodPrice = findViewById(R.id.etFoodPrice)
        etFoodLocation = findViewById(R.id.etFoodLocation)
        btnChooseImage = findViewById(R.id.btnChooseImage)
        btnUpdateFood = findViewById(R.id.btnUpdateFood)
        imageView = findViewById(R.id.imageView)

        btnChooseImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1000)
        }

        btnUpdateFood.setOnClickListener {
            val id = etFoodId.text.toString().toIntOrNull()
            if (id == null) {
                Toast.makeText(this, "ID món ăn không hợp lệ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val food = Food(
                id = id,
                name = etFoodName.text.toString().trim(),
                details = etFoodDetails.text.toString().trim(),
                price = etFoodPrice.text.toString().toDouble(),
                location = etFoodLocation.text.toString().trim(),
                image = foodImage?.let { bitmapToByteArray(it) } ?: byteArrayOf()
            )
            val result = dbHelper.updateFood(food)
            if (result > 0) {
                Toast.makeText(this, "Cập nhật món ăn thành công!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Cập nhật món ăn thất bại!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, it)
                imageView.setImageBitmap(bitmap)
                foodImage = bitmap
            }
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }
}
