package org.softwaremaestro.presenter.student_home

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.Util.dpToPx
import org.softwaremaestro.presenter.databinding.FragmentStudentHomeBinding
import org.softwaremaestro.presenter.question_upload.QuestionUploadActivity
import org.softwaremaestro.presenter.student_home.adapter.LectureAdapter

private const val GRIDLAYOUT_SPAN_COUNT = 2
private const val GRIDLAYOUT_SPICING = 8

@AndroidEntryPoint
class StudentHomeFragment : Fragment() {

    private lateinit var binding: FragmentStudentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStudentHomeBinding.inflate(layoutInflater)

        binding.btnQuestion.setOnClickListener {
            val intent = Intent(activity, QuestionUploadActivity::class.java)
            startActivity(intent)
        }

        binding.rvLecture.apply {
            adapter = LectureAdapter {
                Log.d("message", it)
            }.apply {
                val lectures = mutableListOf<Lecture>().apply {
                    add(Lecture("경우의 수를 다 셌는데 안 맞아요", "수학1"))
                    add(Lecture("이차곡선의 성질이 이해가 안 돼요", "기하"))
                }
                setItem(lectures)
            }
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }

        return binding.root
    }


    private fun RecyclerView.setSpacing(dp: Int) {
        this.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                position: Int,
                parent: RecyclerView
            ) {
                super.getItemOffsets(outRect, position, parent)
                when (position % GRIDLAYOUT_SPAN_COUNT) {
                    // 그리드 레이아웃의 맨 왼쪽 뷰
                    0 -> outRect.right = dpToPx(dp, requireContext())
                    // 그리드 레이아웃의 맨 오른쪽 뷰
                    GRIDLAYOUT_SPAN_COUNT - 1 -> outRect.left = dpToPx(dp, requireContext())
                    else -> {
                        outRect.left = dpToPx(dp, requireContext())
                        outRect.right = dpToPx(dp, requireContext())
                    }
                }
            }
        })
    }
}