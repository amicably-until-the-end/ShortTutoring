package org.softwaremaestro.presenter.question_upload.question_selected_upload

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import org.softwaremaestro.domain.question_selected_upload.entity.QuestionSelectedUploadVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionSelectedFormBinding
import org.softwaremaestro.presenter.question_upload.question_normal_upload.adapter.FormImageAdapter
import org.softwaremaestro.presenter.question_upload.question_normal_upload.adapter.TimeSelectAdapter
import org.softwaremaestro.presenter.question_upload.question_normal_upload.widget.DialogSchoolLevel
import org.softwaremaestro.presenter.question_upload.question_normal_upload.widget.DialogSchoolSubject
import org.softwaremaestro.presenter.question_upload.question_selected_upload.viewmodel.QuestionReservationViewModel
import org.softwaremaestro.presenter.question_upload.question_selected_upload.viewmodel.QuestionSelectedUploadViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.MyProfileViewModel
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.moveBack
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor
import org.softwaremaestro.presenter.util.toBase64
import org.softwaremaestro.presenter.util.widget.LoadingDialog
import org.softwaremaestro.presenter.util.widget.STTFirebaseAnalytics
import org.softwaremaestro.presenter.util.widget.STTFirebaseAnalytics.EVENT
import org.softwaremaestro.presenter.util.widget.SimpleAlertDialog
import java.time.LocalDateTime


@AndroidEntryPoint
class QuestionSelectedFormFragment : Fragment() {

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var dialogSchoolLevel: DialogSchoolLevel
    private lateinit var dialogSchoolSubject: DialogSchoolSubject

    lateinit var binding: FragmentQuestionSelectedFormBinding
    private val myProfileViewModel: MyProfileViewModel by activityViewModels()
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

        initDialog()
        myProfileViewModel.getMyProfile()
        resetViewModelValue()
        parseMathSubjectJson()
        setObserver()
        setToolBar()
        setImageRecyclerView()
        setSubmitButton()
        setFields()
        setTeacherId()
    }

    private fun initDialog() {
        initSchoolLevelDialog()
    }

    private fun initSchoolLevelDialog() {
        dialogSchoolLevel = DialogSchoolLevel {
            questionSelectedUploadViewModel.setSchoolLevel(it)
            initSchoolSubjectDialog(it)
            if (it == "모르겠어요") {
                questionSelectedUploadViewModel.setSchoolSubject(" ")
                binding.containerSubject.visibility = View.GONE
            } else {
                dialogSchoolSubject.show(parentFragmentManager, "dialogSchoolSubject")
                binding.containerSubject.visibility = View.VISIBLE
            }
        }
    }

    private fun initSchoolSubjectDialog(schoolLevel: String) {
        dialogSchoolSubject = DialogSchoolSubject(schoolLevel) {
            val subject = if (it == "모르겠어요") " " else it
            questionSelectedUploadViewModel.setSchoolSubject(subject)
        }
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

    private fun observeImages() {
        questionSelectedUploadViewModel.images.observe(viewLifecycleOwner) {
            imageAdapter.setItem(it!!)
            CoroutineScope(Dispatchers.IO).launch {
                questionSelectedUploadViewModel.setImagesBase64(it.map { it.toBase64() })
            }
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
        questionSelectedUploadViewModel.uploadedQuestionChatId.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Loading -> {
                    loadingDialog = LoadingDialog(requireContext())
                    binding.btnSubmit.setEnabledAndChangeColor(false)
                    loadingDialog.show()
                }

                is UIState.Success -> {
                    STTFirebaseAnalytics.logEvent(EVENT.QUESTION_RESERVED)
                    loadingDialog.dismiss()
                    val intent = Intent()
                    intent.putExtra(QUESTION_UPLOAD_RESULT, it.data)
                    requireActivity().setResult(Activity.RESULT_OK, intent)
                    requireActivity().finish()
                }

                else -> {
                    loadingDialog.dismiss()
                    binding.btnSubmit.setEnabledAndChangeColor(true)
                    SimpleAlertDialog().apply {
                        title = "질문 등록에 실패했습니다"
                        description = "잠시 후 다시 시도해주세요"
                    }.show(parentFragmentManager, "question upload fail")
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

                myProfileViewModel.amount.value ?: run {
                    Util.createToast(requireActivity(), "사용자의 코인을 가져오는데 실패했습니다").show()
                    return@setOnClickListener
                }

                //버튼 여러번 눌러지는 거 방지
                val questionSelectedUploadVO = QuestionSelectedUploadVO(
                    description = binding.etQuestionDesc.text.toString(),
                    schoolLevel = questionSelectedUploadViewModel.schoolLevel.value!!,
                    schoolSubject = questionSelectedUploadViewModel.schoolSubject.value!!,
                    mainImageIndex = 0,
                    images = questionSelectedUploadViewModel.imagesBase64.value!!,
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
                Util.createToast(requireActivity(), "사진을 등록해주세요").show()
            } else if (etQuestionDesc.text.isNullOrEmpty()) {
                Util.createToast(requireActivity(), "질문 내용을 입력해주세요").show()
            } else if (tvSchoolSelected.text.isNullOrEmpty()) {
                Util.createToast(requireActivity(), "학교를 선택해주세요").show()
            } else if (tvSubjectSelected.text.isNullOrEmpty()) {
                Util.createToast(requireActivity(), "과목을 선택해주세요").show()
            } else {
                Util.createToast(requireActivity(), "모든 항목을 입력해주세요").show()
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

        private const val TEACHER_ID = "teacherId"

        private const val QUESTION_ID = "questionId"
    }
}
