package org.softwaremaestro.presenter.teacher_home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
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
            Picasso.with(this).load(image).fit().centerCrop().into(binding.containerImage)
        }

        binding.containerImage.setOnClickListener {
            supportFinishAfterTransition()
        }
    }
}