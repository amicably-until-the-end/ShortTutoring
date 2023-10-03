package org.softwaremaestro.presenter.login

import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel.autoLogin()
        observeAutoLoginResult()
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
        startActivity(intent)
    }

    private fun goToStudentHomeActivity() {
        val intent = Intent(this, StudentHomeActivity::class.java)
        startActivity(intent)
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}