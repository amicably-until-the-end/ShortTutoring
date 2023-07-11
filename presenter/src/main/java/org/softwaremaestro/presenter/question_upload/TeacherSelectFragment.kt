package org.softwaremaestro.presenter.question_upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.FragmentTeacherSelectBinding


@AndroidEntryPoint
class TeacherSelectFragment : Fragment() {


    lateinit var binding: FragmentTeacherSelectBinding
    private val viewModel: TeacherSelectViewModel by viewModels()


    lateinit var teacherListAdapter: TeacherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentTeacherSelectBinding.inflate(layoutInflater, container, false)


        viewModel.startGetTeacherList(arguments?.getString("question_id")!!)
        setTeacherRecycler()
        setObserver()

        return binding.root
    }

    private fun setObserver() {
        viewModel.teacherList.observe(viewLifecycleOwner, Observer {

        })
    }

    private fun setTeacherRecycler() {
        teacherListAdapter =
            TeacherAdapter(viewModel.teacherList.value!!, object : OnItemClickListener {
                override fun onAcceptBtnClicked(teacherId: String) {
                    // TODO : implement below
                    //check valid accept via viewmodel
                    //if valid goto tutoring room
                    //else show somethings
                    Toast.makeText(context, teacherId, Toast.LENGTH_SHORT)
                }

            })

        binding.rvTeacherList.apply {
            adapter = teacherListAdapter
        }

    }
}