package org.softwaremaestro.presenter.student_home

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ActivityStudentHomeBinding

@AndroidEntryPoint
class StudentHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpBottomNavigationBar()
        setToolBar()


    }

    /**
     * Bottom Navigation Bar 에 Navigation Component 적용
     */
    fun setUpBottomNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)
    }

    /**
     * 상단바 설정
     */
    fun setToolBar() {

        // 액션바 설정
        val toolbar: Toolbar = binding.toolbar
        if (toolbar != null)
            setSupportActionBar(toolbar)

        // 뒤로가기 버튼
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.student_home_menu, menu)
        return true
    }

}
