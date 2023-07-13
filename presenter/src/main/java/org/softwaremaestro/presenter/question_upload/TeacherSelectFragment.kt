package org.softwaremaestro.presenter.question_upload

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.question_upload.entity.TeacherVO
import org.softwaremaestro.presenter.databinding.FragmentTeacherSelectBinding
import java.util.logging.Logger


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

        val question_id = arguments?.getString("questionId")

        if (question_id == null) {
            Toast.makeText(context, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("mymymy", "$question_id question id")
            viewModel.startGetTeacherList(question_id)
        }
        setTeacherRecycler()
        setObserver()

        return binding.root
    }

    private fun setObserver() {
        viewModel.teacherList.observe(viewLifecycleOwner, Observer {
            Log.d("mymymy", "${viewModel.teacherList.value.toString()} is changed")
            teacherListAdapter.setItems(viewModel.teacherList.value ?: emptyList())
            teacherListAdapter.notifyDataSetChanged()

        })
    }

    private fun setTeacherRecycler() {
        teacherListAdapter =
            TeacherAdapter(
                viewModel.teacherList.value ?: emptyList(),
                object : OnItemClickListener {
                    override fun onAcceptBtnClicked(teacherId: String) {

                    }

                })

        binding.rvTeacherList.apply {
            adapter = teacherListAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
        teacherListAdapter.setItems(
            viewModel.teacherList.value ?: emptyList()
        )

    }
}