package org.softwaremaestro.presenter.question_upload.question_normal_upload

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionNormalFormBinding
import org.softwaremaestro.presenter.question_upload.question_normal_upload.adapter.FormImageAdapter
import org.softwaremaestro.presenter.question_upload.question_normal_upload.adapter.TimeSelectAdapter
import org.softwaremaestro.presenter.question_upload.question_normal_upload.viewmodel.QuestionUploadViewModel
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor
import org.softwaremaestro.presenter.util.toBase64
import org.softwaremaestro.presenter.util.widget.LoadingDialog
import org.softwaremaestro.presenter.util.widget.TimePickerBottomDialog
import java.text.SimpleDateFormat


@AndroidEntryPoint
class QuestionNormalFormFragment : Fragment() {

    private lateinit var loadingDialog: LoadingDialog

    lateinit var binding: FragmentQuestionNormalFormBinding
    private val viewModel: QuestionUploadViewModel by activityViewModels()


    //recyclerView adapters
    private lateinit var imageAdapter: FormImageAdapter
    private var timeSelectAdapter: TimeSelectAdapter? = null


    private var mathSubjects = HashMap<String, HashMap<String, HashMap<String, Int>>>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentQuestionNormalFormBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        parseMathSubjectJson()
        setObserver()
        setToolBar()
        setImageRecyclerView()
        setDesiredTimeRecyclerView()
        checkAndEnableSubjectBtn()
        setSubmitButton()
        setFields()
    }

    /**
     *  모든 내용이 입력되었으면 제출 버튼을 활성화한다
     */
    private fun checkAndEnableSubjectBtn() {
        isAllFieldsEntered().let {
            binding.btnSubmit.setEnabledAndChangeColor(it)
        }
    }

    /**
    뷰를 클릭하면 해당 뷰에 값을 입력하는 페이지로 이동한다
     */
    private fun setFields() {
        binding.etQuestionDesc.setText(viewModel.description.value)
        binding.etQuestionDesc.addTextChangedListener {
            viewModel._description.value = it.toString()
        }
        binding.btnSchoolSelect.setOnClickListener {
            showSchoolSelectDialog()
        }
        binding.btnSubjectSelect.setOnClickListener {
            if (binding.tvSchoolSelected.text.isNullOrEmpty()) {
                showSchoolSelectDialog()
            } else {
                showSubjectSelectDialog()
            }
        }
        setAnswerNowToggleBtn()
    }

    private fun showSchoolSelectDialog() {
        AlertDialog.Builder(requireContext())
            .setItems(mathSubjects.keys.toTypedArray()) { _, which ->
                val selectedSchool = mathSubjects.keys.toTypedArray()[which]
                viewModel._school.value = selectedSchool
                showSubjectSelectDialog()
            }
            .setTitle("학교")
            .setPositiveButton("확인", null)
            .setNegativeButton("취소", null)
            .create().show()

    }

    private fun showSubjectSelectDialog() {
        var subjects =
            mathSubjects[viewModel.school.value]?.keys?.toTypedArray()!!

        AlertDialog.Builder(requireContext())
            .setItems(subjects) { _, which ->
                val selectedSchool = subjects[which]
                viewModel._subject.value = selectedSchool
            }
            .setTitle("교과 과정")
            .setPositiveButton("확인", null)
            .setNegativeButton("취소", null)
            .create().show()
    }


    private fun setImageRecyclerView() {
        imageAdapter = FormImageAdapter() {
            navigateToCamera()
        }

        binding.rvQuestionImages.apply {
            adapter = imageAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }

    }

    private fun setDesiredTimeRecyclerView() {
        var currentTime = System.currentTimeMillis()
        var hour: Int = SimpleDateFormat("HH").format(currentTime).toInt()
        var time: Int = SimpleDateFormat("mm").format(currentTime).toInt()
        timeSelectAdapter = TimeSelectAdapter() {
            val picker = TimePickerBottomDialog {
                timeSelectAdapter?.items?.add(it)
                checkAndEnableSubjectBtn()
                timeSelectAdapter?.notifyDataSetChanged()
            }.apply {
                setTitle("희망 수업 시간을 선택해주세요")
            }
            picker.show(parentFragmentManager, "timePicker")
        }

        binding.rvDesiredTime.apply {
            adapter = timeSelectAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }


    }

    private fun setAnswerNowToggleBtn() {
        binding.toggleAnswerNow.setOnClickListener {
            checkAndEnableSubjectBtn()
        }
    }

    private fun observeImages() {
        viewModel.images.observe(viewLifecycleOwner) {
            imageAdapter.setItem(it!!)
            checkAndEnableSubjectBtn()
        }
    }


    private fun observeSchoolLevel() {
        viewModel.school.observe(viewLifecycleOwner) {
            binding.tvSchoolSelected.text = it
            checkAndEnableSubjectBtn()
        }
    }

    private fun observeSubject() {
        viewModel.subject.observe(viewLifecycleOwner) {
            binding.tvSubjectSelected.text = it
            checkAndEnableSubjectBtn()
        }
    }


    private fun observeQuestionId() {
        viewModel.questionUploadState.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Loading -> {
                    loadingDialog = LoadingDialog(requireContext())
                    binding.btnSubmit.setEnabledAndChangeColor(false)
                    loadingDialog.show()
                }

                is UIState.Success -> {
                    loadingDialog.dismiss()
                    val intent = Intent()
                    intent.putExtra(QUESTION_UPLOAD_RESULT, it.data.questionId)
                    activity?.setResult(Activity.RESULT_OK, intent)
                    activity?.finish()
                }

                else -> {
                    loadingDialog.dismiss()
                    binding.btnSubmit.setEnabledAndChangeColor(true)
                    Toast.makeText(requireContext(), "질문 등록에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        checkAndEnableSubjectBtn()
    }


    private fun setObserver() {
        observeImages()
        observeSchoolLevel()
        observeSubject()
        observeQuestionId()
    }

    private fun navigateToCamera() {
        findNavController().navigate(R.id.action_questionNormalFormFragment_to_questionCameraFragment)
    }

    /**
     * 제출 버튼을 클릭하면 과외 요청을 보낸다.
     */
    private fun setSubmitButton() {
        binding.btnSubmit.apply {
            setOnClickListener {
                if (isAllFieldsEntered()) {
                    //버튼 여러번 눌러지는 거 방지
                    val questionUploadVO = QuestionUploadVO(
                        images = viewModel.images.value!!.map { it.toBase64() },
                        description = binding.etQuestionDesc.text.toString(),
                        schoolLevel = binding.tvSchoolSelected.text.toString(),
                        schoolSubject = binding.tvSubjectSelected.text.toString(),
                        hopeImmediate = binding.toggleAnswerNow.isChecked,
                        hopeTutoringTime = timeSelectAdapter!!.items.map {
                            it.toString()
                        },
                        mainImageIndex = 0
                    )
                    viewModel.uploadQuestion(questionUploadVO)
                } else {
                    alertEmptyField()
                }
            }
        }
    }

    private fun alertEmptyField() {
        with(binding) {
            if (viewModel.images.value.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "사진을 등록해주세요", Toast.LENGTH_SHORT).show()
            } else if (etQuestionDesc.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "질문 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if (tvSchoolSelected.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "학교를 선택해주세요", Toast.LENGTH_SHORT).show()
            } else if (tvSubjectSelected.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "과목을 선택해주세요", Toast.LENGTH_SHORT).show()
            } else if (!toggleAnswerNow.isChecked || timeSelectAdapter?.items.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "희망 답변 시간을 선택해주세요", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isAllFieldsEntered(): Boolean {
        with(binding) {
            return (viewModel.images.value != null &&
                    etQuestionDesc.text != null && tvSchoolSelected.text != null
                    && tvSubjectSelected.text != null && (
                    toggleAnswerNow.isChecked || !timeSelectAdapter?.items.isNullOrEmpty()))
        }
    }

    private fun setToolBar() {
        binding.btnToolbarBack.setOnClickListener {
            activity?.finish()
        }
    }

    private fun parseMathSubjectJson() {
        val assetsManager = resources.assets
        try {
            val inputStream = assetsManager.open("mathSubjectLabels.json")
            val reader = inputStream.bufferedReader()
            val gson = Gson()
            // Define the type of the outermost structure
            val type =
                object : TypeToken<HashMap<String, HashMap<String, HashMap<String, Int>>>>() {}.type
            // Parse the JSON string and populate mathSubjects
            mathSubjects = gson.fromJson(reader, type)
        } catch (e: Exception) {
            return
        }
    }


    companion object {
        const val QUESTION_UPLOAD_RESULT = "questionUploadResult"
    }
}