package org.softwaremaestro.presenter.my_page.teacher_my_page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.ActivityFollowingBinding
import org.softwaremaestro.presenter.teacher_search.adapter.TeacherAdapter

@AndroidEntryPoint
class FollowingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFollowingBinding

    private lateinit var teacherAdapter: TeacherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToolbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setTeacherRecyclerView()
    }

    private fun setTeacherRecyclerView() {

        teacherAdapter = TeacherAdapter {
            // go to teacher profile
        }

        binding.rvFollowing.apply {
            adapter = teacherAdapter
            layoutManager =
                LinearLayoutManager(this@FollowingActivity, LinearLayoutManager.VERTICAL, false)
        }
    }
}