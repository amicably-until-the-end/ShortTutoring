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
import org.softwaremaestro.presenter.databinding.FragmentRegisterStudentInfoBinding
import org.softwaremaestro.presenter.login.viewmodel.StudentRegisterViewModel
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor

@AndroidEntryPoint
class RegisterStudentInfoFragment : Fragment() {

    private lateinit var binding: FragmentRegisterStudentInfoBinding
    private val viewModel: StudentRegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterStudentInfoBinding.inflate(layoutInflater)

        setRgSchoolLevel()
        setRgSchoolGrade()
        setBtnNext()
        setBtnToolbarBack()
        observe()

        return binding.root
    }

    private fun setRgSchoolLevel() {
        binding.rgSchoolLevel.setOnCheckedChangeListener { _, resId ->
            when (resId) {
                R.id.rb_middle_school -> "중학교"
                R.id.rb_high_school -> "고등학교"
                else -> null
            }?.let {
                viewModel.setSchoolLevel(it)
            }
        }
    }

    private fun setRgSchoolGrade() {
        binding.rgGrade.setOnCheckedChangeListener { _, resId ->
            when (resId) {
                R.id.rb_grade_0 -> 1
                R.id.rb_grade_1 -> 2
                R.id.rb_grade_2 -> 3
                else -> null
            }?.let {
                viewModel.setSchoolGrade(it)
            }
        }
    }

    private fun setBtnNext() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_registerStudentInfoFragment_to_completeStudentProfileFragment)
        }
    }

    private fun setBtnToolbarBack() {
        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observe() {
        observeSchoolLevel()
        observeSchoolLevelAndGrade()
    }

    private fun observeSchoolLevel() {
        viewModel.schoolLevel.observe(viewLifecycleOwner) {
            binding.containerSchoolGrade.visibility = View.VISIBLE
        }
    }

    private fun observeSchoolLevelAndGrade() {
        viewModel.schoolLevelAndGradeProper.observe(viewLifecycleOwner) { proper ->
            binding.btnNext.setEnabledAndChangeColor(proper)
        }
    }
}