package org.softwaremaestro.presenter.question_upload.question_selected_upload

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.ActivityQuestionReserveBinding

@AndroidEntryPoint
class QuestionReserveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionReserveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionReserveBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}