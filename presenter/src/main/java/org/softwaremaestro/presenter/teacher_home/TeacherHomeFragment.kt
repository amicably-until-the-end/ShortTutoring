package org.softwaremaestro.presenter.teacher_home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadVO
import org.softwaremaestro.domain.answer_upload.entity.TeacherVO
import org.softwaremaestro.presenter.databinding.FragmentTeacherHomeBinding
import org.softwaremaestro.presenter.teacher_home.viewmodel.AnswerViewModel
import org.softwaremaestro.presenter.teacher_home.viewmodel.QuestionsViewModel

@AndroidEntryPoint
class TeacherHomeFragment : Fragment() {

    private lateinit var binding: FragmentTeacherHomeBinding
    private val questionsViewModel : QuestionsViewModel by viewModels()
    private val answerViewModel : AnswerViewModel by viewModels()
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var jobGetQuestions: Job
    private lateinit var watingDialog: WaitingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTeacherHomeBinding.inflate(layoutInflater)
        questionAdapter = QuestionAdapter(object: OnItemClickListener {
            override fun onItemClick() {
                watingDialog.show()
                uploadAnswer()
            }
        })
        watingDialog = WaitingDialog(requireActivity())
        jobGetQuestions = getJobGetQuestions(1000L).apply { start() }

        binding.rvQuestion.apply {
            adapter = questionAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        observeQuestions()

        observeAnswer()



        return binding.root
    }

    private fun observeQuestions() {
        questionsViewModel.questions.observe(viewLifecycleOwner) {
            questionAdapter.setItem(it)
            questionAdapter.notifyDataSetChanged()
        }
    }

    private fun observeAnswer() {
        answerViewModel.answer.observe(viewLifecycleOwner) {
            Log.d("answer", it.exampleData)
        }
    }

    private fun getJobGetQuestions(timeInterval: Long): Job {
        return viewLifecycleOwner.lifecycleScope.launch(start = CoroutineStart.LAZY) {
            while (NonCancellable.isActive) {
                if (!watingDialog.isShowing) {
                    questionsViewModel.getQuestions()
                }
                delay(timeInterval)
            }
        }
    }

    private fun uploadAnswer() {
//        val problemId = "this should be properly set, or error occurs"
//        answerViewModel.uploadAnswer(AnswerUploadVO(problemId, TeacherVO("teacherId")))
    }
}
