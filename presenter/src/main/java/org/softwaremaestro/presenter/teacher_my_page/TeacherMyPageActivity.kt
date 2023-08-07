package org.softwaremaestro.presenter.teacher_my_page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ActivityTeacherMyPageBinding

class TeacherMyPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherMyPageBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherMyPageBinding.inflate(layoutInflater)
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
    }
}