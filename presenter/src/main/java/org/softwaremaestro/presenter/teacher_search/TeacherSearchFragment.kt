package org.softwaremaestro.presenter.teacher_search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.domain.follow.entity.FollowingGetResponseVO
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentTeacherSearchBinding
import org.softwaremaestro.presenter.teacher_search.adapter.TeacherAdapter
import org.softwaremaestro.presenter.teacher_search.viewmodel.TeacherSearchViewModel
import org.softwaremaestro.presenter.util.hideKeyboardAndRemoveFocus

@AndroidEntryPoint
class TeacherSearchFragment : Fragment() {

    private lateinit var binding: FragmentTeacherSearchBinding
    private val teacherSearchViewModel: TeacherSearchViewModel by viewModels()
    private lateinit var teacherAdapter: TeacherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolBar()
        setAtvOptionSearch()
        setEtSearch()
        setRvTeacher()
        setObserver()
    }

    private fun setToolBar() {
        binding.btnToolbarBack.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun setAtvOptionSearch() {
        val optionAdapter = ArrayAdapter(
            requireContext(), R.layout.item_univ, listOf(
                "닉네임", "별점", "찜한 학생 수", "예약 질문 수", "대학교", "학과"
            )
        )
        with(binding.atvOptionSearch) {
            setAdapter(optionAdapter)
            onItemClickListener =
                AdapterView.OnItemClickListener { _, _, pos, _ ->
                    when (pos) {
                        0 -> {
                            binding.tvSearch.visibility = View.GONE
                            binding.containerSearch.visibility = View.VISIBLE
                        }

                        1 -> {
                            binding.tvSearch.visibility = View.VISIBLE
                            binding.containerSearch.visibility = View.GONE
                        }

                        else -> {}
                    }
                    binding.ivUnfold.setBackgroundResource(R.drawable.ic_unfold)
                }
        }

        binding.containerOptionSearch.setOnClickListener {
            with(binding.atvOptionSearch) {
                if (!isPopupShowing) {
                    showDropDown()
                    binding.ivUnfold.setBackgroundResource(R.drawable.ic_fold)
                } else {
                    dismissDropDown()
                    binding.ivUnfold.setBackgroundResource(R.drawable.ic_unfold)
                }
            }
        }
    }

    private fun setEtSearch() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if ("별점" == binding.atvOptionSearch.text.toString()) {
                    teacherSearchViewModel.searchTeacherByName(binding.etSearch.text.toString())
                } else {
                    // 설정된 검색 옵션에 맞게 viewModel의 다른 메서드 호출
                }
                hideKeyboardAndRemoveFocus(binding.etSearch)
                return@setOnEditorActionListener true
            } else return@setOnEditorActionListener false
        }
    }

    private fun setRvTeacher() {
        teacherAdapter = TeacherAdapter {
            // 선생님 다이어로그 띄워주기
        }

        binding.rvTeacher.apply {
            adapter = teacherAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }

    private fun setObserver() {
        teacherSearchViewModel.searchedResult.observe(viewLifecycleOwner) {
            it?.map {
                FollowingGetResponseVO(
                    id = it.teacherId,
                    name = it.nickname,
                    bio = it.bio,
                    profileImage = it.profileUrl,
                    role = "teacher",
                    schoolDivision = "학부 설정 안되어있음",
                    schoolName = it.univ,
                    schoolDepartment = "학부 설정 안되어있음",
                    schoolGrade = -1,
                    followersCount = -1,
                    followingCount = -1
                )
            }?.let {
                teacherAdapter.setItem(it)
                teacherAdapter.notifyDataSetChanged()
            }
        }
    }
}