package org.softwaremaestro.presenter.teacher_home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ActivityTeacherHomeBinding

@AndroidEntryPoint
class TeacherHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherHomeBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpBottomNavigationBar()
    }

    /**
     * Bottom Navigation Bar 에 Navigation Component 적용
     */
    fun setUpBottomNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view_teacher) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavView.apply {
            setupWithNavController(navController)
            setOnItemSelectedListener {
                NavigationUI.onNavDestinationSelected(it, navController)
                return@setOnItemSelectedListener true
            }
            itemIconTintList = null
        }
    }
}