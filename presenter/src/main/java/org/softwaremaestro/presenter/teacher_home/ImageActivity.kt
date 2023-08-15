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

        val image = intent.getStringExtra(IMAGE)
        val subject = intent.getStringExtra(SUBJECT)
        val difficulty = intent.getStringExtra(DIFFICULTY)
        val description = intent.getStringExtra(DESCRIPTION)

        try {
            Glide.with(this).load(image).transform().sizeMultiplier(0.1f).centerCrop()
                .into(binding.containerImage)
            binding.tvSubjectAndDifficulty.text = "${subject} · ${difficulty}"
            binding.tvDesciption.text = description

            binding.btnClose.setOnClickListener {
                finish()
            }
        } catch (e: Exception) {
            e.printStackTrace()

            // 에러 처리
        }
    }
}