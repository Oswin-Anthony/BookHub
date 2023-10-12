package com.example.bookhub.activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookhub.R
import com.example.bookhub.fragment.AboutAppFragment
import com.example.bookhub.fragment.DashboardFragment
import com.example.bookhub.fragment.FavouritesFragment
import com.example.bookhub.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var constraintLayout: ConstraintLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    var previousManuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        constraintLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)

        setUpToolBar()
        openDashboard()
        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        
        navigationView.setNavigationItemSelectedListener {
            if(previousManuItem != null) {
                previousManuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousManuItem = it

            when(it.itemId) {
                R.id.dashboard -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.favourites -> {
                    openFavourites()
                    drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    openProfile()
                    drawerLayout.closeDrawers()
                }
                R.id.about -> {
                    openAboutApp()
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun setUpToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openDashboard() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, DashboardFragment())
            .commit()
        supportActionBar?.title = "Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)
    }

    fun openFavourites() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, FavouritesFragment())
            .commit()
        supportActionBar?.title = "Favourites"
    }

    fun openProfile() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, ProfileFragment())
            .commit()
        supportActionBar?.title = "Profile"
    }

    fun openAboutApp() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, AboutAppFragment())
            .commit()
        supportActionBar?.title = "About App"
    }

    override fun onBackPressed() {
        when(supportFragmentManager.findFragmentById(R.id.frame)) {
            !is DashboardFragment -> openDashboard()
            else -> {
                val dialog = AlertDialog.Builder(this@MainActivity)
                dialog.setTitle("Exit")
                dialog.setMessage("Do you want to exit the application?")
                dialog.setPositiveButton("Yes") { _, _ ->
                    ActivityCompat.finishAffinity(this@MainActivity)
                }
                dialog.setNegativeButton("No") { _, _ -> }
                dialog.create()
                dialog.show()
            }
        }
    }
}