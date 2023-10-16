package org.softwaremaestro.presenter.video_player

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import org.softwaremaestro.presenter.databinding.ActivityVideoPlayerBinding


class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding
    private var simpleExoPlayer: ExoPlayer? = null
    private var mPlayWhenReady = false
    private var mCurrentWindow = 0
    private var mPlaybackPosition = 0L

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
    }

    private fun initializePlayer() {
        simpleExoPlayer = SimpleExoPlayer.Builder(this).build().also {
            binding.pvVideo.player = it

            val videoUrl =
                "https://www.shutterstock.com/shutterstock/videos/1044255715/preview/stock-footage-person-signing-important-document-camera-following-tip-of-the-pen-as-it-signs-crucial-business.webm"
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
            it.setMediaItem(mediaItem)
        }

        simpleExoPlayer?.apply {
            playWhenReady = mPlayWhenReady
            seekTo(mCurrentWindow, mPlaybackPosition)
            prepare()
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