package org.softwaremaestro.presenter.question_upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionFormBinding


@AndroidEntryPoint
class QuestionFormFragment : Fragment() {

    lateinit var binding: FragmentQuestionFormBinding

    private val viewModel: QuestionUploadViewModel by viewModels()

    private var mathSubjects = HashMap<String, HashMap<String, List<Pair<Int, String>>>>()


    private fun parseMathSubjectJson() {
        val assestManager = resources.assets
        try {
            val inputStream = assestManager.open("mathSubjectLabels.json")
            val reader = inputStream.bufferedReader()
            val gson = Gson()
            mathSubjects = gson.fromJson(reader, mathSubjects.javaClass)
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

        /*        binding.btnSubmit.setOnClickListener {
                    viewModel.uploadQuestion(
                        QuestionUploadVO(binding.etDetail.text.toString(),binding)

                }*/
        observe()

        setSpinners()
        setRadioGroups()
        return binding.root
    }

    private fun setRadioGroups() {
        binding.rgSchool.setOnCheckedChangeListener { _, checkId ->
            when (checkId) {
                R.id.rb_middleschool -> {
                    viewModel.schoolSelected_.value = "중학교"
                }

                R.id.rb_highschool -> {
                    viewModel.schoolSelected_.value = "고등학교"
                }
            }
        }
        binding.rgDifficulty.setOnCheckedChangeListener { _, checkId ->
            when (checkId) {
                R.id.rb_easy -> viewModel.schoolSelected_.value = "easy"
                R.id.rb_normal -> viewModel.schoolSelected_.value = "normal"
                R.id.rb_hard -> viewModel.schoolSelected_.value = "hard"
            }
        }
    }

    private fun setSpinners() {
        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mathSubjects.keys.toList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerChapter.adapter = adapter
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //observe()
    }

    private fun observe() {
        /*viewModel.resultString.observe(viewLifecycleOwner, Observer {
            Log.d("mymym", viewModel.resultString.value!!);
        })*/
        viewModel.schoolSelected.observe(viewLifecycleOwner, Observer {
            val adapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                mathSubjects[viewModel.schoolSelected.value]?.keys?.toList()!!
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerChapter.adapter = adapter
        })
    }
}