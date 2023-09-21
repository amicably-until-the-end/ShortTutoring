package org.softwaremaestro.presenter.question_reserve

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentReservationFormBinding
import org.softwaremaestro.presenter.util.adapter.TimeRangePickerAdapter


class ReservationFormFragment : Fragment() {

    private lateinit var binding: FragmentReservationFormBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReservationFormBinding.inflate(layoutInflater)

        setDatePicker()
        setTimeRangePicker()

        binding.btnSubmit.setOnClickListener {
            findNavController().navigate(R.id.action_reservationFormFragment_to_studentChatFragment)
        }

        return binding.root
    }

    private fun setDatePicker() {
        binding.dpQuestionReserve.setOnDateSelectListener() { year, month, day ->
            binding.containerTimePicker.visibility = View.VISIBLE
            Toast.makeText(requireContext(), "$year-$month-$day", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setTimeRangePicker() {
        binding.trpTutoringTime.rvTimePicker.apply {
            adapter = TimeRangePickerAdapter(10,
                onBtnClick = { start, end ->
                    if (start != null && end != null) {
                        binding.btnSubmit.setBackgroundResource(R.drawable.bg_radius_10_grad_blue)
                        binding.tvSubmit.setTextColor(resources.getColor(R.color.white, null))

                        binding.containerTimeDuration.visibility = View.VISIBLE

                        binding.cvQuestionCost.visibility = View.VISIBLE

                        binding.cvQuestionCost.visibility = View.VISIBLE

                        // 선생님에게 지정질문하고 채팅 페이지로 이동
                    } else {
                        binding.btnSubmit.setBackgroundResource(R.drawable.bg_radius_10_background_grey)
                        binding.tvSubmit.setTextColor(
                            resources.getColor(
                                R.color.sub_text_grey,
                                null
                            )
                        )

                        binding.containerTimeDuration.visibility = View.INVISIBLE

                        binding.cvQuestionCost.visibility = View.GONE
                    }
                },
                onRangeChange = { start, end ->
                    val timeDuration = end.toTime() - start.toTime()
                    binding.tvSelectedTime.text =
                        "${start} ~ ${end} (${timeDuration}분)"
                    binding.tvQuestionCost.text =
                        (timeDuration * TEACHER_ANSWER_COST / 1_000).toString()
                })
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        }
    }

    companion object {
        private const val TEACHER_ANSWER_COST = 2000
    }
}