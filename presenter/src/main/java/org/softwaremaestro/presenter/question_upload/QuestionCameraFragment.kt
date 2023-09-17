package org.softwaremaestro.presenter.question_upload

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionCameraBinding
import org.softwaremaestro.presenter.question_upload.adapter.CapturePreviewAdapter
import org.softwaremaestro.presenter.question_upload.viewmodel.QuestionUploadViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class QuestionCameraFragment : Fragment() {

    private lateinit var binding: FragmentQuestionCameraBinding


    private lateinit var questionUploadActivity: QuestionUploadActivity


    private val viewModel: QuestionUploadViewModel by activityViewModels()

    private lateinit var previewAdapter: CapturePreviewAdapter

    private var previewSelected = 0


    override fun onAttach(context: Context) {
        super.onAttach(context)
        questionUploadActivity = context as QuestionUploadActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionCameraBinding.inflate(inflater, container, false)
        setShutterListener()
        setPreviewRecyclerView()
        setNextButton()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel._images.value?.let {
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
        }
    }


    private fun setNextButton() {
        binding.btnNext.setOnClickListener {
            if (previewAdapter.items.size > 0) {
                viewModel._images.postValue(previewAdapter.items)
                navigateToQuestionForm()
            }
        }
    }

    private fun setPreviewRecyclerView() {
        previewAdapter = CapturePreviewAdapter() {
            if (it < previewAdapter.items.size) {
                previewAdapter.items.removeAt(it)
                previewSelected -= 1
                previewAdapter.notifyDataSetChanged()
            }
        }

        binding.rvCapturePreview.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = previewAdapter
        }

    }

    private fun navigateToQuestionForm() {
        findNavController().navigate(R.id.action_questionCameraFragment_to_questionFormFragment)
    }


}