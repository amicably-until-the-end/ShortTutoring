package org.softwaremaestro.presenter.classroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentClassroomBinding

class ClassroomFragment : Fragment() {

    private lateinit var binding: FragmentClassroomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentClassroomBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTutoringId.text = requireActivity().intent.getStringExtra("tutoringId") ?: "-"
    }
}