package org.softwaremaestro.presenter.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentTosBinding
import org.softwaremaestro.presenter.login.viewmodel.StudentRegisterViewModel
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.Util.getWidth

@AndroidEntryPoint
class ToSFragment : Fragment() {

    private lateinit var binding: FragmentTosBinding
    private var agreeAll = false
    private val viewModel: StudentRegisterViewModel by activityViewModels()
    private var isSmallSizeScreen = false

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
        supportSmallScreenSize()
        loadViewModelValue()
        setToolBar()
        setToggleButtons()
        observe()
        setTexts()
        setNextButton()
        setTosContainer()
        setPrivacyPolicyContainer()
    }

    private fun supportSmallScreenSize() {
        val width = getWidth(requireActivity())
        isSmallSizeScreen = width < 600
        if (isSmallSizeScreen) {
            val paddingValue = Util.toPx(30, requireContext())
            binding.glLeft.setGuidelineBegin(paddingValue)
            binding.glRight.setGuidelineEnd(paddingValue)
        }
    }

    private fun loadViewModelValue() {
        if (viewModel.agreeAll.value == true) {
            binding.tbAgreeOnTos.isChecked = true
            binding.tbAgreeOnPrivacyPolicy.isChecked = true
        } else {
            binding.tbAgreeOnTos.isChecked = false
            binding.tbAgreeOnPrivacyPolicy.isChecked = false
        }
    }

    private fun setToolBar() {
        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setToggleButtons() {
        binding.tbAgreeOnTos.setOnCheckedChangeListener { _, checked ->
            viewModel.setAgreeOnToS(checked)
        }

        binding.tbAgreeOnPrivacyPolicy.setOnCheckedChangeListener { _, checked ->
            viewModel.setAgreeOnPrivacyPolicy(checked)
        }
    }

    private fun observe() {
        viewModel.agreeAll.observe(viewLifecycleOwner) {
            with(binding.btnNext) {
                if (it == true) {
                    setBackgroundResource(R.drawable.bg_radius_5_grad_blue)
                    setTextColor(resources.getColor(R.color.white, null))
                    agreeAll = true
                } else {
                    setBackgroundResource(R.drawable.bg_radius_5_grey)
                    setTextColor(resources.getColor(R.color.sub_text_grey, null))
                    agreeAll = false
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
            if (agreeAll) {
                findNavController().navigate(R.id.action_toSFragment_to_registerRoleFragment)
            } else {
                Util.createToast(requireActivity(), "약관에 동의해주세요").show()
            }
        }
    }

    private fun setTosContainer() {
        binding.containerTosBody.setOnClickListener {
            startActivity(Intent(requireActivity(), ToSNotionActivity::class.java))
        }
    }

    private fun setPrivacyPolicyContainer() {
        binding.containerPrivacyPolicyBody.setOnClickListener {
            startActivity(Intent(requireActivity(), PrivacyPolicyNotionActivity::class.java))
        }
    }
}