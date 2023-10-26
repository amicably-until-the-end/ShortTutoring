package org.softwaremaestro.presenter.teacher_home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ActivityQuestionDetailBinding
import org.softwaremaestro.presenter.teacher_home.TeacherHomeFragment.Companion.DESCRIPTION
import org.softwaremaestro.presenter.teacher_home.TeacherHomeFragment.Companion.HOPE_TIME
import org.softwaremaestro.presenter.teacher_home.TeacherHomeFragment.Companion.IMAGE
import org.softwaremaestro.presenter.teacher_home.TeacherHomeFragment.Companion.OFFERED_ALREADY
import org.softwaremaestro.presenter.teacher_home.TeacherHomeFragment.Companion.QUESTION_ID
import org.softwaremaestro.presenter.teacher_home.TeacherHomeFragment.Companion.SUBJECT
import org.softwaremaestro.presenter.teacher_home.adapter.QuestionDetailImagesAdapter
import org.softwaremaestro.presenter.teacher_home.viewmodel.AnswerViewModel

@AndroidEntryPoint
class QuestionDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionDetailBinding

    private val viewModel: AnswerViewModel by viewModels()

    private var images: ArrayList<String>? = null
    private var subject: String? = null
    private var description: String? = null
    private var questionId: String? = null
    private var hopeTime: String? = null
    private var offeredAlready = false


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
        if (offeredAlready) {
            binding.btnOffer.apply {
                setBackgroundResource(R.drawable.bg_radius_5_grey)
                setTextColor(resources.getColor(R.color.sub_text_grey, null))
                setOnClickListener {
                    Toast.makeText(this@QuestionDetailActivity, "이미 신청한 수업입니다", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            binding.btnOffer.apply {
                setBackgroundResource(R.drawable.bg_radius_5_grad_blue)
                setTextColor(resources.getColor(R.color.white, null))
                setOnClickListener {
                    viewModel.uploadAnswer(AnswerUploadVO(questionId!!))
                }
            }
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
        offeredAlready = intent.getBooleanExtra(OFFERED_ALREADY, false)
    }

    private fun setObserver() {
        viewModel.answer.observe(this) {
            it ?: return@observe

            val intent = intent
            intent.putExtra(OFFER_RESULT, OFFER_SUCCESS)
            intent.putExtra(CHAT_ID, it.chatRoomId)
            setResult(RESULT_OK, intent)
            finish()

            viewModel.resetAnswer()
        }
    }

    private fun setImageRecyclerView() {
        val mAdapter = QuestionDetailImagesAdapter().apply {
            setItem(images as List<String>)
            notifyDataSetChanged()
        }

        binding.rvImages.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                this@QuestionDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }.also {
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(it)
        }
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
    companion object {
        const val OFFER_RESULT = "offer_result"
        const val OFFER_SUCCESS = 100
        const val OFFER_FAIL = 200
        const val CHAT_ID = "chat_id"
    }
}