package com.example.food_ordering_app


import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.navigation.NavigationView

class MoreActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.navigation_view)

        val accountFragment = AccountFragment()
        val ordersFragment = OrdersFragment()
        val cartFragment = CartFragment()
        val notificationsFragment = NotificationsFragment()
        val supportFragment = SupportFragment()

        val navHostFragment = findViewById<FrameLayout>(R.id.nav_host_fragment)

        if (navHostFragment == null) {
            Log.e("MoreActivity", "Nav host fragment is null")
        } else {
            Log.d("MoreActivity", "Nav host fragment is not null")
        }

        makeCurrentFragment(accountFragment)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_account -> makeCurrentFragment(accountFragment)
                R.id.navigation_cart -> makeCurrentFragment(cartFragment)
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