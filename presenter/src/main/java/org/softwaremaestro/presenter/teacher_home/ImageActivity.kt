package org.softwaremaestro.presenter.teacher_home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.softwaremaestro.presenter.databinding.ActivityImageBinding


class ImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val image = intent.getStringExtra("image")

        if (image == null) {
            // 에러 처리
        } else {
            Glide.with(this).load(image).sizeMultiplier(0.1f).fitCenter()
                .into(binding.containerImage)
        }

        binding.containerImage.setOnClickListener {
            finish()
        }
    }
}