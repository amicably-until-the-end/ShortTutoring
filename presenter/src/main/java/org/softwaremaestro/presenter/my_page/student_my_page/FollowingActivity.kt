package org.softwaremaestro.presenter.my_page.student_my_page

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.follow.entity.FollowingGetResponseVO
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.presenter.databinding.ActivityFollowingBinding
import org.softwaremaestro.presenter.my_page.viewmodel.FollowingViewModel
import org.softwaremaestro.presenter.teacher_profile.viewmodel.TeacherRecommendViewModel
import org.softwaremaestro.presenter.teacher_search.adapter.TeacherAdapter
import org.softwaremaestro.presenter.util.Util

@AndroidEntryPoint
class FollowingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFollowingBinding

    private val teacherRecommendsViewModel: TeacherRecommendViewModel by viewModels()
    private val followingViewModel: FollowingViewModel by viewModels()
    private lateinit var followingAdapter: TeacherAdapter
    private lateinit var teacherRecommendsAdapter: TeacherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFollowingRecyclerView()
        setTeacherRecommendsRecyclerView()
        setObserver()
        getRemoteData()
        setToolbar()
    }

    private fun setFollowingRecyclerView() {

        followingAdapter = TeacherAdapter {
            // dialog 띄어야 함
        }

        binding.rvFollowing.apply {
            adapter = followingAdapter
            layoutManager =
                LinearLayoutManager(this@FollowingActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setTeacherRecommendsRecyclerView() {
        teacherRecommendsAdapter = TeacherAdapter {
            // dialog 띄워야 함
        }

        binding.rvTeacherRecommend.apply {
            adapter = teacherRecommendsAdapter
            layoutManager =
                LinearLayoutManager(this@FollowingActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setObserver() {
        observeFollowing()
        observeTeacherRecommends()
    }

    private fun observeFollowing() {
        followingViewModel.following.observe(this) {
            it?.let {
                if (it.isNotEmpty()) {
                    binding.tvFollowing.text = "${it.size}명의 선생님을 찜했어요"
                    binding.rvFollowing.visibility = View.VISIBLE
                    binding.containerFollowingEmpty.visibility = View.GONE
                } else {
                    binding.tvFollowing.text = "아직 찜한 선생님이 없어요"
                    binding.rvFollowing.visibility = View.GONE
                    binding.containerFollowingEmpty.visibility = View.VISIBLE
                }
                followingAdapter.setItem(it)
                followingAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun observeTeacherRecommends() {
        teacherRecommendsViewModel.teacherRecommends.observe(this) { teachers ->
            teachers?.let { teacher ->
                teacher.map {
                    FollowingGetResponseVO(
                        id = it.teacherId,
                        name = it.nickname,
                        bio = it.bio,
                        profileImage = it.profileUrl,
                        role = "teacher",
                        schoolDivision = "",
                        schoolName = it.univ,
                        schoolDepartment = "",
                        followers = it.followers,
                        followingCount = it.followers?.size
                    )
                }.let {
                    teacherRecommendsAdapter.setItem(it)
                    teacherRecommendsAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun getRemoteData() {
        if (SocketManager.userId != null) {
            followingViewModel.getFollowing(SocketManager.userId!!)
            teacherRecommendsViewModel.getTeachers()
        } else {
            Toast.makeText(this, "사용자의 아이디를 가져오는데 실패했습니다", Toast.LENGTH_SHORT).show()
            Util.logError(this::class.java, "userId is null")
        }
    }

    private fun setToolbar() {
        binding.btnToolbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}