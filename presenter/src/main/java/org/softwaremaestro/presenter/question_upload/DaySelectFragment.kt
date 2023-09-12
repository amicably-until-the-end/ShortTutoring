package org.softwaremaestro.presenter.question_upload

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentReservationDaySelectBinding


class DaySelectFragment : Fragment() {

    private lateinit var binding: FragmentReservationDaySelectBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReservationDaySelectBinding.inflate(layoutInflater)

        setDatePicker()

        return binding.root
    }

    private fun setDatePicker() {
        binding.datePicker.setOnRangeSelectListener() { year, month, day ->
            Toast.makeText(requireContext(), "$year-$month-$day", Toast.LENGTH_SHORT).show()
        }
    }


}