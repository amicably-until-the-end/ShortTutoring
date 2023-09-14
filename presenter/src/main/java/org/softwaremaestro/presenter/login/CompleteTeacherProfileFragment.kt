package org.softwaremaestro.presenter.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentCompleteTeacherProfileBinding
import org.softwaremaestro.presenter.login.viewmodel.TeacherRegisterViewModel

class CompleteTeacherProfileFragment : Fragment() {

    private lateinit var binding: FragmentCompleteTeacherProfileBinding
    private val viewModel: TeacherRegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompleteTeacherProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBtnComplete()

        observeRegisterResult()
    }

    private fun setBtnComplete() {

        binding.btnComplete.setOnClickListener {
            viewModel.registerTeacher()
        }
    }

    private fun observeRegisterResult() {

        viewModel.registerResult.observe(viewLifecycleOwner) { isTeacherRegistered ->

            // Todo : 추후에 수정
            findNavController().navigate(R.id.action_completeTeacherProfileFragment_to_loginFrament)

            if (isTeacherRegistered) {

            } else {

            }
        }
    }
}