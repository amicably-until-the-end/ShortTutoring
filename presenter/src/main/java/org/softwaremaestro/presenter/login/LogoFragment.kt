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
            startActivity(intent)
        }

        binding.containerLoginByGoogle.setOnClickListener {
            val intent = Intent(activity, TeacherHomeActivity::class.java)
            startActivity(intent)
        }

        binding.containerLoginByKakao.setOnClickListener {
            //Navigation.findNavController(it).navigate(R.id.action_logoFragment_to_registerRoleFragment)
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                    if (error != null) {
                        Log.e("kakao", "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(
                            requireContext(),
                            callback = callback
                        )
                    } else if (token != null) {
                        Log.i("kakao", "카카오톡으로 로그인 성공 ${token.accessToken}")
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
            }

        }
        checkAutoLogin()
        setObserver()
        checkKakaoToken()
        return binding.root
    }

    private fun checkKakaoToken() {
        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.instance.accessTokenInfo { _, error ->
                if (error != null) {
                    if (error is KakaoSdkError && error.isInvalidTokenError()) {
                        //로그인 필요
                    } else {
                        //기타 에러
                    }
                } else {
                    //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                }
            }
        } else {
            //로그인 필요
        }
    }


    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("kakao", "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i("kakao", "카카오계정으로 로그인 성공 ${token.accessToken}")
        }
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