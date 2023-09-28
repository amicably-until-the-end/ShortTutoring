package org.softwaremaestro.presenter.student_home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ActivityStudentHomeBinding
import org.softwaremaestro.presenter.question_upload.question_normal_upload.QuestionUploadActivity
import javax.inject.Inject

@AndroidEntryPoint
class StudentHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentHomeBinding
    private lateinit var navController: NavController

    @Inject
    lateinit var socketManager: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpBottomNavigationBar()
        initSocket()
    }

    private fun initSocket() {
        socketManager.init()
    }

    /**
     * Bottom Navigation Bar 에 Navigation Component 적용
     */
    fun setUpBottomNavigationBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavView.apply {
            setupWithNavController(navController)
            setOnItemSelectedListener {
                if (it.itemId == R.id.questionCameraFragment) {
                    var intent =
                        Intent(this@StudentHomeActivity, QuestionUploadActivity::class.java)
                    startActivity(intent)
                    return@setOnItemSelectedListener false
                }
                NavigationUI.onNavDestinationSelected(it, navController)
                return@setOnItemSelectedListener true
            }
            background = null
            itemIconTintList = null
        }

        binding.fab.setOnClickListener {
            var intent = Intent(this, QuestionUploadActivity::class.java)
            startActivity(intent)
        }

    }

    fun moveToChatTab() {
        val chatItem = binding.bottomNavView.menu.findItem(R.id.studentChatFragment)
        NavigationUI.onNavDestinationSelected(chatItem, navController)
        //파라미터 넘기고 싶으면 navController navigate 직접 호출하고 바텀 네비게이션은 선택된 아이콘 직접 바꾸기
    }
}
