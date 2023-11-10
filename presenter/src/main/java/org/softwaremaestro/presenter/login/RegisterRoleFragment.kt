package org.softwaremaestro.presenter.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentRegisterRoleBinding
import org.softwaremaestro.presenter.login.viewmodel.StudentRegisterViewModel
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.Util.getWidth
import org.softwaremaestro.presenter.util.Util.toPx

// 로그인 화면에서 회원 가입을 누르면 나오는 화면.
// 유저는 학생과 선생님 중에서 선택한다.
@AndroidEntryPoint
class RegisterRoleFragment : Fragment() {

    private lateinit var binding: FragmentRegisterRoleBinding
    private val viewModel: StudentRegisterViewModel by activityViewModels()
    private var isSmallSizeScreen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterRoleBinding.inflate(inflater, container, false)
        supportSmallScreenSize()
        loadViewModelValue()
        setTvStudent()
        setTvTeacher()
        setBtnNext()
        setBtnToolbarBack()
        observe()

        return binding.root
    }

    private fun supportSmallScreenSize() {
        val width = getWidth(requireActivity())
        isSmallSizeScreen = width < 600
        if (isSmallSizeScreen) {
            val paddingValue = toPx(30, requireContext())
            binding.glLeft.setGuidelineBegin(paddingValue)
            binding.glRight.setGuidelineEnd(paddingValue)
        }
    }

    private fun loadViewModelValue() {
        when (viewModel.role.value) {
            0 -> {
                binding.rbStudent.isChecked = true
            }

            1 -> {
                binding.rbTeacher.isChecked = true
            }

            else -> {
                binding.rgRole.clearCheck()
            }
        }
    }

    private fun setTvStudent() {
        binding.rbStudent.setOnClickListener {
            viewModel.setRole(0)
        }
    }

    private fun setTvTeacher() {
        binding.rbTeacher.setOnClickListener {
            viewModel.setRole(1)
        }
    }

    private fun setBtnNext() {
        binding.btnNext.setOnClickListener {
            val dest = when (viewModel.role.value) {
                0 -> R.id.action_registerRoleFragment_to_registerStudentInfoFragment
                1 -> R.id.action_registerRoleFragment_to_registerTeacherInfoFragment
                else -> null
            }

            if (dest != null) {
                findNavController().navigate(dest)
            } else {
                Util.createToast(requireActivity(), "계정 정보를 선택해주세요").show()
            }

        }
    }

    private fun setBtnToolbarBack() {
        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observe() {
        viewModel.role.observe(viewLifecycleOwner) { role ->
            if (role == null) {
                with(binding.btnNext) {
                    setBackgroundResource(R.drawable.bg_radius_5_grey)
                    setTextColor(resources.getColor(R.color.sub_text_grey, null))
                }
            } else {
                with(binding.btnNext) {
                    setBackgroundResource(R.drawable.bg_radius_5_grad_blue)
                    setTextColor(resources.getColor(R.color.white, null))
                }
            }
        }
    }
}