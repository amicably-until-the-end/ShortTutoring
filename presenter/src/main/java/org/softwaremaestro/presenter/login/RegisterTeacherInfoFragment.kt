package org.softwaremaestro.presenter.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.softwaremaestro.domain.common.BaseResult
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentRegisterTeacherInfoBinding
import org.softwaremaestro.presenter.login.univGet.UnivGetService
import org.softwaremaestro.presenter.login.viewmodel.TeacherRegisterViewModel
import org.softwaremaestro.presenter.teacher_home.TeacherHomeActivity
import org.softwaremaestro.presenter.util.hideKeyboardAndRemoveFocus
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor

// 회원가입 두 번째 화면.
// 개인정보를 입력한다.
@AndroidEntryPoint
class RegisterTeacherInfoFragment : Fragment() {

    private lateinit var binding: FragmentRegisterTeacherInfoBinding
    private val viewModel: TeacherRegisterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterTeacherInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val service = UnivGetService()
        lifecycleScope.launch {
            service.getUniv("한양")
                .catch {
                    Log.d("hhcc", it.message.toString())
                }
                .collect { result ->
                    when (result) {
                        is BaseResult.Success -> Log.d("hhcc", result.data.toString())
                        is BaseResult.Error -> Log.d("hhcc", result.rawResponse)
                    }
                }
        }

        // 필드를 클릭하면 해당 필드에 값을 입력하는 페이지로 이동한다
        setOnClickListener()

        // 다음 버튼을 누르면 로고화면으로 돌아간다
        setFieldButtons()
        setNextButton()
        setObserver()
        setToolBar()

        val univs = arrayOf("고려대학교", "연세대학교", "서울대학교", "성균관대학교", "숭실대학교", "한양대학교")
        val univAdpater = ArrayAdapter(requireContext(), R.layout.item_univ, univs)
        binding.atvUniv.setAdapter(univAdpater)

        binding.atvUniv.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->

                hideKeyboardAndRemoveFocus(binding.atvUniv)
            }
    }

    private fun setToolBar() {
        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    private fun setOnClickListener() {
        setNextButton()
    }

    private fun setNextButton() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_registerTeacherInfoFragment_to_univAuthFragment)
        }
    }

    private fun setFieldButtons() {
//        binding.etCollege.setOnClickListener {
//            findNavController().navigate(R.id.action_registerTeacherInfoFragment_to_searchCollegeFragment)
//        }
//        binding.etUniv.setOnClickListener {
//            findNavController().navigate(R.id.action_registerTeacherInfoFragment_to_searchUnivFragment)
//        }
//        binding.etMajor.setOnClickListener {
//            findNavController().navigate(R.id.action_registerTeacherInfoFragment_to_searchMajorFragment)
//        }
//        binding.etYearOfAdmission.setOnClickListener {
//            findNavController().navigate(R.id.action_registerTeacherInfoFragment_to_searchYearOfAdmissionFragment)
//        }
    }


    private fun setObserver() {
//        viewModel.admissonYear.observe(viewLifecycleOwner) {
//            binding.etYearOfAdmission.setText(it)
//            checkFields()
//        }
//        viewModel.univ.observe(viewLifecycleOwner) {
//            binding.etUniv.setText(it)
//            checkFields()
//        }
//        viewModel.college.observe(viewLifecycleOwner) {
//            binding.etCollege.setText(it)
//            checkFields()
//        }
//        viewModel.major.observe(viewLifecycleOwner) {
//            binding.etMajor.setText(it)
//            checkFields()
//        }

        viewModel.registerResult.observe(viewLifecycleOwner) { isRegisterSuccess ->
            if (isRegisterSuccess) {
                val intent = Intent(requireContext(), TeacherHomeActivity::class.java)
                startActivity(intent)
                //TODO: 시작하는 activity 제외하고 모두 종료
            } else {
                // TODO: 회원가입 실패 처리
                findNavController().popBackStack()
            }
        }
    }

    /**
     * // 입력받지 않은 에딧텍스트가 있으면 버튼을 활성화하지 않는다
     */
    private fun checkFields() {

        if (viewModel.major.value != null && viewModel.univ.value != null && viewModel.college.value != null && viewModel.admissonYear.value != null) {
            binding.btnNext.setEnabledAndChangeColor(true)
        } else {
            binding.btnNext.setEnabledAndChangeColor(false)
        }
    }


}