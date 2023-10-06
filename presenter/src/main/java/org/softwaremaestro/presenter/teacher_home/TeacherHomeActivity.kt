package org.softwaremaestro.presenter.teacher_home

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
import org.softwaremaestro.presenter.databinding.ActivityTeacherHomeBinding
import org.softwaremaestro.presenter.login.SplashActivity
import org.softwaremaestro.presenter.student_home.viewmodel.DeepLinkViewModel
import javax.inject.Inject

@AndroidEntryPoint
class TeacherHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherHomeBinding
    private lateinit var navController: NavController
    private val deepLinkViewModel: DeepLinkViewModel by viewModels()

    @Inject
    lateinit var socketManager: SocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSocket()
        setUpBottomNavigationBar()
    }

    private fun initSocket() {
        socketManager.init()
    }

    override fun onStart() {
        super.onStart()
        getIntentExtra()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("deepLink", "onNewIntent ${this::class.java} ${this.hashCode()}")
        intent?.apply {
            try {
                val chatId = getStringExtra(SplashActivity.APP_LINK_ARGS_CHAT_ID)
                if (!chatId.isNullOrEmpty()) {
                    deepLinkViewModel.chattingId = chatId
                    moveToChatTab()
                }
            } catch (e: Exception) {
                Log.w(this@TeacherHomeActivity::class.java.name, "onNewIntent: $e")
            }
        }
    }

    fun moveToChatTab() {
        val item = binding.bottomNavView.menu.findItem(R.id.studentChatFragment)
        // Return true only if the destination we've navigated to matches the MenuItem
        NavigationUI.onNavDestinationSelected(item, navController)
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
                deepLinkViewModel.chattingId = chatId
                moveToChatTab()
            }
        }
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