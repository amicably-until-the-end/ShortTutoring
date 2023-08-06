package org.softwaremaestro.presenter.classroom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.softwaremaestro.presenter.databinding.ActivityClassroomBinding

class ClassroomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassroomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassroomBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}