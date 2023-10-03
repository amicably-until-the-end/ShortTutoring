package org.softwaremaestro.presenter.question_upload.question_selected_upload

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentReservationFormBinding
import org.softwaremaestro.presenter.question_upload.question_selected_upload.viewmodel.QuestionReservationViewModel
import org.softwaremaestro.presenter.util.adapter.TimeRangePickerAdapter
import java.time.LocalDate
import java.time.LocalTime

@AndroidEntryPoint
class ReservationFormFragment : Fragment() {

    private lateinit var binding: FragmentReservationFormBinding
    private val questionReservationViewModel: QuestionReservationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReservationFormBinding.inflate(layoutInflater)

        resetViewModelValue()
        setDatePicker()
        setTimeRangePicker()
        observe()

        return binding.root
    }

    private fun resetViewModelValue() {
        questionReservationViewModel.resetInputs()
    }

    private fun setDatePicker() {
        binding.dpQuestionReserve.apply {
            setOnDateSelectListener { year, month, day ->
                questionReservationViewModel.setRequestDate(LocalDate.of(year, month, day))
                binding.containerTimePicker.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "$year-$month-$day", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setTimeRangePicker() {
        binding.trpTutoringTime.rvTimePicker.apply {
            adapter = TimeRangePickerAdapter(10,
                onBtnClick = { start, end ->
                    questionReservationViewModel.setRequestTutoringStartTime(
                        start?.run { LocalTime.of(hour, minute) }
                    )
                    questionReservationViewModel.setRequestTutoringEndTime(
                        end?.run { LocalTime.of(hour, minute) }
                    )
                },
                onRangeChange = { start, end ->
                    with(start) {
                        questionReservationViewModel.setRequestTutoringStartTime(
                            LocalTime.of(hour, minute)
                        )
                        Log.d(
                            "onRangeChange",
                            "start: ${questionReservationViewModel.requestTutoringStartTime.value}"
                        )
                    }
                    with(end) {
                        questionReservationViewModel.setRequestTutoringEndTime(
                            LocalTime.of(hour, minute)
                        )
                        Log.d(
                            "onRangeChange",
                            "end: ${questionReservationViewModel.requestTutoringEndTime}"
                        )
                    }
                    Log.d("onRangeChange", "start: $start, end: $end")
                    val timeDuration = end.toTime() - start.toTime()
                    binding.tvSelectedTime.text =
                        "${start} ~ ${end} (${timeDuration}분)"
                    binding.tvQuestionCost.text =
                        (timeDuration * TEACHER_ANSWER_COST / 1_000).toString()
                })
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


        }
    }

    private fun observe() {
        questionReservationViewModel.inputProper.observe(viewLifecycleOwner) { proper ->
            if (proper) {
                setBtnSubmit(true)
                binding.btnSubmit.moveToCameraFragmentWhenClicked()
            } else {
                setBtnSubmit(false)
                binding.btnSubmit.setOnClickListener(null)
            }
        }
    }

    private fun View.moveToCameraFragmentWhenClicked() {
        setOnClickListener {
            findNavController().navigate(R.id.action_reservationFormFragment_to_questionCameraFragment)
        }
    }

    private fun setBtnSubmit(activated: Boolean) {
        with(binding) {
            if (activated) {
                btnSubmit.setBackgroundResource(R.drawable.bg_radius_10_grad_blue)
                tvSubmit.setTextColor(resources.getColor(R.color.white, null))

                containerTimeDuration.visibility = View.VISIBLE

                cvQuestionCost.visibility = View.VISIBLE

            } else {
                btnSubmit.setBackgroundResource(R.drawable.bg_radius_10_background_grey)
                tvSubmit.setTextColor(resources.getColor(R.color.sub_text_grey, null))

                containerTimeDuration.visibility = View.INVISIBLE

                cvQuestionCost.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val TEACHER_ANSWER_COST = 2000
    }
}