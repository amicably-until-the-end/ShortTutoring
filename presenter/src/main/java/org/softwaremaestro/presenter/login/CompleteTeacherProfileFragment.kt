package org.softwaremaestro.presenter.login

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentCompleteTeacherProfileBinding
import org.softwaremaestro.presenter.login.viewmodel.TeacherRegisterViewModel
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor
import org.softwaremaestro.presenter.util.showKeyboardAndRequestFocus
import org.softwaremaestro.presenter.util.toBase64
import org.softwaremaestro.presenter.util.widget.LoadingDialog
import org.softwaremaestro.presenter.util.widget.ProfileImageSelectBottomDialog


class CompleteTeacherProfileFragment : Fragment() {

    private lateinit var binding: FragmentCompleteTeacherProfileBinding
    private val viewModel: TeacherRegisterViewModel by activityViewModels()
    private lateinit var dialog: ProfileImageSelectBottomDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompleteTeacherProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun setBtnEditTeacherImage() {
        binding.containerTeacherImg.setOnClickListener {
            dialog = ProfileImageSelectBottomDialog(
                onImageChanged = { image ->
                    binding.ivTeacherImg.setBackgroundResource(image)
                },
                onSelect = { image ->
                    viewModel._image.value = BitmapFactory.decodeResource(
                        resources, image
                    ).toBase64()
                    dialog.dismiss()
                },
            )
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
            viewModel.registerTeacher()
        }
    }

    private fun observe() {
        observeName()
        observeBio()
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

    private fun observeInputProper() {
        viewModel.teacherInputProper.observe(viewLifecycleOwner) {
            binding.btnComplete.setEnabledAndChangeColor(it)
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
                    findNavController().navigate(R.id.action_completeStudentProfileFragment_to_loginFrament)
                }

                else -> {
                    loadingDialog.dismiss()
                    binding.btnComplete.setEnabledAndChangeColor(true)
                    Toast.makeText(requireContext(), "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}