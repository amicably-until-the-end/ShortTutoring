package org.softwaremaestro.presenter.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.FragmentEmailCheckBinding
import org.softwaremaestro.presenter.login.viewmodel.TeacherRegisterViewModel
import org.softwaremaestro.presenter.Util.setEnabledAndChangeColor


@AndroidEntryPoint
class EmailCheckFragment : Fragment() {


    private lateinit var binding: FragmentEmailCheckBinding
    private val viewModel: TeacherRegisterViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEmailCheckBinding.inflate(inflater, container, false)

        setButtons()
        setObserver()
        setRegisterButton()
        return binding.root
    }

    private fun setEmailGetButton() {
        binding.btnGetEmail.setOnClickListener {
            viewModel.sendVerificationMail(binding.etUniv.text.toString())
        }
    }

    private fun setButtons() {
        setEmailGetButton()
        setCheckCodeButton()
        setRegisterButton()
    }

    private fun observeSendMailResult() {
        viewModel.sendEmailResult.observe(viewLifecycleOwner) {
            if (it) {
                binding.btnGetEmail.text = "메일을 전송했습니다."
                binding.btnGetEmail.setEnabledAndChangeColor(false)
                binding.btnCheckCode.setEnabledAndChangeColor(true)
                binding.etUniv.isFocusable = false
            } else {
                Toast.makeText(requireContext(), "올바른 학교 메일을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeCheckEmailResult() {
        viewModel.checkEmailResult.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "인증번호가 일치합니다.", Toast.LENGTH_SHORT).show()
                binding.btnRegister.setEnabledAndChangeColor(true)
            } else {
                Toast.makeText(requireContext(), "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setObserver() {
        observeSendMailResult()
        observeCheckEmailResult()
    }


    private fun setCheckCodeButton() {
        binding.btnCheckCode.setOnClickListener {
            viewModel.checkEmailCode(
                binding.etUniv.text.toString(),
                binding.etEmailCode.text.toString().toInt()
            )
        }
    }

    private fun setRegisterButton() {
        binding.btnRegister.setOnClickListener {
            viewModel.registerTeacher()
        }
    }


}