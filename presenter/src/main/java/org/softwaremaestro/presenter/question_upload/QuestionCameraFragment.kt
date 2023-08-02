package org.softwaremaestro.presenter.question_upload

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionCameraBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class QuestionCameraFragment : Fragment() {

    private lateinit var binding: FragmentQuestionCameraBinding


    private lateinit var questionUploadActivity: QuestionUploadActivity


    private val viewModel: QuestionUploadViewModel by activityViewModels()

    //private var capturedFileName: String? = null;
    var mCameraId = "0"


    override fun onAttach(context: Context) {
        super.onAttach(context)
        questionUploadActivity = context as QuestionUploadActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQuestionCameraBinding.inflate(inflater, container, false)
        //setViewHolder()
        setShutterListener()
        return binding.root
    }

    private fun setShutterListener() {
        binding.btnShutter.setOnClickListener {
            val image = binding.textureView.capture()
            viewModel._image.postValue(image)
            navigateToQuestionForm()
        }
    }

    private fun navigateToQuestionForm() {
        findNavController().navigate(R.id.action_questionCameraFragment_to_questionFormFragment)
    }

    private fun getPermission() {
        var permissionList = arrayOf<String>()

        if (checkSelfPermission(
                questionUploadActivity,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList += android.Manifest.permission.CAMERA
        }
        if (checkSelfPermission(
                questionUploadActivity,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList += android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        }
        if (checkSelfPermission(
                questionUploadActivity,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList += android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        ActivityCompat.requestPermissions(questionUploadActivity, permissionList, 1001);

    }


}