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
        binding.dpQuestionReserve.setOnRangeSelectListener() { year, month, day ->
            Toast.makeText(requireContext(), "$year-$month-$day", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setTimeRangePicker() {
        binding.trpTutoringTime.rvTimePicker.apply {
            adapter = TimeRangePickerAdapter(10) { start, end ->
//                if (onRangeChangeListener != null) onRangeChangeListener!!(start, end)
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}