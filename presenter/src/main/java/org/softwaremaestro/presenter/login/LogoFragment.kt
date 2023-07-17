package org.softwaremaestro.presenter.login

import android.content.Intent
import android.media.tv.TvContract.Channels.Logo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.login.usecase.AutoLoginUseCase
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentLogoBinding
import org.softwaremaestro.presenter.student_home.StudentHomeActivity
import org.softwaremaestro.presenter.teacher_home.TeacherHomeActivity
import javax.inject.Inject

// 앱에 들어왔을 때 보이는 첫 화면.
// 로그인 창과 소셜 로그인 버튼이 있다.
@AndroidEntryPoint
class LogoFragment @Inject constructor() :
    Fragment() {

    private lateinit var binding: FragmentLogoBinding

    private val viewModel: LogoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLogoBinding.inflate(inflater, container, false)

        binding.tvLogo.setOnClickListener {
            val intent = Intent(activity, StudentHomeActivity::class.java)
            startActivity(intent)
        }

        binding.containerLoginByGoogle.setOnClickListener {
            val intent = Intent(activity, TeacherHomeActivity::class.java)
            startActivity(intent)
        }

        binding.containerLoginByKakao.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_logoFragment_to_registerRoleFragment)
        }
        checkAutoLogin()
        setObserver()
        return binding.root
    }

    private fun checkAutoLogin() {
        viewModel.getSaveToken()
    }

    private fun setObserver() {
        viewModel.savedToken.observe(viewLifecycleOwner) {
            if (viewModel.savedToken != null) {
                //viewModel.getUserInfo()
            }
        }
        viewModel.userInfo.observe(viewLifecycleOwner) {
            if (viewModel.userInfo != null) {
                Log.d("mymymy", "get user info in frag ${viewModel.userInfo.value.toString()}")
                if (viewModel.userInfo.value?.role == "student") {
                    val intent = Intent(activity, StudentHomeActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(activity, TeacherHomeActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

}