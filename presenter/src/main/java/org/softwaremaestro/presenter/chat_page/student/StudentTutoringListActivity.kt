package org.softwaremaestro.presenter.chat_page.student

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.softwaremaestro.presenter.databinding.ActivityStudentTutoringListBinding

class StudentTutoringListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentTutoringListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentTutoringListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}