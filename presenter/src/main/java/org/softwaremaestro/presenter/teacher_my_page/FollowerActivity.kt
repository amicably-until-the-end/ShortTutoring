package org.softwaremaestro.presenter.teacher_my_page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.follower_get.entity.FollowerGetResponseVO
import org.softwaremaestro.presenter.databinding.ActivityFollowerBinding
import org.softwaremaestro.presenter.teacher_my_page.adapter.StudentAdapter

@AndroidEntryPoint
class FollowerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFollowerBinding

    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToolbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setStudentRecyclerView()

        // mock up
        addItemToStudentAdapter()
    }

    private fun setStudentRecyclerView() {

        studentAdapter = StudentAdapter()

        binding.rvFollowers.apply {
            adapter = studentAdapter
            layoutManager =
                LinearLayoutManager(this@FollowerActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun addItemToStudentAdapter() {

        studentAdapter.setItem(
            mutableListOf<FollowerGetResponseVO>().apply {
                repeat(5) {
                    add(
                        FollowerGetResponseVO(
                            "",
                            "예비성대생",
                            "고등학교 2학년",
                            null,
                            "student",
                            "고등학교",
                            2,
//                            "23.08.20"
                            -1,
                            -1
                        )
                    )
                }
            }
        )

        studentAdapter.notifyDataSetChanged()
    }
}