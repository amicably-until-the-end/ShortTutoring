package org.softwaremaestro.presenter.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentRegisterRoleBinding

// 로그인 화면에서 회원 가입을 누르면 나오는 화면.
// 유저는 선생님과 학생 중에서 선택한다.
class RegisterRoleFragment : Fragment() {

    private lateinit var binding: FragmentRegisterRoleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterRoleBinding.inflate(inflater, container, false)

        binding.btnRegisterRole.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_registerRoleFragment_to_registerInfoFragment)
        }

        return binding.root
    }
}