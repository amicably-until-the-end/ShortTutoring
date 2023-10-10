package org.softwaremaestro.presenter.teacher_home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.answer_upload.entity.AnswerUploadVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ActivityQuestionDetailBinding
import org.softwaremaestro.presenter.teacher_home.TeacherHomeFragment.Companion.DESCRIPTION
import org.softwaremaestro.presenter.teacher_home.TeacherHomeFragment.Companion.HOPE_TIME
import org.softwaremaestro.presenter.teacher_home.TeacherHomeFragment.Companion.IMAGE
import org.softwaremaestro.presenter.teacher_home.TeacherHomeFragment.Companion.OFFERED_ALREADY
import org.softwaremaestro.presenter.teacher_home.TeacherHomeFragment.Companion.OFFER_SUCCESS
import org.softwaremaestro.presenter.teacher_home.TeacherHomeFragment.Companion.QUESTION_ID
import org.softwaremaestro.presenter.teacher_home.TeacherHomeFragment.Companion.SUBJECT
import org.softwaremaestro.presenter.teacher_home.adapter.QuestionDetailImagesAdapter
import org.softwaremaestro.presenter.teacher_home.viewmodel.AnswerViewModel
import org.softwaremaestro.presenter.util.widget.SimpleAlertDialog

@AndroidEntryPoint
class QuestionDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionDetailBinding

    private val viewModel: AnswerViewModel by viewModels()

    private var images: ArrayList<String>? = null
    private var subject: String? = null
    private var description: String? = null
    private var questionId: String? = null
    private var hopeTime: String? = null
    private var offeredAlready: Boolean? = null


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
        if (offeredAlready!!) {
            binding.btnOffer.apply {
                setBackgroundResource(R.drawable.bg_radius_5_grey)
                setTextColor(resources.getColor(R.color.sub_text_grey, null))
                setOnClickListener {
                    Toast.makeText(this@QuestionDetailActivity, "이미 신청한 질문입니다", Toast.LENGTH_SHORT)
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
        viewModel.offerResult.observe(this) {
            if (it != null) {
                setResult(OFFER_SUCCESS)
                finish()
            } else {
                SimpleAlertDialog().apply {
                    title = "수업 요청을 실패했습니다"
                    description = "잠시 후에 다시 시도해주세요"
                }.show(supportFragmentManager, "simpleAlertDialog")
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