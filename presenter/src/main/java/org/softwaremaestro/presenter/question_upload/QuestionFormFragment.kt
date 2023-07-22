package org.softwaremaestro.presenter.question_upload

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
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

    private var mathSubjects = HashMap<String, HashMap<String, HashMap<String, Int>>>()

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

    private val selectedValues = listOf(
        INPUT_DESCRIPTION, INPUT_SCHOOL_LEVEL, INPUT_SUBJECT, INPUT_DIFFICULTY
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

        parseMathSubjectJson()
        setButtons()
        setPickerButtons()
        setRadioGroups()
        //setSubjectSpinner()
        //setSpinnerListener()
        setObserver()

        try {
            setPicture(QuestionFormFragmentArgs.fromBundle(requireArguments()).image)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 뷰를 클릭하면 값을 입력하는 페이지로 이동한다
        setOnClickListener()

        // 선생님이 입력한 값을 뷰에 표시한다
        setSelectedValues()

        // 선생님이 입력한 값을 저장한다
        saveSelectedValues()

        (requireActivity() as QuestionUploadActivity).image?.let {
            binding.ivPhoto.setImageBitmap(it)
        }

        // 입력받지 않은 에딧텍스트가 있으면 버튼을 활성화하지 않는다
        isAllValuesEntered().let {
            binding.btnSubmit.setEnabledAndChangeColor(it)
        }
    }

    private fun setOnClickListener() {
        ets.zip(actions).forEach { pair ->
            pair.first.setOnClickListener {
                findNavController().navigate(pair.second)
            }
        }
    }

    private fun setSelectedValues() {
        selectedValues.zip(ets).forEach { pair ->
            arguments?.getString(pair.first)?.let {
                pair.second.setText(it)
            }
        }
    }

    private fun saveSelectedValues() {
        with(requireActivity() as QuestionUploadActivity) {
            arguments?.getString(INPUT_DESCRIPTION)?.let { description = it }
            arguments?.getString(INPUT_SCHOOL_LEVEL)?.let { schoolLevelSelected = it }
            arguments?.getString(INPUT_SUBJECT)?.let { subjectSelected = it }
            arguments?.getString(INPUT_DIFFICULTY)?.let { difficultySelected = it }
        }
    }

    private fun isAllValuesEntered() = ets.map { it.text.isNullOrEmpty() }.contains(true).toggle()

    private fun Boolean.toggle() = !this

    private fun setPicture(image: Bitmap) {
        binding.ivPhoto.setImageBitmap(image)
        (requireActivity() as QuestionUploadActivity).image = image
    }

    private fun setObserver() {
        viewModel.questionId.observe(viewLifecycleOwner) {
            if (!viewModel.questionId.value.isNullOrEmpty()) {
                Log.d("mymymy", "${viewModel.questionId.value} <= in observer")
                val bundle = bundleOf("questionId" to viewModel.questionId.value)
                findNavController().navigate(
                    R.id.action_questionFormFragment_to_teacherSelectFragment,
                    bundle
                )
            }

        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun setButtons() {
        binding.btnSubmit.setOnClickListener {
            viewModel.uploadQuestion(
                with(requireActivity() as QuestionUploadActivity) {
                    QuestionUploadVO(
                        STUDENT_ID,
                        description!!,
                        "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=",
                        schoolLevelSelected!!,
                        subjectSelected!!,
                        "",
                        difficultySelected!!
                    )
                }
            )
        }
    }

    private fun setPickerButtons() {

//        binding.btnSubject.setOnClickListener {
//            val subjectArray = mathSubjects[schoolSelected]?.keys?.toTypedArray()!!
//            val builder = AlertDialog.Builder(context)
//            builder.setTitle("과목을 선택해 주세요")
//                .setItems(subjectArray) { dialog, which ->
//                    subjectSelected = subjectArray[which]
//                    binding.btnSubject.text = subjectSelected
//                }
//            builder.show()
//        }
    }

    private fun clearChapter() {
//        binding.btnChapter.text = "과목을 선택해 주세요"
//        chapterSelected = null
    }

    private fun clearSubject() {
//        binding.btnSubject.text = "단원을 선택해 주세요"
//        subjectSelected = null
//        chapterSelected = null
    }

    private fun setRadioGroups() {
//        binding.rgSchoolLevel.setOnCheckedChangeListener { _, checkId ->
//            schoolSelected =
//                when (checkId) {
//                    R.id.rb_middle_school -> "중학교"
//                    R.id.rb_high_school -> "고등학교"
//                    else -> "고등학교"
//                }
//            clearSubject()
//            clearChapter()
//        }
//        binding.rgDifficulty.setOnCheckedChangeListener { _, checkId ->
//            difficultySelected =
//                when (checkId) {
//                    R.id.rb_easy -> "easy"
//                    R.id.rb_middle -> "normal"
//                    else -> "hard"
//                }
//        }
    }

    /* private fun setSubjectSpinner() {

         val adapter = ArrayAdapter(
             requireContext(),
             android.R.layout.simple_spinner_item,
             mathSubjects[schoolSelected]?.keys?.toList()!!
         )
         binding.atSubject.setAdapter(adapter)
     }

     private fun setChapterSpinner() {

         val adapter = ArrayAdapter<String>(
             requireContext(),
             android.R.layout.simple_spinner_item,
             mathSubjects[schoolSelected]?.get(subjectSelected)?.keys?.toList()!!
         )
         binding.atChapter.setAdapter(adapter)
     }




     private fun setSpinnerListener() {
         binding.atSubject.onItemSelectedListener =
             object : AdapterView.OnItemSelectedListener {
                 override fun onItemSelected(
                     parent: AdapterView<*>?,
                     view: View?,
                     pos: Int,
                     id: Long
                 ) {
                     val selectedItem = parent?.getItemAtPosition(pos).toString()!!
                     Log.d("mymy", selectedItem)
                     subjectSelected = selectedItem
                     setChapterSpinner()
                 }

                 override fun onNothingSelected(p0: AdapterView<*>?) {
                     return
                 }

             }
         binding.atChapter.onItemSelectedListener =
             object : AdapterView.OnItemSelectedListener {
                 override fun onItemSelected(
                     parent: AdapterView<*>?,
                     view: View?,
                     pos: Int,
                     id: Long
                 ) {
                     val selectedItem = parent?.getItemAtPosition(pos).toString()!!
                     chapterSelected = selectedItem
                 }

                 override fun onNothingSelected(p0: AdapterView<*>?) {
                     return
                 }
             }
     }*/

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