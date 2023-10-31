package org.softwaremaestro.presenter.video_player

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ActivityVideoPlayerBinding
import org.softwaremaestro.presenter.student_home.StudentHomeFragment.Companion.DESCRIPTION
import org.softwaremaestro.presenter.student_home.StudentHomeFragment.Companion.FOLLOWING
import org.softwaremaestro.presenter.student_home.StudentHomeFragment.Companion.PROFILE_IMAGE
import org.softwaremaestro.presenter.student_home.StudentHomeFragment.Companion.RECORDING_FILE_URL
import org.softwaremaestro.presenter.student_home.StudentHomeFragment.Companion.SCHOOL_LEVEL
import org.softwaremaestro.presenter.student_home.StudentHomeFragment.Companion.STUDENT_NAME
import org.softwaremaestro.presenter.student_home.StudentHomeFragment.Companion.SUBJECT
import org.softwaremaestro.presenter.student_home.StudentHomeFragment.Companion.TEACHER_ID
import org.softwaremaestro.presenter.teacher_profile.viewmodel.FollowUserViewModel
import org.softwaremaestro.presenter.util.widget.DetailAlertDialog
import org.softwaremaestro.presenter.util.widget.STTFirebaseAnalytics

@AndroidEntryPoint
class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding
    private var simpleExoPlayer: ExoPlayer? = null
    private var mPlayWhenReady = true
    private var mCurrentWindow = 0
    private var mPlaybackPosition = 0L

    private lateinit var profileImage: String
    private lateinit var studentName: String
    private lateinit var schoolLevel: String
    private lateinit var subject: String
    private lateinit var description: String
    private lateinit var tutoringUrl: String
    private lateinit var teacherId: String
    private var mFollowing = false

    private val followUserViewModel: FollowUserViewModel by viewModels()
    private lateinit var unfollowDialog: DetailAlertDialog
    private var startTime: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getExtraValues()
        setControllerTexts()
        initUnfollowDialog()
        observeFollowUserState()
        startTime = System.currentTimeMillis()
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT < 24) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
        if (startTime != null) {
            val lectureDuration = (System.currentTimeMillis() - startTime!!) / 1000L
            STTFirebaseAnalytics.logEvent(
                STTFirebaseAnalytics.EVENT.LECTURE_DURATION,
                key = "lecture_duration",
                value = lectureDuration.toString()
            )
        }
    }

    private fun initializePlayer() {
        simpleExoPlayer = SimpleExoPlayer.Builder(this)
            .setSeekForwardIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
            .also {
                binding.pvVideo.player = it
                val mediaItem = MediaItem.fromUri(Uri.parse(tutoringUrl))
                it.setMediaItem(mediaItem)
            }

        simpleExoPlayer?.apply {
            playWhenReady = mPlayWhenReady
            seekTo(mCurrentWindow, mPlaybackPosition)
            prepare()
        }
    }

    private fun getExtraValues() {
        intent.extras ?: run {
            org.softwaremaestro.presenter.util.Util.createToast(this, "영상을 재생할 수 없습니다").show()
            finish()
        }
        with(intent.extras!!) {
            profileImage = getString(PROFILE_IMAGE, "")
            studentName = getString(STUDENT_NAME, "")
            schoolLevel = getString(SCHOOL_LEVEL, "")
            subject = getString(SUBJECT, "")
            description = getString(DESCRIPTION, "")
            tutoringUrl = getString(RECORDING_FILE_URL, "")
            teacherId = getString(TEACHER_ID, "")
            mFollowing = getBoolean(FOLLOWING)
        }

        if (teacherId.isEmpty()) {
            val heartIv = findViewById<ImageView>(R.id.iv_heart)
            heartIv.visibility = View.GONE
        }
    }

    private fun setControllerTexts() {
        findViewById<ImageView>(R.id.iv_back).setOnClickListener { finish() }
        findViewById<TextView>(R.id.tv_teacher_name).text = studentName
        findViewById<TextView>(R.id.tv_description).text = description
        findViewById<TextView>(R.id.tv_subject).text = "${schoolLevel} ${subject}"
        val imgView = findViewById<ImageView>(R.id.iv_profile_image)
        Glide.with(this).load(profileImage).centerCrop().into(imgView)
        val heartIv = findViewById<ImageView>(R.id.iv_heart)
        heartIv.background =
            if (mFollowing) resources.getDrawable(R.drawable.ic_heart_full, null)
            else resources.getDrawable(R.drawable.ic_heart_empty, null)
        heartIv.setOnClickListener {
            if (teacherId.isEmpty()) {
                org.softwaremaestro.presenter.util.Util.createToast(this, "선생님 아이디를 가져오는데 실패했습니다")
                    .show()
                return@setOnClickListener
            }

            if (!mFollowing) {
                followUserViewModel.followUser(teacherId)
            } else {
                unfollowDialog.show(supportFragmentManager, "unfollow dialog")
            }
        }
    }

    private fun initUnfollowDialog() {
        unfollowDialog = DetailAlertDialog(
            title = "선생님 찜하기를 취소할까요?",
            description = "선생님에게 예약 질문을 할 수 없게 됩니다"
        ) {
            followUserViewModel.unfollowUser(teacherId)
            findViewById<ImageView>(R.id.iv_heart).background =
                resources.getDrawable(R.drawable.ic_heart_empty, null)
            org.softwaremaestro.presenter.util.Util.createToast(this, "선생님 찜하기가 해제되었습니다").show()
        }
    }

    private fun observeFollowUserState() {
        followUserViewModel.followUserState.observe(this) { following ->
            following ?: return@observe
            mFollowing = following
            findViewById<ImageView>(R.id.iv_heart).background =
                if (following) resources.getDrawable(R.drawable.ic_heart_full, null)
                else resources.getDrawable(R.drawable.ic_heart_empty, null)
        }
    }

    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.pvVideo).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun releasePlayer() {
        simpleExoPlayer?.apply {
            mPlaybackPosition = currentPosition
            mCurrentWindow = currentWindowIndex
            mPlayWhenReady = playWhenReady
            release()
        }
        simpleExoPlayer = null
    }
}