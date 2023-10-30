package org.softwaremaestro.presenter.question_upload.question_normal_upload.widget

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.os.Handler
import android.os.HandlerThread
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.softwaremaestro.presenter.util.Util


class CameraTextureView(var mContext: Context, attrs: AttributeSet?) :
    TextureView(mContext, attrs) {
    private val REQUEST_CAMERA = 1
    var cameraDevice: CameraDevice? = null
    var previewSize: Size? = null

    private var surfaceTextureListener: SurfaceTextureListener = object : SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(
            surfaceTexture: SurfaceTexture,
            width: Int,
            height: Int
        ) {
            val cameraManager = mContext.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val cameraId = getBackFacingCameraId(cameraManager)
            if (cameraId == null) {
                Log.e("mymymy", "Fail to get camera info")
                previewSize = null
                return
            }
            previewSize = openCamera(cameraManager, cameraId)
        }

        override fun onSurfaceTextureSizeChanged(
            surfaceTexture: SurfaceTexture,
            width: Int,
            height: Int
        ) {
        }

        override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
            return false
        }

        override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {}
    }
    private var callback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            startPreview(previewSize)
        }

        override fun onDisconnected(cameraDevice: CameraDevice) {}
        override fun onError(cameraDevice: CameraDevice, i: Int) {}
    }

    init {
        setSurfaceTextureListener(surfaceTextureListener)
    }

    fun openCamera(cameraManager: CameraManager, cameraId: String?): Size? {
        val cameraAllowed = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
        if (cameraAllowed == PackageManager.PERMISSION_DENIED) {
            val permissionArray = arrayOf(Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(
                (mContext as Activity),
                permissionArray,
                REQUEST_CAMERA
            )
        } else {
            try {
                val cameraCharacteristics = cameraManager.getCameraCharacteristics(
                    cameraId!!
                )
                val map =
                    cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                if (map == null) {
                    Log.e(this@CameraTextureView::class.java.name, "Fail to get configuration")
                    Util.createToast(context as Activity, "Fail to open Camera!").show()
                    return null
                }
                val previewSize = map.getOutputSizes(
                    SurfaceTexture::class.java
                )[0]
                cameraManager.openCamera(cameraId, callback, null)
                return previewSize
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
        return null
    }

    private fun startPreview(previewSize: Size?) {
        if (!this.isAvailable) {
            return
        }
        val texture = this.surfaceTexture ?: return
        if (previewSize == null) return
        texture.setDefaultBufferSize(previewSize.width, previewSize.height)
        val surface = Surface(texture)
        val builder: CaptureRequest.Builder = try {
            cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
            return
        }
        builder.addTarget(surface)
        try {
            cameraDevice!!.createCaptureSession(
                listOf(surface),
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                        updatePreview(builder, cameraCaptureSession)
                    }

                    override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {}
                },
                null
            )
        } catch (e: CameraAccessException) {
            e.printStackTrace()

        }
    }

    fun updatePreview(builder: CaptureRequest.Builder, session: CameraCaptureSession) {
        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
        val thread = HandlerThread("CameraPreview")
        thread.start()
        val backgroundHandler = Handler(thread.looper)
        try {
            val matrix = configureTransform(width, height)
            setTransform(matrix)
            session.setRepeatingRequest(builder.build(), null, backgroundHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun getBackFacingCameraId(cameraManager: CameraManager): String? {
        try {
            for (cameraId in cameraManager.cameraIdList) {
                val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
                val cameraOrientation =
                    cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)!!
                if (cameraOrientation == CameraCharacteristics.LENS_FACING_BACK) return cameraId
            }
            return cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        return null
    }

    private fun configureTransform(viewWidth: Int, viewHeight: Int): Matrix {
        val matrix = Matrix()
        val viewRect = RectF(0.0f, 0.0f, viewWidth.toFloat(), viewHeight.toFloat())
        val bufferRect =
            RectF(0.0f, 0.0f, previewSize!!.height.toFloat(), previewSize!!.width.toFloat())
        val centerX = viewRect.centerX()
        val centerY = viewRect.centerY()
        bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
        matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
        val scale = Math.max(
            viewHeight.toFloat() / previewSize!!.width,
            viewWidth.toFloat() / previewSize!!.height
        )
        matrix.postScale(scale, scale, centerX, centerY)
        return matrix
    }

    fun capture(): Bitmap {
        val bitmap = bitmap
        val width = (width * cropWidthPercent).toInt()
        val height = (width * cropAspectRatio).toInt()
        return Bitmap.createBitmap(
            bitmap!!,
            (bitmap.width - width) / 2, (bitmap.height - height) / 2, width, height,
            getTransform(null), true
        )
    }

    companion object {
        const val cropWidthPercent = 0.8f
        const val cropAspectRatio = 1.25f
    }
}
