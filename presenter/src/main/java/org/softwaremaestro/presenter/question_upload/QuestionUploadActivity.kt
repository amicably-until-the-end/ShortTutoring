package org.softwaremaestro.presenter.question_upload

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.ActivityQuestionUploadBinding

@AndroidEntryPoint
class QuestionUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionUploadBinding
    var image: Bitmap? = null
    var description: String? = null
    var schoolLevelSelected: String? = null
    var subjectSelected: String? = null
    var difficultySelected: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}