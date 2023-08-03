package org.softwaremaestro.presenter.classroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ActivityClassroomBinding

class ClassroomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassroomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassroomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
    }
}