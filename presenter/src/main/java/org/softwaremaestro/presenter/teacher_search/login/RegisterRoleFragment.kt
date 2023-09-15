package org.softwaremaestro.presenter.teacher_search.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentRegisterRoleBinding
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor

// 로그인 화면에서 회원 가입을 누르면 나오는 화면.
// 유저는 학생과 선생님 중에서 선택한다.
@AndroidEntryPoint
class RegisterRoleFragment : Fragment() {

    private lateinit var binding: FragmentRegisterRoleBinding
    private var selectedRole: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterRoleBinding.inflate(inflater, container, false)

        binding.tvStudent.setOnClickListener {
            binding.btnNext.setEnabledAndChangeColor(true)
            selectedRole = 0
        }

        binding.tvTeacher.setOnClickListener {
            binding.btnNext.setEnabledAndChangeColor(true)
            selectedRole = 1
        }

        binding.btnNext.setOnClickListener {
            when (selectedRole) {
                0 -> R.id.action_registerRoleFragment_to_registerStudentInfoFragment
                1 -> R.id.action_registerRoleFragment_to_registerTeacherInfoFragment
                else -> null
            }?.let { dest ->
                findNavController().navigate(dest)
            }
        }
        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

}