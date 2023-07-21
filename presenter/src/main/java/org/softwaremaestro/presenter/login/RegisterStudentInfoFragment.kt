package org.softwaremaestro.presenter.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentRegisterStudentInfoBinding

@AndroidEntryPoint
class RegisterStudentInfoFragment : Fragment() {

    private lateinit var binding: FragmentRegisterStudentInfoBinding
    private var selectedSchool: Int = 0
    private var selectedGrade: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterStudentInfoBinding.inflate(layoutInflater)

        binding.rgSchoolLevel.setOnCheckedChangeListener { _, pos ->
            selectedSchool = pos
        }

        binding.rgGrade.setOnCheckedChangeListener { _, pos ->
            selectedGrade = pos
        }

        binding.btnNext.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_registerStudentInfoFragment_to_logoFragment)
        }

        return binding.root
    }
}