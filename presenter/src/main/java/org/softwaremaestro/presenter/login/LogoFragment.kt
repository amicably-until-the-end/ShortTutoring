package org.softwaremaestro.presenter.login

import android.content.Intent
import android.media.tv.TvContract.Channels.Logo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
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
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
        }

        binding.containerLoginByGoogle.setOnClickListener {
            val intent = Intent(activity, TeacherHomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent)
        }


        binding.containerLoginByKakao.setOnClickListener {
            //viewModel.loginWithKakao(requireContext())
        }

        //checkAutoLogin()
        setObserver()
        hideAppBar()
        return binding.root
    }

    private fun hideAppBar() {
        (activity as AppCompatActivity)!!.supportActionBar!!.hide()
    }

    private fun checkAutoLogin() {
        // 자동로그인 처리 구현 방법
        // 1. preferenece 에 저장된 이전 로그인 정보 확인
        // 2. 로그인 정보 없으면 소셜 로그인 선택화면 보여주기
        // 3. access token으로 회원정보 받아오기.
        // 3. 회원정보에 따라서 선생님, 학생 분기.
    }


    private fun observeUserInfo() {
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

    private fun observeKakaoLogin() {
        viewModel.isKakaoLoginSuccess.observe(viewLifecycleOwner) {
            if (viewModel.isKakaoLoginSuccess?.value == true) {
                //카카오 로그인 성공 //로컬에 idToken이 저장되어있음

            }
        }
    }

    private fun setObserver() {
        observeUserInfo()
        observeKakaoLogin()
    }

}