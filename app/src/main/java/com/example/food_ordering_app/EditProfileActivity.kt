package com.example.food_ordering_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class EditProfileActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        dbHelper = DatabaseHelper(this) // Khởi tạo dbHelper trước khi sử dụng

        val userId = SharedPreferencesHelper.getCurrentUserId(this) // Lấy ID của người dùng đang đăng nhập
        currentUser = dbHelper.getCurrentUserFromDatabase(userId) ?: run {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Hiển thị thông tin người dùng hiện tại
        findViewById<EditText>(R.id.editUsername).setText(currentUser.username)
        findViewById<EditText>(R.id.editPhone).setText(currentUser.phone)
        findViewById<EditText>(R.id.editEmail).setText(currentUser.email)
        findViewById<EditText>(R.id.editAddress).setText(currentUser.address)

        val btnSaveChanges = findViewById<Button>(R.id.btnSaveChanges)
        btnSaveChanges.setOnClickListener {
            saveChanges()
        }
    }

    private fun saveChanges() {
        val newPassword = findViewById<EditText>(R.id.editPassword).text.toString()
        val passwordEditText = findViewById<EditText>(R.id.editPassword)
        val password = passwordEditText.text.toString()

        // Kiểm tra mật khẩu
        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter your current password", Toast.LENGTH_SHORT).show()
            return
        }

        if (verifyPassword()) {
            // Mật khẩu hợp lệ, tiếp tục thay đổi thông tin
            val newUsername = findViewById<EditText>(R.id.editUsername).text.toString()
            val newPhone = findViewById<EditText>(R.id.editPhone).text.toString()
            val newEmail = findViewById<EditText>(R.id.editEmail).text.toString()
            val newAddress = findViewById<EditText>(R.id.editAddress).text.toString()

            // Cập nhật thông tin mới vào cơ sở dữ liệu
            val updatedUser = User(currentUser.id, newUsername, newPhone, newEmail, newAddress, newPassword, currentUser.role)
            val result = dbHelper.updateUser(updatedUser)

            if (result > 0) {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                // Chuyển đến trang cá nhân
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Mật khẩu không chính xác
            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
        }
    }
    private fun verifyPassword(): Boolean {
        // Lấy mật khẩu hiện tại từ EditText
        val currentPassword = findViewById<EditText>(R.id.editPassword).text.toString()

        // So sánh mật khẩu hiện tại với mật khẩu đã lưu của người dùng
        return currentPassword == currentUser.password
    }


}
