package org.softwaremaestro.presenter.question_upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.FragmentTeacherSelectBinding


@AndroidEntryPoint
class TeacherSelectFragment : Fragment() {


    lateinit var binding: FragmentTeacherSelectBinding
    private val viewModel: TeacherSelectViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentTeacherSelectBinding.inflate(layoutInflater, container, false)

        setTeacherRecycler()

        return binding.root
    }

    private fun setTeacherRecycler() {
        binding.rvTeacherList.apply {
            adapter = TeacherAdapter(viewModel.teacherList.value!!, object : OnItemClickListener {
                override fun onAcceptBtnClicked(teacherId: String) {
                    //check valid accept via viewmodel
                    //if valid goto tutoring room
                    //else show something
                    Toast.makeText(context, teacherId, Toast.LENGTH_SHORT)
                }

            })
        }

    }


}