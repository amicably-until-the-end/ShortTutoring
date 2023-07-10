package org.softwaremaestro.presenter.teacher_home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.model.vo.QuestionVO
import org.softwaremaestro.presenter.databinding.FragmentTeacherHomeBinding

class TeacherHomeFragment : Fragment() {

    private lateinit var binding: FragmentTeacherHomeBinding
    private lateinit var dialog: WaitingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTeacherHomeBinding.inflate(inflater, container, false)
        dialog = WaitingDialog(requireActivity())

        // 질문 리싸이클러뷰를 설정한다
        setQuestionRecyclerView()

        return binding.root
    }

    private fun setQuestionRecyclerView() {
        val items = mutableListOf(
            QuestionVO(byteArrayOf(1, 2, 3), 1, 2, 3, listOf(4,5,6)),
            QuestionVO(byteArrayOf(1, 2, 3), 1, 2, 3, listOf(4,5,6)),
            QuestionVO(byteArrayOf(1, 2, 3), 1, 2, 3, listOf(4,5,6)),
        )

        binding.rvQuestion.apply {
            adapter = QuestionAdapter(items, object: OnItemClickListener {
                override fun onItemClick() {
                    dialog.show()

                    startRepeatingJob(1000L)
                }
            })
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun startRepeatingJob(timeInterval: Long): Job {

        return viewLifecycleOwner.lifecycleScope.launch {
            while (NonCancellable.isActive) {
                waitUntilPick()
                delay(timeInterval)
            }
        }
    }

    private fun waitUntilPick() {


    }
}
