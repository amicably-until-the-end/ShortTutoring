package org.softwaremaestro.presenter.student_home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ActivityStudentHomeBinding

@AndroidEntryPoint
class StudentHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentHomeBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpBottomNavigationBar()
    }

    /**
     * Bottom Navigation Bar 에 Navigation Component 적용
     */
    fun setUpBottomNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)
        binding.bottomNavView.setOnItemSelectedListener {
            NavigationUI.onNavDestinationSelected(it, navController)
            return@setOnItemSelectedListener true
        }
    }
}
