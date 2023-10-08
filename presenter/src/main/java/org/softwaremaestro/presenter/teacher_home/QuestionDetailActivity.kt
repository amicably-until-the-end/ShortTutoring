package org.softwaremaestro.presenter.teacher_home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadVO
import org.softwaremaestro.presenter.databinding.ActivityQuestionDetailBinding
import org.softwaremaestro.presenter.teacher_home.adapter.QuestionDetailImagesAdapter
import org.softwaremaestro.presenter.teacher_home.viewmodel.AnswerViewModel

@AndroidEntryPoint
class QuestionDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionDetailBinding

    private val viewModel: AnswerViewModel by viewModels()

    var images: ArrayList<String>? = null
    var subject: String? = null
    var description: String? = null
    var questionId: String? = null
    var hopeTime: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentExtra()
        setQuestionContent()
        setImageRecyclerView()
        setOfferButton()
        setObserver()
        setCloseButton()

    }

    private fun setOfferButton() {
        binding.btnOffer.setOnClickListener {
            viewModel.uploadAnswer(AnswerUploadVO(questionId!!))
        }
    }

    private fun setCloseButton() {
        binding.btnClose.setOnClickListener {
            finish()
        }
    }

    private fun setQuestionContent() {
        binding.tvDescription.text = description
        binding.tvSubject.text = subject
        binding.tvHopeTime.text = hopeTime
    }

    fun getIntentExtra() {
        images = intent.getStringArrayListExtra(IMAGE)
        subject = intent.getStringExtra(SUBJECT)
        description = intent.getStringExtra(DESCRIPTION)
        questionId = intent.getStringExtra(QUESTION_ID)
        hopeTime = intent.getStringExtra(HOPE_TIME)
    }

    private fun setObserver() {
        viewModel.answer.observe(this) {
            if (it != null) {
                Toast.makeText(this, "요청이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "이미 등록한 문제입니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setImageRecyclerView() {
        val adapter = QuestionDetailImagesAdapter()
        binding.rvImages.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(
                this@QuestionDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
        adapter.setItem(images as List<String>)
        adapter.notifyDataSetChanged()
    }
    /*
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
     */
}