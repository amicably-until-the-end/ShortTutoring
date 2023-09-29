package org.softwaremaestro.presenter.teacher_profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.ActivityTeacherProfileBinding

@AndroidEntryPoint
class TeacherProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeacherProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}