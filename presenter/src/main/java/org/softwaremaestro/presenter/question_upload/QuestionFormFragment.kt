package org.softwaremaestro.presenter.question_upload

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.Util.LoadingDialog
import org.softwaremaestro.presenter.Util.UIState
import org.softwaremaestro.presenter.databinding.FragmentQuestionFormBinding
import org.softwaremaestro.presenter.question_upload.viewmodel.QuestionUploadViewModel
import org.softwaremaestro.presenter.Util.setEnabledAndChangeColor
import org.softwaremaestro.presenter.question_upload.adapter.FormImageAdapter
import org.softwaremaestro.presenter.question_upload.adapter.TimeSelectAdapter


@AndroidEntryPoint
class QuestionFormFragment : Fragment() {

    private lateinit var loadingDialog: LoadingDialog

    lateinit var binding: FragmentQuestionFormBinding
    private val viewModel: QuestionUploadViewModel by activityViewModels()


    //recyclerView adapters
    private lateinit var imageAdapter: FormImageAdapter
    private lateinit var timeSelectAdapter: TimeSelectAdapter


    private var mathSubjects = HashMap<String, HashMap<String, HashMap<String, Int>>>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentQuestionFormBinding.inflate(layoutInflater)
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
        setFieldButtons()

    }

    /**
     *  모든 내용이 입력되었으면 제출 버튼을 활성화한다
     */
    private fun checkAndEnableSubjectBtn() {
        isAllValuesEntered().let {
            binding.btnSubmit.setEnabledAndChangeColor(it)
        }
    }

    /**
    뷰를 클릭하면 해당 뷰에 값을 입력하는 페이지로 이동한다
     */
    private fun setFieldButtons() {
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
                binding.tvSchoolSelected.text = selectedSchool
                showSubjectSelectDialog()
            }
            .setTitle("학교")
            .setPositiveButton("확인", null)
            .setNegativeButton("취소", null)
            .create().show()

    }

    private fun showSubjectSelectDialog() {
        var subjects =
            mathSubjects[binding.tvSchoolSelected.text.toString()]?.keys?.toTypedArray()!!

        AlertDialog.Builder(requireContext())
            .setItems(subjects) { _, which ->
                val selectedSchool = subjects[which]
                binding.tvSubjectSelected.text = selectedSchool
            }
            .setTitle("구분")
            .setPositiveButton("확인", null)
            .setNegativeButton("취소", null)
            .create().show()
    }


    private fun isAllValuesEntered(): Boolean {
        return (
                viewModel.images.value != null &&
                        viewModel.description.value != null &&
                        viewModel.school.value != null &&
                        viewModel.subject.value != null &&
                        viewModel.difficulty.value != null)
    }

    private fun Boolean.toggle() = !this


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
        var hour: Int = 0;
        var time: Int = 0;
        timeSelectAdapter = TimeSelectAdapter() {
            val picker = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    hour = hourOfDay
                    time = minute
                    timeSelectAdapter.items.add("${hourOfDay}시 ${minute}분")
                    timeSelectAdapter.notifyDataSetChanged()
                },
                hour,
                time,
                true
            )
            picker.show()
        }

        binding.rvDesiredTime.apply {
            adapter = timeSelectAdapter
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
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
                    Toast.makeText(requireContext(), "질문 등록에 성공했습니다.", Toast.LENGTH_SHORT).show()
                    val bundle = bundleOf("questionId" to it.data.questionId)
                    findNavController().navigate(
                        R.id.action_questionFormFragment_to_teacherSelectFragment,
                        bundle
                    )
                }

                else -> {
                    loadingDialog.dismiss()
                    binding.btnSubmit.setEnabledAndChangeColor(true)
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
        findNavController().navigate(R.id.action_questionFormFragment_to_questionCameraFragment)
    }

    /**
     * 제출 버튼을 클릭하면 과외 요청을 보낸다.
     */
    private fun setSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            //버튼 여러번 눌러지는 거 방지
            binding.btnSubmit.setEnabledAndChangeColor(false)

            viewModel.uploadQuestion()

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
}