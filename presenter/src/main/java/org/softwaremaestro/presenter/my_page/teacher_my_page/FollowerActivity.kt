package org.softwaremaestro.presenter.my_page.teacher_my_page

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.socket.SocketManager
import org.softwaremaestro.presenter.databinding.ActivityFollowerBinding
import org.softwaremaestro.presenter.my_page.adapter.StudentAdapter
import org.softwaremaestro.presenter.my_page.viewmodel.FollowerViewModel
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.Util.logError

@AndroidEntryPoint
class FollowerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFollowerBinding
    private val followerViewModel: FollowerViewModel by viewModels()
    private lateinit var followersAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setFollowersRecyclerView()
        observeFollower()
        getRemoteData()
        setToolbar()
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
                binding.tvFollower.text =
                    if (it.isNotEmpty()) {
                        "${it.size}명의 학생이 나를 찜했어요"
                    } else {
                        "아직 나를 찜한 학생이 없어요"
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