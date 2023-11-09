package org.softwaremaestro.presenter.my_page.teacher_my_page

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.presenter.databinding.ActivityFollowerBinding
import org.softwaremaestro.presenter.my_page.adapter.StudentAdapter
import org.softwaremaestro.presenter.my_page.viewmodel.FollowerViewModel
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.Util.getWidth
import org.softwaremaestro.presenter.util.Util.logError
import org.softwaremaestro.presenter.util.Util.toPx

@AndroidEntryPoint
class FollowerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFollowerBinding
    private val followerViewModel: FollowerViewModel by viewModels()
    private lateinit var followersAdapter: StudentAdapter
    private var isSmallSizeScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowerBinding.inflate(layoutInflater)
        supportSmallScreenSize()
        setContentView(binding.root)

        setFollowersRecyclerView()
        observeFollower()
        getRemoteData()
        setToolbar()
    }

    private fun supportSmallScreenSize() {
        val width = getWidth(this)
        isSmallSizeScreen = width < 600
        if (isSmallSizeScreen) {
            val paddingValue = toPx(20, this)
            binding.tvFollower.setPadding(paddingValue, 0, paddingValue, 0)
            binding.rvFollowers.setPadding(paddingValue, 0, paddingValue, 0)
            (binding.tvFollowerEmpty.layoutParams as LinearLayout.LayoutParams).apply {
                leftMargin = paddingValue
                rightMargin = paddingValue
            }
            (binding.containerFollower.layoutParams as LinearLayout.LayoutParams).topMargin =
                toPx(15, this)
            (binding.rvFollowers.layoutParams as FrameLayout.LayoutParams).topMargin =
                toPx(15, this)
        }
    }

    private fun setToolbar() {
        binding.btnToolbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setFollowersRecyclerView() {

        followersAdapter = StudentAdapter()

        binding.rvFollowers.apply {
            adapter = followersAdapter
            layoutManager =
                LinearLayoutManager(this@FollowerActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observeFollower() {
        followerViewModel.follower.observe(this) {
            it?.let {
                if (it.isNotEmpty()) {
                    binding.tvFollower.text = "${it.size}명의 학생이 나를 찜했어요"
                    binding.rvFollowers.visibility = View.VISIBLE
                    binding.containerFollowerEmpty.visibility = View.GONE
                } else {
                    binding.tvFollower.text = "아직 나를 찜한 학생이 없어요"
                    binding.rvFollowers.visibility = View.GONE
                    binding.containerFollowerEmpty.visibility = View.VISIBLE
                }
                followersAdapter.setItem(it)
                followersAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun getRemoteData() {
        if (SocketManager.userId != null) {
            followerViewModel.getFollower(SocketManager.userId!!)
        } else {
            Util.createToast(this, "사용자의 아이디를 가져오는데 실패했습니다").show()
            logError(this::class.java, "userId is null")
        }
    }
}