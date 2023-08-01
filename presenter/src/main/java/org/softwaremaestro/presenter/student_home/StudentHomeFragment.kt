package org.softwaremaestro.presenter.student_home

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.Util.dpToPx
import org.softwaremaestro.presenter.databinding.FragmentStudentHomeBinding
import org.softwaremaestro.presenter.question_upload.QuestionUploadActivity
import org.softwaremaestro.presenter.student_home.adapter.BestTeacherAdapter
import org.softwaremaestro.presenter.student_home.adapter.LectureAdapter
import org.softwaremaestro.presenter.student_home.adapter.MyTeacherAdapter
import org.softwaremaestro.presenter.student_home.item.BestTeacher
import org.softwaremaestro.presenter.student_home.item.Lecture
import org.softwaremaestro.presenter.student_home.item.MyTeacher

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

        setQuestionButton()
        setLectureRecyclerView()
        setMyTeacherRecyclerView()
        setBestTeacherRecyclerView()


        return binding.root
    }


    private fun setMyTeacherRecyclerView() {
        binding.rvMyTeacher.apply {
            adapter = MyTeacherAdapter {
                Log.d("message", it)
            }.apply {
                val teachers = mutableListOf<MyTeacher>().apply {
                    add(
                        MyTeacher(
                            "수학멘토",
                            "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/2048px-Circle-icons-profile.svg.png",
                            "1"
                        )
                    )
                    add(
                        MyTeacher(
                            "수학의신",
                            "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/2048px-Circle-icons-profile.svg.png",
                            "1"
                        ),
                    )
                    add(
                        MyTeacher(
                            "수학의신",
                            "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/2048px-Circle-icons-profile.svg.png",
                            "1"
                        ),
                    )
                }
                setItem(teachers)
            }
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }


    private fun setBestTeacherRecyclerView() {
        binding.rvBestTeacher.apply {
            adapter = BestTeacherAdapter {
                Log.d("message", it)
            }.apply {
                val lectures = mutableListOf<BestTeacher>().apply {
                    add(
                        BestTeacher(
                            "강해린",
                            "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/2048px-Circle-icons-profile.svg.png",
                            "1",
                            35,
                            "성균관대학교",
                        )
                    )
                    add(
                        BestTeacher(
                            "팜하니",
                            "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7e/Circle-icons-profile.svg/2048px-Circle-icons-profile.svg.png",
                            "1",
                            31,
                            "피식대학교",
                        )
                    )
                }
                setItem(lectures)
            }
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setLectureRecyclerView() {
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
    }

    private fun setQuestionButton() {
        binding.btnQuestion.setOnClickListener {
            val intent = Intent(activity, QuestionUploadActivity::class.java)
            startActivity(intent)
        }
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