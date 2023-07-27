package org.softwaremaestro.presenter.question_upload

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionFormBinding
import org.softwaremaestro.presenter.setEnabledAndChangeColor
import java.io.ByteArrayOutputStream


private const val STUDENT_ID = "test-student-id"

@AndroidEntryPoint
class QuestionFormFragment : Fragment() {

    lateinit var binding: FragmentQuestionFormBinding
    private val viewModel: QuestionUploadViewModel by viewModels()

//    private var mathSubjects = HashMap<String, HashMap<String, HashMap<String, Int>>>()

    private val selectedValues: List<String?> by lazy {
        with(requireActivity() as QuestionUploadActivity) {
            listOf(description, schoolLevelSelected, subjectSelected, difficultySelected)
        }
    }

    private val ets: List<TextInputEditText> by lazy {
        with(binding) {
            listOf(etDescription, etSchoolLevel, etSubject, etDifficulty)
        }
    }

    private val actions = listOf(
        R.id.action_questionFormFragment_to_questionFormDescriptionFragment,
        R.id.action_questionFormFragment_to_questionFormSchoolLevelFragment,
        R.id.action_questionFormFragment_to_questionFormSubjectFragment,
        R.id.action_questionFormFragment_to_questionFormDifficultyFragment
    )

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

        // 카메라로 촬영한 사진을 저장한다.
        saveImage()

        // 선생님이 입력한 값을 뷰에 표시한다
        setSelectedValues()

        // 모든 내용이 입력되었으면 제출 버튼을 활성화한다
        checkAndEnableSubjectBtn()

        // 제출 버튼을 클릭하면 과외 요청을 보낸다.
        setSubmitButton()

        // 뷰를 클릭하면 해당 뷰에 값을 입력하는 페이지로 이동한다
        setOnClickListenerToViews()
    }

    private fun checkAndEnableSubjectBtn() {
        isAllValuesEntered().let {
            binding.btnSubmit.setEnabledAndChangeColor(it)
        }
    }

    private fun saveImage() {
        try {
            QuestionFormFragmentArgs.fromBundle(requireArguments()).image.let {
                (requireActivity() as QuestionUploadActivity).image = it
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setOnClickListenerToViews() {

        binding.ivImage.setOnClickListener {
            findNavController().navigate(R.id.action_questionFormFragment_to_questionCameraFragment)
        }

        ets.zip(actions).forEach { pair ->
            pair.first.setOnClickListener {
                findNavController().navigate(pair.second)
            }
        }
    }

    private fun setSelectedValues() {

        (requireActivity() as QuestionUploadActivity).image.let {
            binding.ivImage.setImageBitmap(it)
        }

        selectedValues.zip(ets).forEach { pair ->
            pair.first.let {
                pair.second.setText(it)
            }
        }
    }

    private fun isAllValuesEntered() = ets.map { it.text.isNullOrEmpty() }.contains(true).toggle()

    private fun Boolean.toggle() = !this

    private fun setObserver() {
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
    }

    private fun Bitmap.toBase64(): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
    }

    private fun setSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            //버튼 여러번 눌러지는 거 방지
            binding.btnSubmit.isEnabled = false
            
            CoroutineScope(Dispatchers.IO).launch {
                val base64 = (requireActivity() as QuestionUploadActivity).image!!.toBase64()

                withContext(Dispatchers.Main) {
                    with(requireActivity() as QuestionUploadActivity) {
                        viewModel.uploadQuestion(
                            QuestionUploadVO(
                                STUDENT_ID,
                                base64,
                                "png",
                                description!!,
                                schoolLevelSelected!!,
                                subjectSelected!!,
                                difficultySelected!!
                            )
                        )
                    }
                }
            }

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