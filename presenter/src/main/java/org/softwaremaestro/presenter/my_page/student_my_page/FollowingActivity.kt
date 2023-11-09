package org.softwaremaestro.presenter.my_page.student_my_page

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.presenter.databinding.ActivityFollowingBinding
import org.softwaremaestro.presenter.my_page.viewmodel.FollowingViewModel
import org.softwaremaestro.presenter.teacher_profile.viewmodel.BestTeacherViewModel
import org.softwaremaestro.presenter.teacher_search.adapter.TeacherAdapter
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.Util.toPx

@AndroidEntryPoint
class FollowingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFollowingBinding

    private val bestTeacherViewModel: BestTeacherViewModel by viewModels()
    private val followingViewModel: FollowingViewModel by viewModels()
    private lateinit var followingAdapter: TeacherAdapter
    private lateinit var bestTeacherAdapter: TeacherAdapter
    private var isSmallSizeScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowingBinding.inflate(layoutInflater)
        supportSmallScreenSize()
        setContentView(binding.root)

        setFollowingRecyclerView()
        setTeacherRecommendsRecyclerView()
        setObserver()
        getRemoteData()
        setToolbar()
    }

    private fun supportSmallScreenSize() {
        val width = Util.getWidth(this)
        isSmallSizeScreen = width < 600
        val paddingValue = toPx(20, this)
        if (isSmallSizeScreen) {
            binding.tvFollowing.setPadding(paddingValue, 0, paddingValue, 0)
            binding.rvFollowing.setPadding(paddingValue, 0, paddingValue, 0)
            (binding.tvFollowingEmpty.layoutParams as LinearLayout.LayoutParams).apply {
                leftMargin = paddingValue
                rightMargin = paddingValue
            }
        }
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
        bestTeacherAdapter = TeacherAdapter {
            // dialog 띄워야 함
        }

        binding.rvTeacherRecommend.apply {
            adapter = bestTeacherAdapter
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
        bestTeacherViewModel.bestTeachers.observe(this) { teachers ->
            teachers ?: return@observe
            bestTeacherAdapter.setItem(teachers)
            bestTeacherAdapter.notifyDataSetChanged()
        }
    }

    private fun getRemoteData() {
        if (SocketManager.userId != null) {
            followingViewModel.getFollowing(SocketManager.userId!!)
            //teacherRecommendsViewModel.getTeachers()
        } else {
            Util.createToast(this, "사용자의 아이디를 가져오는데 실패했습니다").show()
            Util.logError(this::class.java, "userId is null")
        }
    }

    private fun setToolbar() {
        binding.btnToolbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}