package org.softwaremaestro.presenter.teacher_home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.Util.LoadingDialog
import org.softwaremaestro.presenter.Util.UIState
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
    var questionId: String? = "!234"
    var hopeTime: ArrayList<String>? = null


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
        binding.tvHopeTime.text = hopeTime.toString()
    }

    fun getIntentExtra() {
        images = intent.getStringArrayListExtra(IMAGE)
        subject = intent.getStringExtra(SUBJECT)
        description = intent.getStringExtra(DESCRIPTION)
        questionId = intent.getStringExtra(QUESTION_ID)
        hopeTime = intent.getStringArrayListExtra(HOPE_TIME)
    }

    private fun setObserver() {
        viewModel.answer.observe(this) {
            if (it != null) {
                Toast.makeText(this, "요청이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
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