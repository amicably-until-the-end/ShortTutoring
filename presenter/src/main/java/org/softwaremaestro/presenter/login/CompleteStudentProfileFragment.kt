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
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentCompleteStudentProfileBinding
import org.softwaremaestro.presenter.login.viewmodel.StudentRegisterViewModel
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor
import org.softwaremaestro.presenter.util.showKeyboardAndRequestFocus
import org.softwaremaestro.presenter.util.toBase64
import org.softwaremaestro.presenter.util.widget.LoadingDialog
import org.softwaremaestro.presenter.util.widget.ProfileImageSelectBottomDialog

@AndroidEntryPoint
class CompleteStudentProfileFragment : Fragment() {

    private lateinit var binding: FragmentCompleteStudentProfileBinding
    private val viewModel: StudentRegisterViewModel by activityViewModels()
    private lateinit var dialog: ProfileImageSelectBottomDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompleteStudentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEtProfileStudentName()
        setBtnEditTeacherImage()
        setTvProfileStudentGrade()
        setEtStudentName()
        setBtnComplete()
        setBtnToolbarBack()
        observe()
    }

    private fun setBtnEditTeacherImage() {
        binding.containerStudentImg.setOnClickListener {
            dialog = ProfileImageSelectBottomDialog(
                onImageChanged = { image ->
                    binding.ivStudentImg.setBackgroundResource(image)
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
            binding.btnComplete.setEnabledAndChangeColor(it)
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
            viewModel.registerStudent()
        }
    }
}