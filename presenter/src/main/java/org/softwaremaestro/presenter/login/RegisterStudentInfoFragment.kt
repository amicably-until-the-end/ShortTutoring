package org.softwaremaestro.presenter.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentRegisterStudentInfoBinding
import org.softwaremaestro.presenter.student_home.StudentHomeActivity

@AndroidEntryPoint
class RegisterStudentInfoFragment : Fragment() {

    private lateinit var binding: FragmentRegisterStudentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterStudentInfoBinding.inflate(inflater, container, false)

        return binding.root
    }
}