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
import androidx.recyclerview.widget.RecyclerView
import org.softwaremaestro.presenter.databinding.FragmentStudentHomeBinding
import org.softwaremaestro.presenter.question_upload.QuestionUploadActivity
import org.softwaremaestro.presenter.Util.dpToPx

private const val GRIDLAYOUT_SPANCOUNT = 2

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
                    (0..10).forEach {
                        add(Lecture("문제를 못 풀겠어요", "23/07/23", "수학1", "지수함수와 로그함수"))
                    }
                }
                setItem(lectures)
            }
            layoutManager = GridLayoutManager(requireActivity(), 2)
            setSpacing(10)
        }

        return binding.root
    }

    private fun RecyclerView.setSpacing(dp: Int) {
        this.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                position: Int,
                parent: RecyclerView
            ) {
                super.getItemOffsets(outRect, position, parent)
                when (position % GRIDLAYOUT_SPANCOUNT) {
                    // 그리드 레이아웃의 맨 왼쪽 뷰
                    0 -> outRect.right = dpToPx(dp, requireContext())
                    // 그리드 레이아웃의 맨 오른쪽 뷰
                    GRIDLAYOUT_SPANCOUNT - 1 -> outRect.right = dpToPx(dp, requireContext())
                    else -> {
                        outRect.left = dpToPx(dp, requireContext())
                        outRect.right = dpToPx(dp, requireContext())
                    }
                }
            }
        })
    }
}