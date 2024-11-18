package com.example.food_ordering_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class CustomerOrdersActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_orders)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewPagerAdapter.addFragment(PendingOrdersFragment(), "Pending Orders")
        viewPagerAdapter.addFragment(InTransitOrdersFragment(), "In Transit Orders")
        viewPagerAdapter.addFragment(DeliveredOrdersFragment(), "Delivered Orders")

        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        updateTabTitles()
    }

    private fun updateTabTitles() {
        val userId = SharedPreferencesHelper.getCurrentUserId(this)
        tabLayout.getTabAt(0)?.text = "Pending Orders (${DatabaseHelper(this).getOrderCountByStatusAndUserId("pending", userId)})"
        tabLayout.getTabAt(1)?.text = "In Transit Orders (${DatabaseHelper(this).getOrderCountByStatusAndUserId("approved", userId)})"
        tabLayout.getTabAt(2)?.text = "Delivered Orders (${DatabaseHelper(this).getOrderCountByStatusAndUserId("delivered", userId)})"
    }
}
