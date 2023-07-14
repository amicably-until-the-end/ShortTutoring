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

        // 액션바 설정
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        // 뒤로가기 버튼
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}