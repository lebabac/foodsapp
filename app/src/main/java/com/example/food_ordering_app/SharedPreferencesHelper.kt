package com.example.food_ordering_app

import android.content.Context

object SharedPreferencesHelper {
    // Lưu userId vào SharedPreferences
    fun saveUserId(context: Context, userId: Long) {
        val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong("userId", userId)
        editor.apply()
    }

    // Lấy userId từ SharedPreferences
    fun getCurrentUserId(context: Context): Long {
        val sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("userId", -1) // Trả về -1 nếu không tìm thấy giá trị
    }
}
