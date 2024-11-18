package com.example.food_ordering_app


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class AccountFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var currentUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = DatabaseHelper(requireContext())

        // Hiển thị thông tin người dùng
        displayUserInfo(view)

        val btnEditProfile = view.findViewById<Button>(R.id.btnEditProfile)
        btnEditProfile.setOnClickListener {
            // Chuyển đến trang chỉnh sửa thông tin
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            // Chuyển đến màn hình đăng nhập và xóa hoạt động hiện tại khỏi ngăn xếp
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()

            // Hiển thị thông báo đăng xuất thành công
            Toast.makeText(requireContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayUserInfo(view: View) {
        val userId = SharedPreferencesHelper.getCurrentUserId(requireContext())
        currentUser = dbHelper.getCurrentUserFromDatabase(userId) ?: run {
            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
            return
        }

        view.findViewById<TextView>(R.id.txtUsername).text = currentUser.username
        view.findViewById<TextView>(R.id.txtPhone).text = currentUser.phone
        view.findViewById<TextView>(R.id.txtEmail).text = currentUser.email
        view.findViewById<TextView>(R.id.txtAddress).text = currentUser.address
    }
}
