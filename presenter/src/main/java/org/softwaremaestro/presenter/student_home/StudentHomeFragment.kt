package org.softwaremaestro.presenter.student_home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import org.softwaremaestro.presenter.databinding.FragmentStudentHomeBinding
import org.softwaremaestro.presenter.question_upload.QuestionUploadActivity


class StudentHomeFragment : Fragment() {

    private lateinit var binding: FragmentStudentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStudentHomeBinding.inflate(layoutInflater)

        binding.btnQuestion.setOnClickListener {
            val intent = Intent(activity, QuestionUploadActivity::class.java)
            startActivity(intent)
        }

        binding.rvTutoring.apply {
            adapter = TutoringAdapter {
                Log.d("message", it)
            }.apply {
                val tutorings = mutableListOf<Tutoring>().apply {
                    (0..10).forEach {
                        add(Tutoring("문제를 못 풀겠어요", "23/07/23"))
                    }
                }
                setItem(tutorings)
            }
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }

        return binding.root
    }
}