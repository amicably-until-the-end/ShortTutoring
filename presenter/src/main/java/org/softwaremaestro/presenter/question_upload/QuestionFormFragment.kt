package org.softwaremaestro.presenter.question_upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionFormBinding
import org.softwaremaestro.presenter.question_upload.viewmodel.QuestionUploadViewModel
import org.softwaremaestro.presenter.setEnabledAndChangeColor


@AndroidEntryPoint
class QuestionFormFragment : Fragment() {

    lateinit var binding: FragmentQuestionFormBinding
    private val viewModel: QuestionUploadViewModel by activityViewModels()

//    private var mathSubjects = HashMap<String, HashMap<String, HashMap<String, Int>>>()

    private val ets: List<TextInputEditText> by lazy {
        with(binding) {
            listOf(etDescription, etSchoolLevel, etSubject, etDifficulty)
        }
    }


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

//        parseMathSubjectJson()
        setObserver()

        setToolBar()

        // 모든 내용이 입력되었으면 제출 버튼을 활성화한다
        checkAndEnableSubjectBtn()

        // 제출 버튼을 클릭하면 과외 요청을 보낸다.
        setSubmitButton()

        // 뷰를 클릭하면 해당 뷰에 값을 입력하는 페이지로 이동한다
        setFieldButtons()
    }

    private fun checkAndEnableSubjectBtn() {
        isAllValuesEntered().let {
            binding.btnSubmit.setEnabledAndChangeColor(it)
        }
    }

    private fun setFieldButtons() {

        binding.ivImage.setOnClickListener {
            findNavController().navigate(R.id.action_questionFormFragment_to_questionCameraFragment)
        }

        binding.etSchoolLevel.setOnClickListener {
            findNavController().navigate(R.id.action_questionFormFragment_to_questionFormSchoolLevelFragment)
        }
        binding.etDescription.setOnClickListener {
            findNavController().navigate(R.id.action_questionFormFragment_to_questionFormDescriptionFragment)
        }
        binding.etSubject.setOnClickListener {
            findNavController().navigate(R.id.action_questionFormFragment_to_questionFormSubjectFragment)
        }
        binding.etDifficulty.setOnClickListener {
            findNavController().navigate(R.id.action_questionFormFragment_to_questionFormDifficultyFragment)
        }

    }


    private fun isAllValuesEntered(): Boolean {
        return (
                viewModel.image.value != null &&
                        viewModel.description.value != null &&
                        viewModel.school.value != null &&
                        viewModel.subject.value != null &&
                        viewModel.difficulty.value != null)
    }

    private fun Boolean.toggle() = !this


    private fun observeImage() {
        viewModel.image.observe(viewLifecycleOwner) {
            binding.ivImage.setImageBitmap(it)
            checkAndEnableSubjectBtn()
        }
    }

    private fun observeDescription() {
        viewModel.description.observe(viewLifecycleOwner) {
            binding.etDescription.setText(it)
            checkAndEnableSubjectBtn()
        }
    }

    private fun observeSchoolLevel() {
        viewModel.school.observe(viewLifecycleOwner) {
            binding.etSchoolLevel.setText(it)
            checkAndEnableSubjectBtn()
        }
    }

    private fun observeSubject() {
        viewModel.subject.observe(viewLifecycleOwner) {
            binding.etSubject.setText(it)
            checkAndEnableSubjectBtn()
        }
    }

    private fun observeDifficulty() {
        viewModel.difficulty.observe(viewLifecycleOwner) {
            binding.etDifficulty.setText(it)
            checkAndEnableSubjectBtn()
        }
    }

    private fun observeQuestionId() {
        viewModel.questionId.observe(viewLifecycleOwner) {
            if (!viewModel.questionId.value.isNullOrEmpty()) {
                val bundle = bundleOf("questionId" to viewModel.questionId.value)
                findNavController().navigate(
                    R.id.action_questionFormFragment_to_teacherSelectFragment,
                    bundle
                )
            } else {
                binding.btnSubmit.isEnabled = true
                Toast.makeText(requireContext(), "질문 등록에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        checkAndEnableSubjectBtn()
    }


    private fun setObserver() {
        observeImage()
        observeDescription()
        observeSchoolLevel()
        observeSubject()
        observeDifficulty()
        observeQuestionId()
    }


    private fun setSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            //버튼 여러번 눌러지는 거 방지
            binding.btnSubmit.isEnabled = false

            viewModel.uploadQuestion()

        }


    }

    private fun setToolBar() {
        binding.btnToolbarBack.setOnClickListener {
            activity?.finish()
        }
    }

//    private fun parseMathSubjectJson() {
//        val assetsManager = resources.assets
//        try {
//            val inputStream = assetsManager.open("mathSubjectLabels.json")
//            val reader = inputStream.bufferedReader()
//            val gson = Gson()
//            // Define the type of the outermost structure
//            val type =
//                object : TypeToken<HashMap<String, HashMap<String, HashMap<String, Int>>>>() {}.type
//            // Parse the JSON string and populate mathSubjects
//            mathSubjects = gson.fromJson(reader, type)
//        } catch (e: Exception) {
//            return
//        }
//    }
}