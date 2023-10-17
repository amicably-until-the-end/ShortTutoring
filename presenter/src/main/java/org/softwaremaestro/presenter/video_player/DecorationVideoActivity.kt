package org.softwaremaestro.presenter.video_player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.softwaremaestro.presenter.databinding.ActivityDecorationVideoBinding

class DecorationVideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDecorationVideoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDecorationVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}