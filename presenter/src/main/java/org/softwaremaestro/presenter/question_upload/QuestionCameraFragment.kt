package org.softwaremaestro.presenter.question_upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionCameraBinding
import org.softwaremaestro.presenter.question_upload.question_normal_upload.QuestionUploadActivity
import org.softwaremaestro.presenter.question_upload.question_normal_upload.adapter.CapturePreviewAdapter
import org.softwaremaestro.presenter.question_upload.question_normal_upload.viewmodel.QuestionUploadViewModel
import org.softwaremaestro.presenter.question_upload.question_selected_upload.viewmodel.QuestionSelectedUploadViewModel
import org.softwaremaestro.presenter.util.moveBack

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class QuestionCameraFragment : Fragment() {

    private lateinit var binding: FragmentQuestionCameraBinding

    private val questionUploadViewModel: QuestionUploadViewModel by activityViewModels()
    private val questionSelectedUploadViewModel: QuestionSelectedUploadViewModel by activityViewModels()

    private lateinit var previewAdapter: CapturePreviewAdapter

    private var previewSelected = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionCameraBinding.inflate(inflater, container, false)
        setShutterListener()
        setPreviewRecyclerView()
        setNextButton()
        setCloseBtn()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionUploadViewModel._images.value?.let {
            previewAdapter.items = it.toMutableList()
            previewAdapter.notifyDataSetChanged()
        }
    }

    private fun setShutterListener() {
        binding.btnShutter.setOnClickListener {
            val image = binding.textureView.capture()
            if (previewSelected < 5) {
                previewAdapter.items.add(image)
                previewSelected += 1
            } else {
                previewAdapter.items[4] = image
            }
            previewAdapter.notifyDataSetChanged()

            binding.btnNext.apply {
                if (previewAdapter.items.isNotEmpty()) {
                    setBackgroundResource(R.drawable.bg_radius_18_grad_blue)
                    setTextColor(resources.getColor(R.color.white, null))
                } else {
                    setBackgroundResource(R.drawable.bg_radius_18_background_grey)
                    setTextColor(resources.getColor(R.color.sub_text_grey, null))
                }
            }
        }
    }


    private fun setNextButton() {
        binding.btnNext.setOnClickListener {
            if (previewAdapter.items.size > 0) {
                questionUploadViewModel._images.postValue(previewAdapter.items)
                questionSelectedUploadViewModel.setImages(previewAdapter.items)
                navigateToQuestionForm()
            }
        }
    }

    private fun setPreviewRecyclerView() {
        previewAdapter = CapturePreviewAdapter {
            if (it < previewAdapter.items.size) {
                previewAdapter.items.removeAt(it)
                previewSelected -= 1
            }
            previewAdapter.notifyDataSetChanged()

            binding.btnNext.apply {
                if (previewAdapter.items.isNotEmpty()) {
                    setBackgroundResource(R.drawable.bg_radius_18_grad_blue)
                    setTextColor(resources.getColor(R.color.white, null))
                } else {
                    setBackgroundResource(R.drawable.bg_radius_18_background_grey)
                    setTextColor(resources.getColor(R.color.sub_text_grey, null))
                }
            }
        }

        binding.rvCapturePreview.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = previewAdapter
        }
    }

    private fun setCloseBtn() {
        binding.btnClose.setOnClickListener {
            moveBack()
        }
    }

    private fun navigateToQuestionForm() {
        findNavController().navigate(
            when (requireActivity()) {
                // 일반 질문
                is QuestionUploadActivity -> R.id.action_questionCameraFragment_to_questionNormalFormFragment
                // 지정 질문
                else -> R.id.action_questionCameraFragment_to_questionSelectedFormFragment
            }

        )
    }
}