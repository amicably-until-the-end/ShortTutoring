package org.softwaremaestro.presenter.teacher_my_page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.ActivitySettingTimeAndCostBinding

@AndroidEntryPoint
class SettingTimeAndCostActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingTimeAndCostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingTimeAndCostBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}