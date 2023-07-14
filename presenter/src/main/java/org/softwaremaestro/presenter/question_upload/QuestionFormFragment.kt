package org.softwaremaestro.presenter.question_upload

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionFormBinding
import java.io.ByteArrayOutputStream
import java.io.File


@AndroidEntryPoint
class QuestionFormFragment : Fragment() {

    lateinit var binding: FragmentQuestionFormBinding

    private val viewModel: QuestionUploadViewModel by viewModels()

    private var mathSubjects = HashMap<String, HashMap<String, HashMap<String, Int>>>()

    var pictureBitmap: Bitmap? = null

    var schoolSelected: String = "고등학교"
    var subjectSelected: String? = null
    var chapterSelected: String? = null
    var difficultySelected: String = "normal"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseMathSubjectJson()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentQuestionFormBinding.inflate(inflater, container, false)
        setButtons()
        setRadioGroups()
        setSubjectSpinner()
        setObserver()
        setSpinnerListener()
        setPicture(QuestionFormFragmentArgs.fromBundle(requireArguments()).image)
        return binding.root
    }

    private fun setPicture(image: Bitmap) {
        binding.ivPhoto.setImageBitmap(image)
        pictureBitmap = image
    }

    private fun setObserver() {
        viewModel.questionId.observe(viewLifecycleOwner) {
            if (viewModel.questionId.value != null) {
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
                QuestionUploadVO(
                    "test-student-id",
                    binding.etDetail.text.toString(),
                    "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=",
                    schoolSelected,
                    subjectSelected ?: "",
                    chapterSelected ?: "",
                    difficultySelected
                )
            )
        }
    }

    private fun setSubjectSpinner() {

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

    private fun setRadioGroups() {
        binding.atSchoolLevel.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, pos, id ->
                schoolSelected =
                    when (pos) {
                        0 -> "초등학교"
                        1 -> "중학교"
                        else -> "고등학교"
                    }
            }

        setSubjectSpinner()

        binding.rgDifficulty.setOnCheckedChangeListener { _, checkId ->
            difficultySelected =
                when (checkId) {
                    R.id.rb_easy -> "easy"
                    R.id.rb_middle -> "normal"
                    else -> "hard"
                }
        }
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

                override fun onNothingSelected(p0: AdapterView<*>?) { return }

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

                override fun onNothingSelected(p0: AdapterView<*>?) { return }
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