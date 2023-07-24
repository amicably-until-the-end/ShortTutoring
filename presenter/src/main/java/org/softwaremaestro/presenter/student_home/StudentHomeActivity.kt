package org.softwaremaestro.presenter.student_home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.ActivityStudentHomeBinding

@AndroidEntryPoint
class StudentHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 액션바 설정
//        val toolbar: Toolbar = binding.toolbar
//        if (toolbar != null)
//            setSupportActionBar(toolbar)

        // 뒤로가기 버튼
//        val actionBar = supportActionBar
//        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}