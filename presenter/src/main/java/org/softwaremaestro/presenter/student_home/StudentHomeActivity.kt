package org.softwaremaestro.presenter.student_home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ActivityStudentHomeBinding
import org.softwaremaestro.presenter.login.SplashActivity
import org.softwaremaestro.presenter.question_upload.question_normal_upload.QuestionUploadActivity
import org.softwaremaestro.presenter.student_home.viewmodel.HomeViewModel
import javax.inject.Inject

@AndroidEntryPoint
class StudentHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentHomeBinding
    private lateinit var navController: NavController

    private val homeViewModel: HomeViewModel by viewModels()


    @Inject
    lateinit var socketManager: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("deepLink", "onViewCreated ${this::class.java} ${this.hashCode()}")

        binding = ActivityStudentHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpBottomNavigationBar() //반드시 첫번째로dfg 실행
        initSocket()

    }

    override fun onStart() {
        super.onStart()
        getIntentExtra()
        requirePermission()
        observeNewMessage()
        addSocketListener()
        homeViewModel.getNewMessageExist()
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("deepLink", "onNewIntent ${this::class.java} ${this.hashCode()}")
        intent?.apply {
            try {
                val chatId = getStringExtra(SplashActivity.APP_LINK_ARGS_CHAT_ID)
                if (!chatId.isNullOrEmpty()) {
                    homeViewModel.chattingId = chatId
                    moveToChatTab()
                }
            } catch (e: Exception) {
                Log.w(this@StudentHomeActivity::class.java.name, "onNewIntent: $e")
            }
        }
    }

    private fun observeNewMessage() {
        homeViewModel.newMessageExist.observe(this) {
            if (it) {
                binding.bottomNavView.getOrCreateBadge(R.id.studentChatFragment).apply {
                    isVisible = true
                }
            } else {
                binding.bottomNavView.removeBadge(R.id.studentChatFragment)
            }
        }
    }

    private fun addSocketListener() {
        socketManager.addHomeListener {
            homeViewModel.getNewMessageExist()
        }
    }

    private fun getIntentExtra() {
        val args = intent.extras
        Log.d("deepLink@StudentHome", "args: $args")
        args?.apply {
            val chatId =
                try {
                    getString(SplashActivity.APP_LINK_ARGS_CHAT_ID)
                } catch (e: Exception) {
                    null
                }
            if (!chatId.isNullOrEmpty()) {
                homeViewModel.chattingId = chatId
                moveToChatTab()
            }
        }
    }

    private fun initSocket() {
        socketManager.init()
    }

    /**
     * Bottom Navigation Bar 에 Navigation Component 적용
     */
    private fun setUpBottomNavigationBar() {
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
                if (it.itemId == R.id.studentChatFragment) {
                    hideChatBadge()
                } else {
                    homeViewModel.getNewMessageExist()
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
        val item = binding.bottomNavView.menu.findItem(R.id.studentChatFragment)
        // Return true only if the destination we've navigated to matches the MenuItem
        NavigationUI.onNavDestinationSelected(item, navController)
    }

    private fun requirePermission() {
        val permission = arrayOf(android.Manifest.permission.POST_NOTIFICATIONS)
        requestPermissions(permission, 0)
    }

    fun hideChatBadge() {
        binding.bottomNavView.removeBadge(R.id.studentChatFragment)
    }

    fun moveToCoinTab() {
        val coinItem = binding.bottomNavView.menu.findItem(R.id.chargeCoinSecondFragment)
        NavigationUI.onNavDestinationSelected(coinItem, navController)
        //파라미터 넘기고 싶으면 navController navigate 직접 호출하고 바텀 네비게이션은 선택된 아이콘 직접 바꾸기
    }
}
