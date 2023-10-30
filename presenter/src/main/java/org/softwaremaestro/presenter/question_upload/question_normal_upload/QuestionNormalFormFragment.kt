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
import org.softwaremaestro.presenter.coin.viewModel.CoinViewModel
import org.softwaremaestro.presenter.databinding.FragmentQuestionNormalFormBinding
import org.softwaremaestro.presenter.question_upload.question_normal_upload.adapter.FormImageAdapter
import org.softwaremaestro.presenter.question_upload.question_normal_upload.adapter.TimeSelectAdapter
import org.softwaremaestro.presenter.question_upload.question_normal_upload.viewmodel.QuestionUploadViewModel
import org.softwaremaestro.presenter.question_upload.question_normal_upload.widget.DialogSchoolLevel
import org.softwaremaestro.presenter.question_upload.question_normal_upload.widget.DialogSchoolSubject
import org.softwaremaestro.presenter.student_home.viewmodel.MyProfileViewModel
import org.softwaremaestro.presenter.util.UIState
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.moveBack
import org.softwaremaestro.presenter.util.toBase64
import org.softwaremaestro.presenter.util.widget.DetailAlertDialog
import org.softwaremaestro.presenter.util.widget.LoadingDialog
import org.softwaremaestro.presenter.util.widget.STTFirebaseAnalytics
import org.softwaremaestro.presenter.util.widget.STTFirebaseAnalytics.EVENT
import org.softwaremaestro.presenter.util.widget.SimpleAlertDialog
import org.softwaremaestro.presenter.util.widget.TimePickerBottomDialog
import java.time.LocalDateTime


@AndroidEntryPoint
class QuestionNormalFormFragment : Fragment() {

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var dialogSchoolLevel: DialogSchoolLevel
    private lateinit var dialogSchoolSubject: DialogSchoolSubject

    lateinit var binding: FragmentQuestionNormalFormBinding
    private val viewModel: QuestionUploadViewModel by activityViewModels()
    private val myProfileViewModel: MyProfileViewModel by activityViewModels()
    private val coinViewModel: CoinViewModel by activityViewModels()


    //recyclerView adapters
    private lateinit var imageAdapter: FormImageAdapter
    private var timeSelectAdapter: TimeSelectAdapter? = null


    private var mathSubjects = HashMap<String, HashMap<String, HashMap<String, Int>>>()

    // 질문 업로드 시간동안 분이 달라지는 경우를 방지하기 위해
    // now의 값을 프래그먼트 생성 시점의 시간으로 고정
    private val now = LocalDateTime.now()
    private val nowTime = TimePickerBottomDialog {}.SpecificTime(now.hour, now.minute)

    private var submitBtnEnabled = false


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

        myProfileViewModel.getMyProfile()
        resetViewModelValue()
        initDialog()
        parseMathSubjectJson()
        setObserver()
        setToolBar()
        setImageRecyclerView()
        setDesiredTimeRecyclerView()
        setSubmitButton()
        setFields()
    }

    private fun resetViewModelValue() {
        viewModel.resetInputs()
    }

    private fun initDialog() {
        initSchoolLevelDialog()
    }

    private fun initSchoolLevelDialog() {
        dialogSchoolLevel = DialogSchoolLevel {
            viewModel._school.value = it
            initSchoolSubjectDialog(it)
            if (it == "모르겠어요") {
                viewModel._subject.postValue(" ")
                binding.containerSubject.visibility = View.GONE
            } else {
                dialogSchoolSubject.show(parentFragmentManager, "dialogSchoolSubject")
                binding.containerSubject.visibility = View.VISIBLE
            }
        }
    }

    private fun initSchoolSubjectDialog(schoolLevel: String) {
        dialogSchoolSubject = DialogSchoolSubject(schoolLevel) {
            viewModel._subject.value = it
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
        timeSelectAdapter = TimeSelectAdapter(
            onAddClick = {
                val items = viewModel.hopeTutoringTime.value ?: return@TimeSelectAdapter
                if (items.size < 3) {
                    val picker = TimePickerBottomDialog { time ->
                        viewModel.addHopeTutoringTime(time) {
                            Util.createToast(requireActivity(), "이미 추가된 시간입니다")
                                .show()
                        }
                    }.apply {
                        setTitle("희망 수업 시간을 선택해주세요")
                    }
                    picker.show(parentFragmentManager, "timePicker")
                } else {
                    Util.createToast(
                        requireActivity(),
                        "희망 수업 시간은 3개 이내로 설정할 수 있습니다"
                    ).show()
                }
            },
            onViewClick = {
                viewModel.removeHopeTutoringTime(it)
            }
        )

        binding.rvDesiredTime.apply {
            adapter = timeSelectAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }


    }

    private fun setAnswerNowToggleBtn() {
        val times = viewModel.hopeTutoringTime.value ?: return
        binding.toggleAnswerNow.setOnCheckedChangeListener { btn, checked ->
            if (checked) {
                if (times.size >= 3) {
                    Util.createToast(
                        requireActivity(),
                        "희망 수업 시간은 3개 이내로 설정할 수 있습니다"
                    ).show()
                    btn.isChecked = false
                } else {
                    viewModel.addHopeTutoringTime(nowTime) {
                        Util.createToast(requireActivity(), "이미 추가된 시간입니다")
                            .show()
                    }
                }
            } else {
                viewModel.removeHopeTutoringTime(nowTime)
            }
        }
    }

    private fun observeImages() {
        viewModel.images.observe(viewLifecycleOwner) {
            imageAdapter.setItem(it!!)
            CoroutineScope(Dispatchers.IO).launch {
                viewModel._imagesBase64.postValue(it.map { it.toBase64() })
            }
        }
    }


    private fun observeSchoolLevel() {
        viewModel.school.observe(viewLifecycleOwner) {
            binding.tvSchoolSelected.text = it
        }
    }

    private fun observeSubject() {
        viewModel.subject.observe(viewLifecycleOwner) {
            binding.tvSubjectSelected.text = it
        }
    }


    private fun observeQuestionUploadState() {
        viewModel.questionUploadState.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Loading -> {
                    binding.btnSubmit.isEnabled = false
                    binding.btnSubmit.setBackgroundResource(R.drawable.bg_radius_5_background_light_blue)
                    binding.tvSubmit.setTextColor(resources.getColor(R.color.primary_blue, null))
                    binding.cbCoin.visibility = View.VISIBLE
                    loadingDialog = LoadingDialog(requireContext())
                    loadingDialog.show()
                }

                is UIState.Success -> {
                    STTFirebaseAnalytics.logEvent(EVENT.NUM_QUESTION_NORMAL, 1L)
                    loadingDialog.dismiss()
                    val intent = Intent()
                    intent.putExtra(QUESTION_UPLOAD_RESULT, it.data.questionId)
                    activity?.setResult(Activity.RESULT_OK, intent)
                    activity?.finish()
                }

                else -> {
                    loadingDialog.dismiss()
                    binding.btnSubmit.setBackgroundResource(R.drawable.bg_radius_5_grad_blue)
                    binding.tvSubmit.setTextColor(resources.getColor(R.color.white, null))
                    binding.cbCoin.visibility = View.VISIBLE
                    SimpleAlertDialog().apply {
                        title = "질문 등록에 실패했습니다"
                        description = "잠시 후 다시 시도해주세요"
                    }.show(parentFragmentManager, "question upload fail")
                }
            }
        }
    }

    private fun observeHopeTutoringTime() {
        viewModel.hopeTutoringTime.observe(viewLifecycleOwner) { mTimes ->
            val adapter = timeSelectAdapter ?: return@observe
            val times = mTimes ?: return@observe
            adapter.items = times.map { time ->
                TimePickerBottomDialog {}.SpecificTime(time.hour, time.minute)
            }.toMutableList()
            adapter.notifyDataSetChanged()

            if (!times.contains(nowTime)) binding.toggleAnswerNow.isChecked = false
        }
    }

    private fun observeInputProper() {
        viewModel.inputProper.observe(viewLifecycleOwner) { proper ->
            submitBtnEnabled = proper
            if (proper) {
                binding.btnSubmit.setBackgroundResource(R.drawable.bg_radius_5_grad_blue)
                binding.tvSubmit.setTextColor(resources.getColor(R.color.white, null))
                binding.cbCoin.visibility = View.VISIBLE
            } else {
                binding.btnSubmit.setBackgroundResource(R.drawable.bg_radius_5_grey)
                binding.tvSubmit.setTextColor(resources.getColor(R.color.sub_text_grey, null))
                binding.cbCoin.visibility = View.INVISIBLE
            }
        }
    }

    private fun observeCoinFreeReceiveState() {
        coinViewModel.coinFreeReceiveState.observe(viewLifecycleOwner) {
            it ?: return@observe

            // 무료 코인 받기 성공
            if (it) {
                SimpleAlertDialog().apply {
                    title = "오늘의 코인을 받았습니다"
                    description =
                        "현재 ${(myProfileViewModel.amount.value!! + 2) * 100}개의 코인을 보유하고 있어요"
                }.show(parentFragmentManager, "receive free coin success")
                // 코인을 업데이트하기 위해 getMyProfile() 호출
                myProfileViewModel.getMyProfile()
            } else {
                SimpleAlertDialog().apply {
                    title = "이미 오늘의 코인을 받았습니다"
                    description = "기본 코인은 매일 200개씩 제공돼요"
                }.show(parentFragmentManager, "receive free coin fail")
            }
            coinViewModel.resetCoinFreeReceiveState()
        }
    }

    private fun setObserver() {
        observeImages()
        observeSchoolLevel()
        observeSubject()
        observeQuestionUploadState()
        observeHopeTutoringTime()
        observeInputProper()
        observeCoinFreeReceiveState()
    }

    private fun navigateToCamera() {
        findNavController().navigate(R.id.action_questionNormalFormFragment_to_questionCameraFragment)
    }

    /**
     * 제출 버튼을 클릭하면 과외 요청을 보낸다.
     */
    private fun setSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            if (!submitBtnEnabled) {
                alertEmptyField()
                return@setOnClickListener
            }

            if (hasErrorGettingProfile()) {
                return@setOnClickListener
            }

            if (myProfileViewModel.amount.value!! * 100 < QUESTION_UPLOAD_COST) {
                DetailAlertDialog(
                    title = "코인이 부족합니다",
                    description = "매일 200코인이 기본 제공돼요.\n오늘의 코인을 받을까요?",
                    confirm = "코인 받기",
                    onConfirm = { coinViewModel.receiveCoinFree() }
                ).show(parentFragmentManager, "coin is insufficient")
                return@setOnClickListener
            }

            val questionUploadVO = QuestionUploadVO(
                images = viewModel.imagesBase64.value!!,
                description = viewModel.description.value!!,
                schoolLevel = viewModel.school.value!!,
                schoolSubject = viewModel.subject.value!!,
                hopeImmediate = binding.toggleAnswerNow.isChecked,
                hopeTutoringTime = viewModel.hopeTutoringTime.value!!.map { it.toLocalDateTime() },
                mainImageIndex = 0
            )
            viewModel.uploadQuestion(questionUploadVO)
        }
    }

    private fun alertEmptyField() {
        with(binding) {
            if (viewModel.images.value.isNullOrEmpty()) {
                Util.createToast(requireActivity(), "사진을 등록해주세요").show()
            } else if (etQuestionDesc.text.isNullOrEmpty()) {
                Util.createToast(requireActivity(), "질문 내용을 입력해주세요").show()
            } else if (tvSchoolSelected.text.isNullOrEmpty()) {
                Util.createToast(requireActivity(), "학교를 선택해주세요").show()
            } else if (tvSubjectSelected.text.isNullOrEmpty()) {
                Util.createToast(requireActivity(), "과목을 선택해주세요").show()
            } else if (!toggleAnswerNow.isChecked || timeSelectAdapter?.items.isNullOrEmpty()) {
                Util.createToast(requireActivity(), "희망 답변 시간을 선택해주세요")
                    .show()
            } else {
                Util.createToast(requireActivity(), "모든 항목을 입력해주세요").show()
            }
        }
    }

    private fun hasErrorGettingProfile(): Boolean {
        val value = myProfileViewModel.amount.value
        if (value != null) return false
        Util.createToast(
            requireActivity(),
            "보유한 코인을 가져오는데 실패했습니다.\n잠시 후 다시 시도해주세요."
        ).show()
        myProfileViewModel.getMyProfile()
        return true
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
        private const val QUESTION_UPLOAD_COST = 100
    }
}