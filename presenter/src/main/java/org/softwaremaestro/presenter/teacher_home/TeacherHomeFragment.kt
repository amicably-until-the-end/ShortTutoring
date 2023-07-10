package org.softwaremaestro.presenter.teacher_home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.question_get.entity.QuestionGetResultVO
import org.softwaremaestro.presenter.databinding.FragmentTeacherHomeBinding

@AndroidEntryPoint
class TeacherHomeFragment : Fragment() {

    private lateinit var binding: FragmentTeacherHomeBinding
    private lateinit var dialog: WaitingDialog
    private val viewModel : TeacherHomeViewModel by viewModels()
    private lateinit var questionAdapter: QuestionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTeacherHomeBinding.inflate(inflater, container, false)
        dialog = WaitingDialog(requireActivity())
        questionAdapter = QuestionAdapter(object: OnItemClickListener {
            override fun onItemClick() {
                dialog.show()
            }
        })

        binding.rvQuestion.apply {
            adapter = questionAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

//        questionAdapter.setItem(listOf(
//            QuestionGetResultVO(
//                "",
//                "고등학교",
//                "수학1",
//                "지수함수",
//                "어려움",
//                "이해를 못하겠어요",
//                listOf("#이해가 빨라요", "#기초가 탄탄해요", ""),
//                listOf("teacher_id"),
//                "student_id"
//            )
//        ))

        viewModel.questions.observe(viewLifecycleOwner) {
            questionAdapter.setItem(it)
            questionAdapter.notifyDataSetChanged()
        }

        val job = startRepeatingJob(1000L)


        return binding.root
    }

    private fun startRepeatingJob(timeInterval: Long): Job {
        return viewLifecycleOwner.lifecycleScope.launch {
            while (NonCancellable.isActive) {
                viewModel.getQuestions()
                waitUntilPick()
                delay(timeInterval)
            }
        }
    }

    private fun waitUntilPick() {


    }
}
