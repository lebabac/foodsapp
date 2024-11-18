package com.example.food_ordering_app

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream

class AddFoodActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etFoodName: EditText
    private lateinit var etFoodPrice: EditText
    private lateinit var etFoodDetails: EditText
    private lateinit var etFoodLocation: EditText
    private lateinit var btnChooseImage: Button
    private lateinit var btnAddFood: Button
    private lateinit var imageView: ImageView
    private var foodImage: Bitmap? = null
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        dbHelper = DatabaseHelper(this)
        etFoodName = findViewById(R.id.etFoodName)
        etFoodPrice = findViewById(R.id.etFoodPrice)
        etFoodDetails = findViewById(R.id.etFoodDetails)
        etFoodLocation = findViewById(R.id.etFoodLocation)
        btnChooseImage = findViewById(R.id.btnChooseImage)
        btnAddFood = findViewById(R.id.btnAddFood)
        imageView = findViewById(R.id.imageView)

        // Khởi tạo pickImageLauncher với ActivityResultContracts.StartActivityForResult
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, it)
                    imageView.setImageBitmap(bitmap)
                    foodImage = bitmap
                }
            }
        }

        btnChooseImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1001)
            } else {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickImageLauncher.launch(intent)
            }
        }

        btnAddFood.setOnClickListener {
            val foodName = etFoodName.text.toString().trim()
            val foodPrice = etFoodPrice.text.toString().toDoubleOrNull()
            val foodDetails = etFoodDetails.text.toString().trim()
            val foodLocation = etFoodLocation.text.toString().trim()

            if (foodName.isNotEmpty() && foodPrice != null && foodDetails.isNotEmpty() && foodLocation.isNotEmpty() && foodImage != null) {
                val food = Food(
                    id = 0,
                    name = foodName,
                    details = foodDetails,
                    price = foodPrice,
                    location = foodLocation,
                    image = bitmapToByteArray(foodImage!!)
                )
                val result = dbHelper.addFood(food)
                if (result != -1L) {
                    Toast.makeText(this, "Thêm món ăn thành công!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Thêm món ăn thất bại!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin và chọn ảnh!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickImageLauncher.launch(intent)
            } else {
                Toast.makeText(this, "Quyền truy cập bộ nhớ bị từ chối!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
