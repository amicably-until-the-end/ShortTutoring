package org.softwaremaestro.presenter.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.login.entity.StudentRegisterVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentRegisterStudentInfoBinding
import org.softwaremaestro.presenter.student_home.StudentHomeActivity

@AndroidEntryPoint
class RegisterStudentInfoFragment : Fragment() {

    private lateinit var binding: FragmentRegisterStudentInfoBinding

    private val viewModel: StudentRegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterStudentInfoBinding.inflate(layoutInflater)

        binding.rgSchoolLevel.setOnCheckedChangeListener { _, resId ->
            when (resId) {
                R.id.rb_middle_school -> viewModel.school = 0
                R.id.rb_high_school -> viewModel.school = 1
            }
        }

        binding.rgGrade.setOnCheckedChangeListener { _, resId ->
            when (resId) {
                R.id.rb_grade_0 -> viewModel.grade = 1
                R.id.rb_grade_1 -> viewModel.grade = 2
                R.id.rb_grade_2 -> viewModel.grade = 3
            }
        }

        binding.btnNext.setOnClickListener {
            viewModel.registerStudent()
        }

        setRegisterResultObserver()
        return binding.root
    }

    private fun setRegisterResultObserver() {
        viewModel.registerSuccess.observe(viewLifecycleOwner) { isRegisterSuccess ->
            if (isRegisterSuccess) {
                val intent = Intent(requireContext(), StudentHomeActivity::class.java)
                startActivity(intent)
            } else {
                //TODO: 실패했을 때 처리
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_registerStudentInfoFragment_to_logoFragment)
            }
        }

    }
}