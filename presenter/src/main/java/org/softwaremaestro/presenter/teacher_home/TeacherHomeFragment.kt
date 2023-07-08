package org.softwaremaestro.presenter.teacher_home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.softwaremaestro.presenter.databinding.FragmentTeacherHomeBinding

class TeacherHomeFragment : Fragment() {

    private lateinit var binding: FragmentTeacherHomeBinding
    private lateinit var dialog: WaitingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTeacherHomeBinding.inflate(inflater, container, false)

        binding.btnAnswer.setOnClickListener {
            dialog = WaitingDialog(requireActivity())
            dialog.show()
        }

        return binding.root
    }
}