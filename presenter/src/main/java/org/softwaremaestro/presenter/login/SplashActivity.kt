package org.softwaremaestro.presenter.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.ActivitySplashBinding
import org.softwaremaestro.presenter.login.viewmodel.LoginViewModel
import org.softwaremaestro.presenter.student_home.StudentHomeActivity
import org.softwaremaestro.presenter.teacher_home.TeacherHomeActivity
import org.softwaremaestro.presenter.util.UIState

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val loginViewModel: LoginViewModel by viewModels()

    private var chatId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("deepLink", "onViewCreated ${this::class.java} ${this.hashCode()}")
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getPendingIntent()
        observeAutoLoginResult()
        loginViewModel.autoLogin()
    }

    private fun getPendingIntent() {
        val args = intent.extras
        args?.apply {
            try {
                Log.d("deepLink@Splash", "args: $args ${getString(APP_LINK_ARGS_CHAT_ID)}")
                chatId = getString(APP_LINK_ARGS_CHAT_ID)
            } catch (e: Exception) {
                Log.w(this@SplashActivity::class.java.name, "getPendingIntent: $e")
            }
        }
    }


    private fun observeAutoLoginResult() {
        loginViewModel.saveRole.observe(this) {
            when (it) {
                is UIState.Success -> {
                    loginViewModel.registerFCMToken()
                    if (it._data == "teacher") {
                        goToTeacherHomeActivity()
                    } else if (it._data == "student") {
                        goToStudentHomeActivity()
                    } else {
                        // TODO : 선생님 , 학생 말고 다른 사용자 있을 경우. EX 관리자.
                        goToLoginActivity()
                    }
                }

                is UIState.Failure -> {
                    goToLoginActivity()
                }

                else -> {
                }
            }
        }
    }

    private fun goToTeacherHomeActivity() {
        val intent = Intent(this, TeacherHomeActivity::class.java)
        chatId?.let { intent.putExtra(APP_LINK_ARGS_CHAT_ID, it) }
        startActivity(intent)
    }

    private fun goToStudentHomeActivity() {
        val intent = Intent(this, StudentHomeActivity::class.java)
        chatId?.let { intent.putExtra(APP_LINK_ARGS_CHAT_ID, it) }
        Log.d("deepLink@Splash", "startStudentHome: $chatId")
        startActivity(intent)
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    companion object {
        const val APP_LINK_ARGS_CHAT_ID = "chattingId"
        const val CHAT_INTENT_FLAG = 100
    }

}