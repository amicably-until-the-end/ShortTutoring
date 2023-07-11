package org.softwaremaestro.presenter.question_upload

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.question_upload.entity.QuestionUploadVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionFormBinding


@AndroidEntryPoint
class QuestionFormFragment : Fragment() {

    lateinit var binding: FragmentQuestionFormBinding

    private val viewModel: QuestionUploadViewModel by viewModels()

    private var mathSubjects = HashMap<String, HashMap<String, HashMap<String, Int>>>()

    var schoolSelected: String = "고등학교"
    var subjectSelected: String = ""
    var chapterCodeSelected: Int = 0
    var chapterSelected: String = ""
    var difficultySelected: String = "normal"

    private fun parseMathSubjectJson() {
        val assestManager = resources.assets
        try {
            val inputStream = assestManager.open("mathSubjectLabels.json")
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
        return binding.root
    }

    private fun setObserver() {
        viewModel.uploadResult.observe(viewLifecycleOwner) {
            Toast.makeText(context, viewModel.uploadResult.value, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setButtons() {
        binding.btnSubmit.setOnClickListener {
            viewModel.uploadQuestion(
                QuestionUploadVO(
                    "testID",
                    binding.etDetail.text.toString(),
                    "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNk+A8AAQUBAScY42YAAAAASUVORK5CYII=",
                    schoolSelected,
                    subjectSelected,
                    chapterSelected,
                    difficultySelected
                )
            )
        }
    }

    private fun setSubjectSpinner() {


        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mathSubjects[schoolSelected]?.keys?.toList()!!
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSubject.adapter = adapter
    }

    private fun setChapterSpinner() {

        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mathSubjects[schoolSelected]?.get(subjectSelected)?.keys?.toList()!!
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerChapter.adapter = adapter
    }

    private fun setRadioGroups() {
        binding.rgSchool.setOnCheckedChangeListener { _, checkId ->
            when (checkId) {
                R.id.rb_middleschool -> schoolSelected = "중학교"


                R.id.rb_highschool -> schoolSelected = "고등학교"

            }
            setSubjectSpinner()
        }
        binding.rgDifficulty.setOnCheckedChangeListener { _, checkId ->
            when (checkId) {
                R.id.rb_easy -> difficultySelected = "easy"

                R.id.rb_normal -> difficultySelected = "normal"


                R.id.rb_hard -> difficultySelected = "hard"

            }
        }
    }


    private fun setSpinnerListener() {
        binding.spinnerSubject.onItemSelectedListener =
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
        binding.spinnerChapter.onItemSelectedListener =
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
    }

}