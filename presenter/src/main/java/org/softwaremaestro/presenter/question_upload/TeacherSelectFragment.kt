package org.softwaremaestro.presenter.question_upload

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.question_upload.entity.TeacherPickReqVO
import org.softwaremaestro.presenter.Util.LoadingDialog
import org.softwaremaestro.presenter.Util.UIState
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
    lateinit var teacherSelectableAdapter: TeacherSelectableAdapter

    private lateinit var loadingDialog: LoadingDialog

    private var noTeacher = true


    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentTeacherSelectBinding.inflate(inflater, container, false)

        retrieveArguments()
        setToolBar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObserver()

    }


    private fun retrieveArguments() {
        val questionId = arguments?.getString("questionId")

        if (questionId != null) {
            viewModel.startGetTeacherList(questionId)
            setTeacherRecycler(questionId)
        }
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
            teacherSelectableAdapter.setItems(viewModel.teacherList.value ?: emptyList())
            teacherSelectableAdapter.notifyDataSetChanged()

        }
    }

    /**
     * 강사를 선택하면, 선택한 강사의 id를 서버에 전달하고, 서버에서 전달받은 whiteboard 정보를 observe한다.
     */
    private fun observeTutoringInfo() {
        viewModel.teacherSelectState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is UIState.Loading -> {
                    loadingDialog = LoadingDialog(requireActivity())
                    loadingDialog.show()
                }

                is UIState.Success -> {
                    loadingDialog.dismiss()
                    val data = it.data
                    //Acticity 간 data class 전달을 위해 Serializable 사용
                    val whiteBoardInfo = SerializedWhiteBoardRoomInfo(
                        data.whiteBoardAppId,
                        data.whiteBoardUUID,
                        data.whiteBoardToken,
                        "2"
                    )
                    val voiceRoomInfo = SerializedVoiceRoomInfo(
                        data.RTCAppId,
                        data.studentRTCToken,
                        data.tutoringId,
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
                }

                is UIState.Failure -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireActivity(), "강사 선택에 실패했습니다.", Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {

                }

            }

        })
    }

    private fun setObserver() {
        observeTeacherList()
        observeTutoringInfo()
    }

    private fun setTeacherRecycler(questionId: String) {
        teacherSelectableAdapter =
            TeacherSelectableAdapter(
                viewModel.teacherList.value ?: emptyList()
            ) { teacherId: String ->
                if (viewModel.teacherSelectState.value == UIState.Loading) return@TeacherSelectableAdapter
                viewModel.pickTeacher(
                    TeacherPickReqVO(
                        questionId = questionId,
                        STUDENT_ID,
                        teacherId = teacherId
                    )
                )
            }
        binding.rvTeacherList.apply {
            adapter = teacherSelectableAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
        teacherSelectableAdapter.setItems(
            viewModel.teacherList.value ?: emptyList()
        )
    }

    private fun setToolBar() {
        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}