package org.softwaremaestro.presenter.teacher_search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.ActivityTeacherSearchBinding

@AndroidEntryPoint
class TeacherSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}