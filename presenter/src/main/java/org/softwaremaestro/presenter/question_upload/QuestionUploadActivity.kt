package org.softwaremaestro.presenter.question_upload

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ActivityLoginBinding
import org.softwaremaestro.presenter.databinding.ActivityQuestionUploadBinding

class QuestionUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }



}