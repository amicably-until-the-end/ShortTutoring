package org.softwaremaestro.presenter.chat_page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.softwaremaestro.presenter.databinding.ActivityQuestionImageBinding
import org.softwaremaestro.presenter.teacher_home.DESCRIPTION
import org.softwaremaestro.presenter.teacher_home.IMAGE
import org.softwaremaestro.presenter.teacher_home.SUBJECT
import org.softwaremaestro.presenter.teacher_home.adapter.QuestionDetailImagesAdapter

class QuestionImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuestionImageBinding

    var images: ArrayList<String>? = null
    var subject: String? = null
    var description: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentExtra()
        setQuestionContent()
        setImageRecyclerView()
        setCloseButton()

    }

    private fun setCloseButton() {
        binding.btnClose.setOnClickListener {
            finish()
        }
    }

    private fun setQuestionContent() {
        binding.tvDescription.text = description
        binding.tvSubject.text = subject
    }

    private fun getIntentExtra() {
        images = intent.getStringArrayListExtra(IMAGE)
        subject = intent.getStringExtra(SUBJECT)
        description = intent.getStringExtra(DESCRIPTION)
    }


    private fun setImageRecyclerView() {
        val adapter = QuestionDetailImagesAdapter()
        binding.rvImages.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(
                this@QuestionImageActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
        adapter.setItem(images as List<String>)
        adapter.notifyDataSetChanged()
    }
}