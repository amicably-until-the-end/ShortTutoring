package org.softwaremaestro.presenter.question_upload

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.question_upload.entity.TeacherPickReqVO
import org.softwaremaestro.presenter.classroom.ClassroomActivity
import org.softwaremaestro.presenter.classroom.item.SerializedVoiceRoomInfo
import org.softwaremaestro.presenter.classroom.item.SerializedWhiteBoardRoomInfo
import org.softwaremaestro.presenter.databinding.FragmentTeacherSelectBinding
import org.softwaremaestro.presenter.question_upload.viewmodel.TeacherSelectViewModel

private const val STUDENT_ID = "test-student-id"

@AndroidEntryPoint
class TeacherSelectFragment : Fragment() {

    lateinit var binding: FragmentTeacherSelectBinding
    private val viewModel: TeacherSelectViewModel by viewModels()
    lateinit var teacherListAdapter: TeacherAdapter
    var noTeacher = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentTeacherSelectBinding.inflate(inflater, container, false)


        val questionId = arguments?.getString("questionId")

        if (questionId == null) {
            Log.d("mymymy", "null question id")
        } else {
            viewModel.startGetTeacherList(questionId)
            setTeacherRecycler(questionId)
        }
        setObserver()
        setToolBar()

        return binding.root
    }


    /**
     * 질문을 답변하겠다고 한 강사 목록을 observe하고, 강사 목록이 변경되면 adapter에 변경된 목록을 전달한다.
     */
    private fun observeTeacherList() {
        viewModel.teacherList.observe(viewLifecycleOwner) {
            if (viewModel.teacherList.value?.size == 1 && noTeacher) {
                noTeacher = false
                binding.layoutParent.removeView(binding.ivWaitingIcon)
            } else if (viewModel.teacherList.value?.size == 1 && noTeacher) {
                noTeacher = true
                binding.layoutParent.addView(binding.ivWaitingIcon)
            }
            teacherListAdapter.setItems(viewModel.teacherList.value ?: emptyList())
            teacherListAdapter.notifyDataSetChanged()

        }
    }

    /**
     * 강사를 선택하면, 선택한 강사의 id를 서버에 전달하고, 서버에서 전달받은 whiteboard 정보를 observe한다.
     */
    private fun observeTutoringInfo() {
        viewModel.tutoringInfo.observe(viewLifecycleOwner, Observer {

            if (it == null) {
                return@Observer
            }
            //Acticity 간 data class 전달을 위해 Serializable 사용
            val whiteBoardInfo = SerializedWhiteBoardRoomInfo(
                it.whiteBoardAppId,
                it.whiteBoardUUID,
                it.whiteBoardToken,
                "2"
            )
            val voiceRoomInfo = SerializedVoiceRoomInfo(
                it.RTCAppId,
                it.studentRTCToken,
                it.tutoringId,
                2,
            )
            //classroom activty에 데이터 전달 및 실행
            val intent = Intent(requireActivity(), ClassroomActivity::class.java).apply {
                putExtra("whiteBoardInfo", whiteBoardInfo)
                putExtra("voiceRoomInfo", voiceRoomInfo)
            }

            //classroom activity 실행
            startActivity(intent)


            //현재 activity 종료
            activity?.finish()
        })
    }

    private fun setObserver() {
        observeTeacherList()
        observeTutoringInfo()
    }

    private fun setTeacherRecycler(questionId: String) {
        teacherListAdapter =
            TeacherAdapter(viewModel.teacherList.value ?: emptyList()) { teacherId: String ->
                viewModel.pickTeacher(
                    TeacherPickReqVO(
                        questionId = questionId,
                        STUDENT_ID,
                        teacherId = teacherId
                    )
                )
            }

        binding.rvTeacherList.apply {
            adapter = teacherListAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
        teacherListAdapter.setItems(
            viewModel.teacherList.value ?: emptyList()
        )
    }

    private fun setToolBar() {
        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}