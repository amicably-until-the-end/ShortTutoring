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
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentCompleteTeacherProfileBinding
import org.softwaremaestro.presenter.login.viewmodel.TeacherRegisterViewModel
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.Util.showKeyboardAndRequestFocus
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor
import org.softwaremaestro.presenter.util.widget.LoadingDialog
import org.softwaremaestro.presenter.util.widget.ProfileImageSelectBottomDialog
import org.softwaremaestro.presenter.util.widget.STTFirebaseAnalytics


class CompleteTeacherProfileFragment : Fragment() {

    private lateinit var binding: FragmentCompleteTeacherProfileBinding
    private val viewModel: TeacherRegisterViewModel by activityViewModels()
    private lateinit var dialog: ProfileImageSelectBottomDialog
    private var isBtnCompleteEnabled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompleteTeacherProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideButtonsUponDisplaySize()
        setViewModelValueToFields()
        setEtProfileTeacherName()
        setBtnEditTeacherImage()
        setTvProfileTeacherUniv()
        setEtProfileTeacherBio()
        setEtTeacherName()
        setEtTeacherBio()
        setBtnToolbarBack()
        setBtnComplete()
        observe()
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
            binding.etProfileTeacherName.setText(it)
            binding.etTeacherName.setText(it)
        }
        viewModel.bio.value?.let {
            binding.etProfileTeacherBio.setText(it)
            binding.etTeacherBio.setText(it)
        }
        viewModel._image.value?.let {
            val resId = Animal.toResId(it)
            binding.ivTeacherImg.setBackgroundResource(resId)
        }
    }

    private fun setBtnEditTeacherImage() {
        binding.containerTeacherImg.setOnClickListener {
            dialog = ProfileImageSelectBottomDialog(
                onImageChanged = { image ->
                    binding.ivTeacherImg.setBackgroundResource(image)
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

    private fun setEtProfileTeacherName() {
        binding.etProfileTeacherName.setOnClickListener {
            showKeyboardAndRequestFocus(binding.etTeacherName)
        }
    }

    private fun setTvProfileTeacherUniv() {
        binding.tvProfileTeacherUniv.text = viewModel.schoolName.value
    }

    private fun setEtProfileTeacherBio() {
        binding.etProfileTeacherBio.setOnClickListener {
            showKeyboardAndRequestFocus(binding.etTeacherBio)
        }
    }

    private fun setBtnToolbarBack() {
        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setEtTeacherName() {
        binding.etTeacherName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0 ?: return
                if (p0.isNotEmpty()) {
                    binding.etTeacherName.hint = ""
                    binding.etProfileTeacherName.hint = ""
                } else {
                    binding.etTeacherName.hint = "선생님 닉네임"
                    binding.etProfileTeacherName.hint = "선생님 닉네임"
                }
                viewModel._name.value = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun setEtTeacherBio() {
        binding.etTeacherBio.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel._bio.value = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun setBtnComplete() {
        binding.btnComplete.setOnClickListener {
            if (isBtnCompleteEnabled) {
                viewModel.registerTeacher()
            } else {
                alertEmptyField()
            }
        }
    }

    private fun alertEmptyField() {
        val msg = if (viewModel.name.value.isNullOrEmpty()) {
            "닉네임을 입력해주세요"
        } else if (viewModel.bio.value.isNullOrEmpty()) {
            "한줄 소개를 입력해주세요"
        } else {
            "프로필 이미지를 설정해주세요"
        }
        Util.createToast(requireActivity(), msg).show()
    }

    private fun observe() {
        observeName()
        observeBio()
        observeImage()
        observeInputProper()
        observeSignupState()
    }

    private fun observeName() {
        viewModel.name.observe(viewLifecycleOwner) {
            binding.etProfileTeacherName.setText(it)
        }
    }

    private fun observeBio() {
        viewModel.bio.observe(viewLifecycleOwner) {
            binding.etProfileTeacherBio.setText(it)
        }
    }

    private fun observeImage() {
        viewModel.image.observe(viewLifecycleOwner) { image ->
            val resId = Animal.toResId(image)
            binding.ivTeacherImg.setBackgroundResource(resId)
        }
    }

    private fun observeInputProper() {
        viewModel.teacherInputProper.observe(viewLifecycleOwner) {
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
        viewModel.teacherSignupState.observe(viewLifecycleOwner) {

            when (it) {
                is UIState.Loading -> {
                    loadingDialog.show()
                    binding.btnComplete.setEnabledAndChangeColor(false)
                }

                is UIState.Success -> {
                    loadingDialog.dismiss()
                    STTFirebaseAnalytics.logEvent(
                        STTFirebaseAnalytics.EVENT.REGISTER,
                        "role",
                        "teacher"
                    )
                    findNavController().navigate(R.id.action_completeTeacherProfileFragment_to_loginFragment)
                }

                else -> {
                    loadingDialog.dismiss()
                    binding.btnComplete.setEnabledAndChangeColor(true)
                    Util.createToast(requireActivity(), "다시 시도해주세요.").show()
                }
            }
        }
    }
}