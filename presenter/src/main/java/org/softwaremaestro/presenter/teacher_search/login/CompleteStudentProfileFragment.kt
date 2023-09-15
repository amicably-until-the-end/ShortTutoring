package org.softwaremaestro.presenter.teacher_search.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentCompleteStudentProfileBinding
import org.softwaremaestro.presenter.teacher_search.login.viewmodel.StudentRegisterViewModel

@AndroidEntryPoint
class CompleteStudentProfileFragment : Fragment() {

    private lateinit var binding: FragmentCompleteStudentProfileBinding

    private val viewModel: StudentRegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompleteStudentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnComplete.setOnClickListener {
            viewModel.registerStudent()
        }

        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }

        setRegisterResultObserver()
    }

    private fun setRegisterResultObserver() {
        viewModel.registerSuccess.observe(viewLifecycleOwner) { isRegisterSuccess ->

            // 추후에 수정하기
            findNavController().navigate(R.id.action_completeStudentProfileFragment_to_loginFrament)

            if (isRegisterSuccess) {
            } else {
                //TODO: 실패했을 때 처리

            }
        }
    }
}