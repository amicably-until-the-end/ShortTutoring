package org.softwaremaestro.presenter.question_upload

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentReservationDaySelectBinding


class DaySelectFragment : Fragment() {

    private lateinit var binding: FragmentReservationDaySelectBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReservationDaySelectBinding.inflate(layoutInflater)
        return binding.root
    }


}