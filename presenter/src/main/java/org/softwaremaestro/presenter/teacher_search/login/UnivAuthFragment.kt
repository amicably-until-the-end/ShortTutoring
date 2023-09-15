package org.softwaremaestro.presenter.teacher_search.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentUnivAuthBinding
import org.softwaremaestro.presenter.teacher_search.login.viewmodel.TeacherRegisterViewModel
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor

@AndroidEntryPoint
class UnivAuthFragment : Fragment() {

    private lateinit var binding: FragmentUnivAuthBinding
    private val viewModel: TeacherRegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUnivAuthBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSendMailButton()
        setEtAuthCode()
        setNextButton()
        setObserver()
    }

    private fun setSendMailButton() {
        binding.btnSendMail.setOnClickListener {
            viewModel.sendVerificationMail(binding.etUnivMail.text.toString())
        }
    }

    private fun setEtAuthCode() {

        binding.etAuthCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                try {
                    viewModel.checkEmailCode(
                        binding.etUnivMail.text.toString(),
                        binding.etAuthCode.text.toString().toInt()
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun setNextButton() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_univAuthFragment_to_completeTeacherProfileFragment)
        }
    }

    private fun setObserver() {
        observeSendMailResult()
        observeCheckEmailResult()
    }

    private fun observeSendMailResult() {
        viewModel.sendEmailResult.observe(viewLifecycleOwner) {
            if (it) {
                binding.btnSendMail.setEnabledAndChangeColor(false)
                binding.etUnivMail.isFocusable = false
            } else {
                Toast.makeText(requireContext(), "올바른 학교 메일을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeCheckEmailResult() {
        viewModel.checkEmailResult.observe(viewLifecycleOwner) {

            binding.btnNext.setEnabledAndChangeColor(it)
            Toast.makeText(
                requireContext(),
                if (it) "인증번호가 일치합니다." else "인증번호가 일치하지 않습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}