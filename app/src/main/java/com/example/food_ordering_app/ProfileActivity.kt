package com.example.food_ordering_app

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class ProfileActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        val accountFragment = AccountFragment()
        val ordersFragment = OrdersFragment()
        val notificationsFragment = NotificationsFragment()
        val supportFragment = SupportFragment()

        makeCurrentFragment(accountFragment)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_account -> makeCurrentFragment(accountFragment)
                R.id.navigation_orders -> makeCurrentFragment(ordersFragment)
                R.id.navigation_notifications -> makeCurrentFragment(notificationsFragment)
                R.id.navigation_support -> makeCurrentFragment(supportFragment)
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, fragment)
            commit()
        }
}