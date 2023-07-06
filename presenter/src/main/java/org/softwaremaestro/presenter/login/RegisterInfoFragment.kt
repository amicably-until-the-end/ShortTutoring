package org.softwaremaestro.presenter.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentRegisterInfoBinding

// 회원가입 두 번째 화면.
// 개인정보를 입력한다.
class RegisterInfoFragment : Fragment() {

    private lateinit var binding: FragmentRegisterInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterInfoBinding.inflate(inflater, container, false)

        binding.btnRegisterInfo.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_registerInfoFragment_to_loginFragment)
        }

        return binding.root
    }
}