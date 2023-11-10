package org.softwaremaestro.presenter.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentUnivAuthBinding
import org.softwaremaestro.presenter.login.viewmodel.TeacherRegisterViewModel
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.setEnabledAndChangeColor

@AndroidEntryPoint
class UnivAuthFragment : Fragment() {

    private lateinit var binding: FragmentUnivAuthBinding
    private val viewModel: TeacherRegisterViewModel by activityViewModels()
    private lateinit var univAndMails: HashMap<String, HashMap<String, String>>
    private var isSmallSizeScreen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnivAuthBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportSmallScreenSize()
        setToolBar()
        initUnivAndMails()
        setEtUnivMailHeader()
        setEtUnivMailBody()
        setSendMailButton()
        setEtAuthCode()
        setNextButton()
        observe()
    }

    private fun supportSmallScreenSize() {
        val width = Util.getWidth(requireActivity())
        isSmallSizeScreen = width < 600
        if (isSmallSizeScreen) {
            val paddingValue = Util.toPx(30, requireContext())
            binding.glLeft.setGuidelineBegin(paddingValue)
            binding.glRight.setGuidelineBegin(paddingValue)
        }
    }

    private fun setToolBar() {
        binding.btnToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setEtUnivMailHeader() {
        binding.etUnivMailHeader.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.btnSendMail.setEnabledAndChangeColor(!p0.isNullOrEmpty() && !binding.etUnivMailBody.text.isNullOrEmpty())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun initUnivAndMails() {
        val assetsManager = resources.assets
        try {
            val inputStream = assetsManager.open("univAndMails.json")
            val reader = inputStream.bufferedReader()
            val gson = Gson()
            // Define the type of the outermost structure
            val type =
                object : TypeToken<HashMap<String, HashMap<String, String>>>() {}.type
            // Parse the JSON string and populate mathSubjects
            univAndMails = gson.fromJson(reader, type)
        } catch (e: Exception) {
            return
        }
    }

    private fun setEtUnivMailBody() {
        viewModel.schoolName.value.let { schoolName ->
            if (schoolName in univAndMails.keys) {
                binding.etUnivMailBody.setText(univAndMails[schoolName]!!["메일"])
            } else {
                binding.etUnivMailBody.isEnabled = true
            }
        }

        binding.etUnivMailBody.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.btnSendMail.setEnabledAndChangeColor(!p0.isNullOrEmpty() && !binding.etUnivMailHeader.text.isNullOrEmpty())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun setSendMailButton() {
        binding.btnSendMail.setOnClickListener {
            viewModel.sendVerificationMail("${binding.etUnivMailHeader.text}@${binding.etUnivMailBody.text}")
        }
    }

    private fun setEtAuthCode() {

        binding.etAuthCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                try {
                    viewModel.checkEmailCode(
                        "${binding.etUnivMailHeader.text}@${binding.etUnivMailBody.text}",
                        binding.etAuthCode.text.toString().toInt()
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun setNextButton() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_univAuthFragment_to_completeTeacherProfileFragment)
        }
    }

    private fun observe() {
        observeSendMailResult()
        observeCheckEmailResult()
    }

    private fun observeSendMailResult() {
        viewModel.sendEmailResult.observe(viewLifecycleOwner) {
            if (it) {
                binding.btnSendMail.setEnabledAndChangeColor(false)
            } else {
                Util.createToast(requireActivity(), "올바른 학교 메일을 입력해주세요.").show()
            }
        }
    }

    private fun observeCheckEmailResult() {
        viewModel.checkEmailResult.observe(viewLifecycleOwner) {

            binding.btnNext.setEnabledAndChangeColor(it)
            if (it) {
                Util.createToast(
                    requireActivity(),
                    "인증번호가 일치합니다."
                ).show()
            }
        }
    }
}