package org.softwaremaestro.presenter.question_upload.question_selected_upload

import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import org.softwaremaestro.presenter.student_home.viewmodel.MyProfileViewModel
import org.softwaremaestro.presenter.util.adapter.TimeRangePickerAdapter
import org.softwaremaestro.presenter.util.moveBack
import org.softwaremaestro.presenter.util.widget.SimpleAlertDialog
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@AndroidEntryPoint
class ReservationFormFragment : Fragment() {

    private lateinit var binding: FragmentReservationFormBinding
    private val myProfileViewModel: MyProfileViewModel by activityViewModels()
    private val questionReservationViewModel: QuestionReservationViewModel by activityViewModels()
    private var cost: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReservationFormBinding.inflate(layoutInflater)
        myProfileViewModel.getMyProfile()
        resetViewModelValue()
        setDatePicker()
        setTimeRangePicker()
        observe()
        setToolbar()

        return binding.root
    }

    private fun resetViewModelValue() {
        questionReservationViewModel.resetInputs()
    }

    private fun setDatePicker() {
        binding.dpQuestionReserve.apply {
            setOnDateSelectListener { year, month, day ->
                val adapter = binding.trpTutoringTime.rvTimePicker.adapter as TimeRangePickerAdapter
                adapter.rangeStart = null
                adapter.rangeEnd = null
                adapter.notifyDataSetChanged()
                questionReservationViewModel.setRequestTutoringStartTime(null)
                questionReservationViewModel.setRequestTutoringEndTime(null)

                questionReservationViewModel.setRequestDate(LocalDate.of(year, month, day))
                binding.containerTimePicker.visibility = View.VISIBLE

                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        with(binding.svContent) {
                            isSmoothScrollingEnabled = true
                            fullScroll(View.FOCUS_DOWN)
                        }
                    }, 200L
                )
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
                    questionReservationViewModel.setRequestTutoringStartTime(
                        LocalTime.of(start.hour, start.minute)
                    )
                    questionReservationViewModel.setRequestTutoringEndTime(
                        LocalTime.of(end.hour, end.minute)
                    )
                    val timeDuration = end.plusMinute(10).toTime() - start.toTime()
                    binding.tvSelectedTime.text =
                        "${start} ~ ${end.plusMinute(10)} (${timeDuration}분)"
                    cost = timeDuration * 10
                    binding.tvQuestionCost.text = "${cost}"
                })
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observe() {
        observeIsReqDateAfterToday()
        observeInputProper()
    }

    private fun observeIsReqDateAfterToday() {
        questionReservationViewModel.isReqDateAfterToday.observe(viewLifecycleOwner) {
            if (!it && questionReservationViewModel.inputNotNull.value == true) {
                val now = LocalDateTime.now()
                Toast.makeText(
                    requireContext(),
                    "${now.hour}시 ${now.minute}분 이후의 시간을 선택해주세요",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun observeInputProper() {
        questionReservationViewModel.inputProper.observe(viewLifecycleOwner) { proper ->
            if (proper) {
                setBtnSubmit(true)
                binding.btnSubmit.setOnClickListener {
                    // 보유한 코인이 부족한 경우
                    if (cost > myProfileViewModel.amount.value!!) {
                        SimpleAlertDialog().apply {
                            title = "코인이 부족합니다"
                            description = "코인을 충전한 후 다시 질문해주세요"
                        }.show(parentFragmentManager, "coin is insufficient")
                    } else {
                        Handler(Looper.getMainLooper()).postDelayed({
                            findNavController().navigate(R.id.action_reservationFormFragment_to_questionCameraFragment)
                        }, 200L)


                        binding.btnSubmit.background = resources.getDrawable(
                            R.drawable.bg_radius_5_background_light_blue,
                            null
                        )
                        binding.tvSubmit.setTextColor(
                            resources.getColor(
                                R.color.primary_blue,
                                null
                            )
                        )

                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.btnSubmit.background =
                                resources.getDrawable(R.drawable.bg_radius_5_grad_blue, null)
                            binding.tvSubmit.setTextColor(resources.getColor(R.color.white, null))
                        }, 500L)
                    }
                }
            } else {
                setBtnSubmit(false)
                binding.btnSubmit.setOnClickListener(null)
            }
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

    private fun setToolbar() {
        binding.btnToolbarBack.setOnClickListener {
            moveBack()
        }
    }
}