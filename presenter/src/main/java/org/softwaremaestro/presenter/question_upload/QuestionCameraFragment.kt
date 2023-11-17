package org.softwaremaestro.presenter.question_upload

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionCameraBinding
import org.softwaremaestro.presenter.question_upload.question_normal_upload.QuestionUploadActivity
import org.softwaremaestro.presenter.question_upload.question_normal_upload.adapter.CapturePreviewAdapter
import org.softwaremaestro.presenter.question_upload.question_normal_upload.viewmodel.QuestionUploadViewModel
import org.softwaremaestro.presenter.question_upload.question_selected_upload.viewmodel.QuestionSelectedUploadViewModel
import org.softwaremaestro.presenter.util.Util
import org.softwaremaestro.presenter.util.moveBack
import java.lang.Integer.min

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
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
        setGalleryButton()
        setCloseBtn()
        observeGalleryBitmap()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        questionUploadViewModel._images.value?.let {
            previewAdapter.items = it.toMutableList()
            previewAdapter.notifyDataSetChanged()
        }
    }

    private fun observeGalleryBitmap() {
        questionUploadViewModel.galleryBitmap.observe(viewLifecycleOwner) {
            Log.d("QuestionUploadViewModel", "observeGalleryBitmap: $it")
            previewAdapter.items = it.toMutableList()
            previewAdapter.notifyDataSetChanged()
            checkImagePresent()
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

            checkImagePresent()
        }
    }

    private fun checkImagePresent() {
        if (previewAdapter.items.isNotEmpty()) {
            binding.btnNext.setBackgroundResource(R.drawable.bg_radius_18_grad_blue)
            binding.btnNext.setTextColor(resources.getColor(R.color.white, null))
        } else {
            binding.btnNext.setBackgroundResource(R.drawable.bg_radius_18_background_grey)
            binding.btnNext.setTextColor(resources.getColor(R.color.sub_text_grey, null))
        }
    }


    private fun setNextButton() {
        binding.btnNext.setOnClickListener {
            if (previewAdapter.items.size > 0) {
                questionUploadViewModel._images.postValue(previewAdapter.items)
                questionSelectedUploadViewModel.setImages(previewAdapter.items)
                navigateToQuestionFormAfter(200L)
                disableNextButtonFor(500L)
            } else {
                Util.createToast(requireActivity(), "문제 사진을 촬영해주세요").show()
            }
        }
    }

    private fun setGalleryButton() {
        binding.btnGallery.setOnClickListener {
            questionUploadViewModel._galleryBitmap.postValue(mutableListOf())
            //get images from gallery
            openGallery()
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        pickImages.launch(galleryIntent)
    }

    private val pickImages =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("QuestionUploadViewModel", "openGallery: success ${result.data}")
                result.data?.let { data ->
                    val clipData = data.clipData

                    if (clipData != null) {
                        if (clipData.itemCount > 5) {
                            Util.createToast(requireActivity(), "사진은 최대 5장까지 선택 가능합니다").show()
                        }
                        // Multiple images selected
                        (0 until min(clipData.itemCount, 5)).map { i ->
                            clipData.getItemAt(i).uri
                        }.let {
                            handleSelectedImage(it)
                        }
                    } else {
                        // Single image selected
                        val uri: Uri = data.data!!
                        handleSelectedImage(listOf(uri))
                    }
                }
            } else {
                Log.d("QuestionUploadViewModel", "openGallery: fail")
            }
        }

    private fun handleSelectedImage(uris: List<Uri>) {
        Log.d("QuestionUploadViewModel", "handleSelectedImage: $uris")
        questionUploadViewModel.getBitmapFromUri(requireActivity().contentResolver, uris)
    }

    private fun disableNextButtonFor(l: Long) {
        binding.btnNext.backgroundTintList =
            resources.getColorStateList(R.color.background_light_blue, null)
        binding.btnNext.setTextColor(resources.getColor(R.color.primary_blue, null))

        Handler(Looper.getMainLooper()).postDelayed({
            binding.btnNext.backgroundTintList =
                resources.getColorStateList(R.color.primary_blue, null)
            binding.btnNext.setTextColor(resources.getColor(R.color.white, null))
        }, l)
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

    private fun navigateToQuestionFormAfter(l: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(
                when (requireActivity()) {
                    // 일반 질문
                    is QuestionUploadActivity -> R.id.action_questionCameraFragment_to_questionNormalFormFragment
                    // 지정 질문
                    else -> R.id.action_questionCameraFragment_to_questionSelectedFormFragment
                }

            )
        }, l)
    }
}