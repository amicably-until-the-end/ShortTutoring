package org.softwaremaestro.presenter.question_upload

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.TotalCaptureResult
import android.media.ImageReader
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentQuestionCameraBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class QuestionCameraFragment : Fragment() {

    private lateinit var binding: FragmentQuestionCameraBinding

    private lateinit var mSurfaceViewHolder: SurfaceHolder
    private lateinit var mCameraDevice: CameraDevice
    private lateinit var mPreviewBuilder: CaptureRequest.Builder
    private lateinit var mSession: CameraCaptureSession
    private lateinit var mImageReader: ImageReader


    private var mHandler: Handler? = null
    private lateinit var questionUploadActivity: QuestionUploadActivity
    private var capturedFileName: String? = null;
    var mCameraId = "0"


    override fun onAttach(context: Context) {
        super.onAttach(context)
        questionUploadActivity = context as QuestionUploadActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPermission()
        setCaptureImageReader()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQuestionCameraBinding.inflate(inflater, container, false)
        setViewHolder()
        setShutterListener()
        return binding.root
    }

    private fun setCaptureImageReader() {
        mImageReader = ImageReader.newInstance(500, 500, ImageFormat.JPEG, 1)
        mImageReader.setOnImageAvailableListener({ imageReader ->

            Log.d("mymy", "captureimage")
            var image = imageReader?.acquireLatestImage()
            var buffer = image!!.planes[0].buffer
            var bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)

            var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())


            var file = File(questionUploadActivity.filesDir, "$timeStamp.jpg")


            var opStream = FileOutputStream(file)

            opStream.write(bytes)
            opStream.close()
            image.close()
            capturedFileName = "$timeStamp.jpg"

        }, mHandler)
    }

    private fun setViewHolder() {
        mSurfaceViewHolder = binding.surfaceView.holder
        mSurfaceViewHolder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(p0: SurfaceHolder) {
                initCameraPreview()
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                mCameraDevice.close()
            }

        })
    }

    private fun setShutterListener() {
        binding.btnShutter.setOnClickListener {
            takePicture()
        }
    }

    private fun takePicture() {
        mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        mPreviewBuilder.addTarget(mImageReader.surface)
        mSession.capture(
            mPreviewBuilder.build(),
            object : CameraCaptureSession.CaptureCallback() {
                override fun onCaptureCompleted(
                    session: CameraCaptureSession,
                    request: CaptureRequest,
                    result: TotalCaptureResult
                ) {
                    super.onCaptureCompleted(session, request, result)

                    Log.d("mymym", capturedFileName!!)

                    val bundle = bundleOf("fileName" to capturedFileName!!)
                    //캡처가 끝나면 navigate
                    findNavController().navigate(
                        R.id.action_questionCameraFragment_to_questionFormFragment,
                        bundle
                    )
                }
            },
            null
        )
    }


    private fun initCameraPreview() {

        // make handler for async image retrieve from os
        val handlerThread = HandlerThread("CAMERA2")
        handlerThread.start()
        mHandler = Handler(handlerThread.looper)

        // camera service 실행, service에 handler, cameraId, device callback 이 들어감
        openCamera()

        //카메라에서 가져온 image를 담는 객체 생성

    }

    @SuppressLint("MissingPermission")
    private fun openCamera() {
        try {
            val mCameraManager =
                questionUploadActivity.getSystemService(Context.CAMERA_SERVICE) as CameraManager

            if (ActivityCompat.checkSelfPermission(
                    questionUploadActivity,
                    Manifest.permission.CAMERA
                )
                != PackageManager.PERMISSION_GRANTED
            ) return

            mCameraManager.openCamera(mCameraId, deviceStateCallback, mHandler)
        } catch (e: CameraAccessException) {
        }
    }


    private val deviceStateCallback = object : CameraDevice.StateCallback() {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        override fun onOpened(camera: CameraDevice) {
            mCameraDevice = camera
            try {
                takePreview()
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

        override fun onDisconnected(camera: CameraDevice) {
            mCameraDevice.close()
        }

        override fun onError(camera: CameraDevice, error: Int) {
            Toast.makeText(questionUploadActivity, "카메라를 열지 못했습니다.", Toast.LENGTH_SHORT);
        }
    }

    fun takePreview() {
        try {
            //실제로 하드웨어 카메라 이미지를 요청하는 부분
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            //ui-surface를 target으로 지정해줌
            mPreviewBuilder.addTarget(mSurfaceViewHolder.surface)


            mCameraDevice.createCaptureSession(
                listOf(mSurfaceViewHolder.surface, mImageReader.surface),
                mSessionPreviewStateCallback,
                mHandler
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private val mSessionPreviewStateCallback = object : CameraCaptureSession.StateCallback() {

        //매 프레임 마다 호출되는 콜백
        override fun onConfigured(session: CameraCaptureSession) {
            mSession = session
            try {
                mSession.setRepeatingRequest(mPreviewBuilder.build(), null, mHandler)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            Toast.makeText(questionUploadActivity, "fail to get frame", Toast.LENGTH_SHORT).show()
        }
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