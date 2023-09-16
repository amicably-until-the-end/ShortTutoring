package org.softwaremaestro.presenter.question_reserve

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.softwaremaestro.presenter.databinding.FragmentReservationFormBinding


class ReservationFormFragment : Fragment() {

    private lateinit var binding: FragmentReservationFormBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReservationFormBinding.inflate(layoutInflater)

        setDatePicker()

        return binding.root
    }

    private fun setDatePicker() {
        binding.dpQuestionReserve.setOnRangeSelectListener() { year, month, day ->
            Toast.makeText(requireContext(), "$year-$month-$day", Toast.LENGTH_SHORT).show()
        }
    }


}