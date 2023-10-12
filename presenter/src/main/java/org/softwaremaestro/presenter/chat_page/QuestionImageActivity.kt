package org.softwaremaestro.presenter.chat_page

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.chat_page.viewmodel.QuestionImageViewModel
import org.softwaremaestro.presenter.databinding.ActivityQuestionImageBinding
import org.softwaremaestro.presenter.teacher_home.adapter.QuestionDetailImagesAdapter

@AndroidEntryPoint
class QuestionImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionImageBinding

    var questionId: String? = null

    private val viewModel: QuestionImageViewModel by viewModels()


    lateinit var adapter: QuestionDetailImagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCloseButton()
        getIntentExtra()
        setImageRecyclerView()
        observeQuestionInfo()
        questionId?.let { viewModel.getImages(it) }

    }

    private fun setCloseButton() {
        binding.btnClose.setOnClickListener {
            finish()
        }
    }

    private fun observeQuestionInfo() {
        viewModel.questionInfo.observe(this) {
            if (it != null) {
                setImageRecyclerViewItem(it.images ?: listOf())
                binding.tvSubject.text = it.problemSubject
                binding.tvDescription.text = it.problemDescription
            }
        }
    }

    private fun getIntentExtra() {
        questionId = intent.getStringExtra(QUESTION_ID)
    }


    private fun setImageRecyclerView() {
        adapter = QuestionDetailImagesAdapter()
        binding.rvImages.apply {
            this.adapter = this@QuestionImageActivity.adapter
            layoutManager = LinearLayoutManager(
                this@QuestionImageActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun setImageRecyclerViewItem(images: List<String>) {
        adapter.setItem(images)
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val QUESTION_ID = "questionId"
    }
}