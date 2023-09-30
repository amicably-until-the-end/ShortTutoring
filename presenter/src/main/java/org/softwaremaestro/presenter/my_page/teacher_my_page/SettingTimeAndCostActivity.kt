package org.softwaremaestro.presenter.my_page.teacher_my_page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.ActivitySettingTimeAndCostBinding
import org.softwaremaestro.presenter.util.adapter.TimePickerAdapter

@AndroidEntryPoint
class SettingTimeAndCostActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingTimeAndCostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingTimeAndCostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTimePickers()
    }

    private fun initTimePickers() {

        with(binding) {
            listOf(
                tpAnswerTimeMon,
                tpAnswerTimeTue,
                tpAnswerTimeWed,
                tpAnswerTimeThu,
                tpAnswerTimeFri,
                tpAnswerTimeSat,
                tpAnswerTimeSun
            ).forEach {
                it.rvTimePicker.apply {
                    adapter = TimePickerAdapter(60)
                    layoutManager = LinearLayoutManager(
                        this@SettingTimeAndCostActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                }
            }
        }
    }
}
