package org.softwaremaestro.presenter.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentTosBinding
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor

@AndroidEntryPoint
class ToSFragment : Fragment() {

    private lateinit var binding: FragmentTosBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTosBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolBar()
        setToggleButtons()
        setTexts()
        setNextButton()

        binding.containerTos.setOnClickListener {
            startActivity(Intent(requireActivity(), ToSNotionActivity::class.java))
        }
        binding.containerPrivacyPolicy.setOnClickListener {
            startActivity(Intent(requireActivity(), PrivacyPolicyNotionActivity::class.java))
        }
    }

    private fun setToolBar() {
        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setToggleButtons() {
        listOf(
            binding.tbAgreeOnTos,
            binding.tbAgreeOnPrivacyPolicy
        ).forEach {
            it.setOnClickListener {
                (binding.tbAgreeOnTos.isChecked && binding.tbAgreeOnPrivacyPolicy.isChecked).let {
                    binding.btnNext.setEnabledAndChangeColor(it)
                }
            }
        }
    }

    private fun setTexts() {
        binding.tvAgreeOnTos.setOnClickListener {
            binding.tbAgreeOnTos.toggle()
        }
        binding.tvAgreeOnPrivacyPolicy.setOnClickListener {
            binding.tbAgreeOnPrivacyPolicy.toggle()
        }
    }

    private fun setNextButton() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_toSFragment_to_registerRoleFragment)
        }
    }
}