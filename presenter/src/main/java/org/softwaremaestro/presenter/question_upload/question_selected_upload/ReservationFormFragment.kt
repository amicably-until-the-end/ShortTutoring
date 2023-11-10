package org.softwaremaestro.presenter.question_upload.question_selected_upload

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.coin.viewModel.CoinViewModel
import org.softwaremaestro.presenter.databinding.FragmentReservationFormBinding
import org.softwaremaestro.presenter.question_upload.question_selected_upload.viewmodel.QuestionReservationViewModel
import org.softwaremaestro.presenter.student_home.viewmodel.MyProfileViewModel
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.adapter.TimeRangePickerAdapter
import org.softwaremaestro.presenter.util.moveBack
import org.softwaremaestro.presenter.util.widget.DetailAlertDialog
import org.softwaremaestro.presenter.util.widget.SimpleAlertDialog
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@AndroidEntryPoint
class ReservationFormFragment : Fragment() {

    private lateinit var binding: FragmentReservationFormBinding
    private val myProfileViewModel: MyProfileViewModel by activityViewModels()
    private val questionReservationViewModel: QuestionReservationViewModel by activityViewModels()
    private val coinViewModel: CoinViewModel by activityViewModels()

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

                        val now = LocalDateTime.now()
                        val pos = now.hour * 6 + now.minute / 10
                        binding.trpTutoringTime.rvTimePicker.scrollToPosition(pos + 10)
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
                })
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun observe() {
        observeIsReqDateAfterToday()
        observeInputProper()
        observeCoinFreeReceiveState()
    }

    private fun observeIsReqDateAfterToday() {
        questionReservationViewModel.isReqDateAfterToday.observe(viewLifecycleOwner) {
            if (!it && questionReservationViewModel.inputNotNull.value == true) {
                val now = LocalDateTime.now()
                Util.createToast(
                    requireActivity(),
                    "${now.hour}시 ${now.minute}분 이후의 시간을 선택해주세요"
                ).show()
            }
        }
    }

    private fun observeInputProper() {
        questionReservationViewModel.inputProper.observe(viewLifecycleOwner) { proper ->
            if (proper) {
                setBtnSubmit(true)
                binding.btnSubmit.setOnClickListener {
                    myProfileViewModel.amount.value ?: run {
                        Util.createToast(requireActivity(), "사용자의 코인을 가져오는데 실패했습니다")
                    }

                    if (myProfileViewModel.amount.value!! * 100 < QUESTION_UPLOAD_COST) {
                        DetailAlertDialog(
                            title = "코인이 부족합니다",
                            description = "매일 200코인이 기본 제공돼요.\n오늘의 코인을 받을까요?",
                            confirm = "코인 받기",
                            onConfirm = { coinViewModel.receiveCoinFree() }
                        ).show(parentFragmentManager, "coin is insufficient")
                        return@setOnClickListener
                    }

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
            } else {
                setBtnSubmit(false)
                binding.btnSubmit.setOnClickListener(null)
            }
        }
    }

    private fun observeCoinFreeReceiveState() {
        coinViewModel.coinFreeReceiveState.observe(viewLifecycleOwner) {
            it ?: return@observe

            myProfileViewModel.amount.value ?: run {
                Util.createToast(requireActivity(), "사용자의 코인을 가져오는데 실패했습니다").show()
                return@observe
            }

            // 무료 코인 받기 성공
            if (it) {
                SimpleAlertDialog().apply {
                    title = "오늘의 코인을 받았습니다"
                    description =
                        "현재 ${(myProfileViewModel.amount.value!! + 2) * 100}개의 코인을 보유하고 있어요"
                }.show(parentFragmentManager, "receive free coin success")
                // 코인을 업데이트하기 위해 getMyProfile() 호출
                myProfileViewModel.getMyProfile()
            } else {
                SimpleAlertDialog().apply {
                    title = "이미 오늘의 코인을 받았습니다"
                    description = "기본 코인은 매일 200개씩 제공돼요"
                }.show(parentFragmentManager, "receive free coin fail")
            }
            coinViewModel.resetCoinFreeReceiveState()
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

    companion object {
        private const val QUESTION_UPLOAD_COST = 100
    }
}