package org.softwaremaestro.presenter.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentRegisterTeacherInfoBinding
import org.softwaremaestro.presenter.student_home.StudentHomeActivity

// 회원가입 두 번째 화면.
// 개인정보를 입력한다.
class RegisterTeacherInfoFragment : Fragment() {

    private lateinit var binding: FragmentRegisterTeacherInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterTeacherInfoBinding.inflate(inflater, container, false)

        binding.btnComplete.setOnClickListener {
            if (true) {
                Navigation.findNavController(it).navigate(R.id.action_registerTeacherInfoFragment_to_logoFragment)
            }
            else {

            }
        }

        return binding.root
    }
}