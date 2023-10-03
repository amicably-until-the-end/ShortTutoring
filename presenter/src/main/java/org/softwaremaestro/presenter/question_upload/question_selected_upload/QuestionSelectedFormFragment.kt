package org.softwaremaestro.presenter.question_upload.question_selected_upload

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
import org.softwaremaestro.domain.question_selected_upload.entity.QuestionSelectedUploadVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionSelectedFormBinding
import org.softwaremaestro.presenter.question_upload.question_normal_upload.adapter.FormImageAdapter
import org.softwaremaestro.presenter.question_upload.question_normal_upload.adapter.TimeSelectAdapter
import org.softwaremaestro.presenter.question_upload.question_selected_upload.viewmodel.QuestionReservationViewModel
import org.softwaremaestro.presenter.question_upload.question_selected_upload.viewmodel.QuestionSelectedUploadViewModel
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor
import org.softwaremaestro.presenter.util.toBase64
import org.softwaremaestro.presenter.util.widget.LoadingDialog
import java.time.LocalDateTime


@AndroidEntryPoint
class QuestionSelectedFormFragment : Fragment() {

    private lateinit var loadingDialog: LoadingDialog

    lateinit var binding: FragmentQuestionSelectedFormBinding
    private val questionSelectedUploadViewModel: QuestionSelectedUploadViewModel by activityViewModels()
    private val questionReservationViewModel: QuestionReservationViewModel by activityViewModels()


    //recyclerView adapters
    private lateinit var imageAdapter: FormImageAdapter
    private var timeSelectAdapter: TimeSelectAdapter? = null


    private var mathSubjects = HashMap<String, HashMap<String, HashMap<String, Int>>>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentQuestionSelectedFormBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resetViewModelValue()
        parseMathSubjectJson()
        setObserver()
        setToolBar()
        setImageRecyclerView()
        setSubmitButton()
        setFields()
        setTeacherId()
    }

    private fun resetViewModelValue() {
        questionSelectedUploadViewModel.resetInputs()
    }

    private fun setTeacherId() {
        requireActivity().intent.getStringExtra("teacher-id")
            ?.let { questionSelectedUploadViewModel.setTeacherId(it) }
    }

    /**
    뷰를 클릭하면 해당 뷰에 값을 입력하는 페이지로 이동한다
     */
    private fun setFields() {
        binding.etQuestionDesc.addTextChangedListener {
            questionSelectedUploadViewModel.setDescription(
                if (it.isNullOrEmpty()) null
                else it.toString()
            )
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
    }

    private fun showSchoolSelectDialog() {
        AlertDialog.Builder(requireContext())
            .setItems(mathSubjects.keys.toTypedArray()) { _, which ->
                val selectedSchool = mathSubjects.keys.toTypedArray()[which]
                questionSelectedUploadViewModel.setSchoolLevel(selectedSchool)
                showSubjectSelectDialog()
            }
            .setTitle("학교")
            .setPositiveButton("확인", null)
            .setNegativeButton("취소", null)
            .create().show()

    }

    private fun showSubjectSelectDialog() {
        var subjects =
            mathSubjects[questionSelectedUploadViewModel.schoolLevel.value]?.keys?.toTypedArray()!!

        AlertDialog.Builder(requireContext())
            .setItems(subjects) { _, which ->
                val selectedSubject = subjects[which]
                questionSelectedUploadViewModel.setSchoolSubject(selectedSubject)
            }
            .setTitle("교과 과정")
            .setPositiveButton("확인", null)
            .setNegativeButton("취소", null)
            .create().show()
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

    private fun observeImages() {
        questionSelectedUploadViewModel.images.observe(viewLifecycleOwner) {
            it?.let { imageAdapter.setItem(it) }
        }
    }


    private fun observeSchoolLevel() {
        questionSelectedUploadViewModel.schoolLevel.observe(viewLifecycleOwner) {
            binding.tvSchoolSelected.text = it
        }
    }

    private fun observeSubject() {
        questionSelectedUploadViewModel.schoolSubject.observe(viewLifecycleOwner) {
            binding.tvSubjectSelected.text = it
        }
    }


    private fun observeQuestionUploadState() {
        questionSelectedUploadViewModel.questionUploadState.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Loading -> {
                    loadingDialog = LoadingDialog(requireContext())
                    binding.btnSubmit.setEnabledAndChangeColor(false)
                    loadingDialog.show()
                }

                is UIState.Success -> {
                    loadingDialog.dismiss()
                    val intent = Intent()
                    intent.putExtra(QUESTION_UPLOAD_RESULT, QUESTION_ID)
                    requireActivity().setResult(Activity.RESULT_OK, intent)
                    requireActivity().finish()
                }

                else -> {
                    loadingDialog.dismiss()
                    binding.btnSubmit.setEnabledAndChangeColor(true)
                    Toast.makeText(requireContext(), "질문 등록에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun setObserver() {
        observeImages()
        observeSchoolLevel()
        observeSubject()
        observeInputProper()
        observeQuestionUploadState()

    }

    private fun observeInputProper() {
        questionSelectedUploadViewModel.inputProper.observe(viewLifecycleOwner) {
            binding.btnSubmit.setEnabledAndChangeColor(it)
        }
    }

    private fun navigateToCamera() {
        findNavController().navigate(R.id.action_questionSelectedFormFragment_to_questionCameraFragment)
    }

    /**
     * 제출 버튼을 클릭하면 과외 요청을 보낸다.
     */
    private fun setSubmitButton() {
        binding.btnSubmit.apply {
            setOnClickListener {
//                if (isAllFieldsEntered()) {
                //버튼 여러번 눌러지는 거 방지
                val questionSelectedUploadVO = QuestionSelectedUploadVO(
                    description = binding.etQuestionDesc.text.toString(),
                    schoolLevel = binding.tvSchoolSelected.text.toString(),
                    schoolSubject = binding.tvSubjectSelected.text.toString(),
                    mainImageIndex = 0,
                    images = questionSelectedUploadViewModel.images.value!!.map {
                        it.toBase64()
                    },
                    requestTutoringStartTime = LocalDateTime.of(
                        questionReservationViewModel.requestDate.value!!,
                        questionReservationViewModel.requestTutoringStartTime.value!!
                    ),
                    requestTutoringEndTime = LocalDateTime.of(
                        questionReservationViewModel.requestDate.value!!,
                        questionReservationViewModel.requestTutoringEndTime.value!!
                    ),
                    requestTeacherId = questionSelectedUploadViewModel.teacherId.value!!
                )
                questionSelectedUploadViewModel.uploadQuestionSelected(questionSelectedUploadVO)
//                } else {
//                    alertEmptyField()
//                }
            }
        }
    }

    private fun alertEmptyField() {
        with(binding) {
            if (questionSelectedUploadViewModel.images.value.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "사진을 등록해주세요", Toast.LENGTH_SHORT).show()
            } else if (etQuestionDesc.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "질문 내용을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else if (tvSchoolSelected.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "학교를 선택해주세요", Toast.LENGTH_SHORT).show()
            } else if (tvSubjectSelected.text.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "과목을 선택해주세요", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isAllFieldsEntered(): Boolean {
        with(binding) {
            return (questionSelectedUploadViewModel.images.value != null &&
                    etQuestionDesc.text != null && tvSchoolSelected.text != null
                    && tvSubjectSelected.text != null)
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

        private const val TEACHER_ID = "teacherId"

        private const val QUESTION_ID = "questionId"
    }
}
