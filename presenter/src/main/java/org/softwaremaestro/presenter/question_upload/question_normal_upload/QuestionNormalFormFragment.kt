package org.softwaremaestro.presenter.question_upload.question_normal_upload

import android.app.Activity
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionNormalFormBinding
import org.softwaremaestro.presenter.question_upload.question_normal_upload.adapter.FormImageAdapter
import org.softwaremaestro.presenter.question_upload.question_normal_upload.adapter.TimeSelectAdapter
import org.softwaremaestro.presenter.question_upload.question_normal_upload.viewmodel.QuestionUploadViewModel
import org.softwaremaestro.presenter.question_upload.question_normal_upload.widget.DialogSchoolLevel
import org.softwaremaestro.presenter.question_upload.question_normal_upload.widget.DialogSchoolSubject
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.moveBack
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor
import org.softwaremaestro.presenter.util.toBase64
import org.softwaremaestro.presenter.util.widget.LoadingDialog
import org.softwaremaestro.presenter.util.widget.SimpleAlertDialog
import org.softwaremaestro.presenter.util.widget.TimePickerBottomDialog
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@AndroidEntryPoint
class QuestionNormalFormFragment : Fragment() {

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var dialogSchoolLevel: DialogSchoolLevel
    private lateinit var dialogSchoolSubject: DialogSchoolSubject

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
    ): View {

        binding = FragmentQuestionNormalFormBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDialog()
        parseMathSubjectJson()
        setObserver()
        setToolBar()
        setImageRecyclerView()
        setDesiredTimeRecyclerView()
        checkAndEnableSubjectBtn()
        setSubmitButton()
        setFields()
    }

    private fun initDialog() {
        initSchoolLevelDialog()
    }

    private fun initSchoolLevelDialog() {
        dialogSchoolLevel = DialogSchoolLevel {
            viewModel._school.value = it
            initSchoolSubjectDialog(it)
            dialogSchoolSubject.show(parentFragmentManager, "dialogSchoolSubject")
        }
    }

    private fun initSchoolSubjectDialog(schoolLevel: String) {
        dialogSchoolSubject = DialogSchoolSubject(schoolLevel) {
            viewModel._subject.value = it
        }
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
        dialogSchoolLevel.show(parentFragmentManager, "dialogSchoolLevel")
    }

    private fun showSubjectSelectDialog() {
        dialogSchoolSubject.show(parentFragmentManager, "dialogSchoolSubject")
    }


    private fun setImageRecyclerView() {
        imageAdapter = FormImageAdapter {
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
        timeSelectAdapter = TimeSelectAdapter {
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
            CoroutineScope(Dispatchers.IO).launch {
                viewModel._imagesBase64.postValue(it.map { it.toBase64() })
            }
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
                    SimpleAlertDialog().apply {
                        title = "질문 등록에 실패했습니다"
                        description = "잠시 후 다시 시도해주세요"
                    }
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
                        images = viewModel.imagesBase64.value!!,
                        description = binding.etQuestionDesc.text.toString(),
                        schoolLevel = binding.tvSchoolSelected.text.toString(),
                        schoolSubject = binding.tvSubjectSelected.text.toString(),
                        hopeImmediate = binding.toggleAnswerNow.isChecked,
                        hopeTutoringTime = timeSelectAdapter!!.items.map {
                            it.toString()
                        }.let {
                            if (binding.toggleAnswerNow.isChecked) {
                                mutableListOf(
                                    LocalTime.now()
                                        .format(DateTimeFormatter.ofPattern("hh시 mm분"))
                                ).apply {
                                    addAll(it)
                                }
                            } else {
                                it
                            }
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
            moveBack()
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