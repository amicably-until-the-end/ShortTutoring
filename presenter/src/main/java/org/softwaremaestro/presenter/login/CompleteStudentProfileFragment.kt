package org.softwaremaestro.presenter.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentCompleteStudentProfileBinding
import org.softwaremaestro.presenter.login.viewmodel.StudentRegisterViewModel
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.Util.showKeyboardAndRequestFocus
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor
import org.softwaremaestro.presenter.util.widget.LoadingDialog
import org.softwaremaestro.presenter.util.widget.ProfileImageSelectBottomDialog
import org.softwaremaestro.presenter.util.widget.STTFirebaseAnalytics
import org.softwaremaestro.presenter.util.widget.STTFirebaseAnalytics.EVENT

@AndroidEntryPoint
class CompleteStudentProfileFragment : Fragment() {

    private lateinit var binding: FragmentCompleteStudentProfileBinding
    private val viewModel: StudentRegisterViewModel by activityViewModels()
    private lateinit var dialog: ProfileImageSelectBottomDialog
    private var isBtnCompleteEnabled = false
    private var isSmallSizeScreen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompleteStudentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportSmallScreenSize()
        hideButtonsUponDisplaySize()
        setViewModelValueToFields()
        setEtProfileStudentName()
        setBtnEditTeacherImage()
        setTvProfileStudentGrade()
        setEtStudentName()
        setBtnComplete()
        setBtnToolbarBack()
        observe()
    }

    private fun supportSmallScreenSize() {
        val width = Util.getWidth(requireActivity())
        isSmallSizeScreen = width < 600
        if (isSmallSizeScreen) {
            val paddingValue = Util.toDp(20, requireContext())
            binding.glLeft.setGuidelineBegin(paddingValue)
            binding.glRight.setGuidelineBegin(paddingValue)
        }
    }

    private fun hideButtonsUponDisplaySize() {
        val metrics = resources.displayMetrics
        val screenWidth = metrics.widthPixels
        if (screenWidth < 1200) {
            binding.btnFollow.visibility = View.INVISIBLE
            binding.containerReserve.visibility = View.INVISIBLE
        }
    }

    private fun setViewModelValueToFields() {
        viewModel.name.value?.let {
            binding.etProfileStudentName.setText(it)
            binding.etStudentName.setText(it)
        }
        viewModel._image.value?.let {
            val resId = Animal.toResId(it)
            binding.ivStudentImg.setBackgroundResource(resId)
        }
    }

    private fun setBtnEditTeacherImage() {
        binding.containerStudentImg.setOnClickListener {
            dialog = ProfileImageSelectBottomDialog(
                onImageChanged = { image ->
                    binding.ivStudentImg.setBackgroundResource(image)
                },
                onSelect = { image ->
                    viewModel._image.value = Animal.toName(image)
                    dialog.dismiss()
                },
            ).apply {
                isCancelable = false
            }
            dialog.show(parentFragmentManager, "profileImageSelectBottomDialog")
        }
    }

    private fun observe() {
        observeName()
        observeStudentNameAndImageProper()
        observeSignupState()
    }

    private fun observeName() {
        viewModel.name.observe(viewLifecycleOwner) {
            binding.etProfileStudentName.setText(it)
        }
    }

    private fun observeStudentNameAndImageProper() {
        viewModel.studentNameAndImageProper.observe(viewLifecycleOwner) {
            with(binding.btnComplete) {
                if (it) {
                    setBackgroundResource(R.drawable.bg_radius_5_grad_blue)
                    setTextColor(resources.getColor(R.color.white, null))
                } else {
                    setBackgroundResource(R.drawable.bg_radius_5_grey)
                    setTextColor(resources.getColor(R.color.sub_text_grey, null))
                }
            }
            isBtnCompleteEnabled = it
        }
    }

    private fun observeSignupState() {
        val loadingDialog = LoadingDialog(requireContext())
        viewModel.studentSignupState.observe(viewLifecycleOwner) {

            when (it) {
                is UIState.Loading -> {
                    loadingDialog.show()
                    binding.btnComplete.setEnabledAndChangeColor(false)
                }

                is UIState.Success -> {
                    loadingDialog.dismiss()
                    STTFirebaseAnalytics.logEvent(EVENT.REGISTER, "role", "student")
                    findNavController().navigate(R.id.action_completeStudentProfileFragment_to_loginFragment)
                }

                else -> {
                    loadingDialog.dismiss()
                    binding.btnComplete.setEnabledAndChangeColor(true)
                    Util.createToast(requireActivity(), "다시 시도해주세요.").show()
                }
            }
        }
    }

    private fun setEtProfileStudentName() {
        binding.etProfileStudentName.setOnClickListener {
            showKeyboardAndRequestFocus(binding.etStudentName)
        }
    }

    private fun setTvProfileStudentGrade() {
        binding.tvProfileStudentGrade.text =
            "${viewModel.schoolLevel.value} ${viewModel.schoolGrade.value}학년"
    }

    private fun setEtStudentName() {
        binding.etStudentName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0 ?: return
                if (p0.isNotEmpty()) {
                    binding.etStudentName.hint = ""
                    binding.etProfileStudentName.hint = ""
                } else {
                    binding.etStudentName.hint = "학생 닉네임"
                    binding.etProfileStudentName.hint = "학생 닉네임"
                }
                viewModel.setName(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun setBtnToolbarBack() {
        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setBtnComplete() {
        binding.btnComplete.setOnClickListener {
            if (isBtnCompleteEnabled) {
                viewModel.registerStudent()
            } else {
                Util.createToast(requireActivity(), "닉네임과 프로필 이미지를 설정해주세요")
                    .show()
            }
        }
    }
}