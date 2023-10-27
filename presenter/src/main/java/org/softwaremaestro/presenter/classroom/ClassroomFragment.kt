package org.softwaremaestro.presenter.classroom

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.gson.Gson
import com.herewhite.sdk.Room
import com.herewhite.sdk.RoomListener
import com.herewhite.sdk.RoomParams
import com.herewhite.sdk.WhiteSdk
import com.herewhite.sdk.WhiteSdkConfiguration
import com.herewhite.sdk.WhiteboardView
import com.herewhite.sdk.domain.ImageInformationWithUrl
import com.herewhite.sdk.domain.MemberState
import com.herewhite.sdk.domain.Promise
import com.herewhite.sdk.domain.Region
import com.herewhite.sdk.domain.RoomPhase
import com.herewhite.sdk.domain.RoomState
import com.herewhite.sdk.domain.SDKError
import dagger.hilt.android.AndroidEntryPoint
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.classroom.adapter.SceneAdapter
import org.softwaremaestro.presenter.classroom.item.SerializedVoiceRoomInfo
import org.softwaremaestro.presenter.classroom.item.SerializedWhiteBoardRoomInfo
import org.softwaremaestro.presenter.classroom.viewmodel.ClassroomViewModel
import org.softwaremaestro.presenter.classroom.widget.DialogGuideline
import org.softwaremaestro.presenter.databinding.FragmentClassroomBinding
import org.softwaremaestro.presenter.util.widget.LoadingDialog
import org.softwaremaestro.presenter.util.widget.SimpleAlertDialog
import org.softwaremaestro.presenter.util.widget.SimpleConfirmDialog


@AndroidEntryPoint
class ClassroomFragment : Fragment() {

    private lateinit var binding: FragmentClassroomBinding


    lateinit var whiteboardView: WhiteboardView


    // Agora whiteboard instance
    lateinit var sdkConfiguration: WhiteSdkConfiguration


    // Agora voice instance
    private lateinit var voiceEngine: RtcEngine

    // Channel Info
    private lateinit var whiteBoardInfo: SerializedWhiteBoardRoomInfo
    private lateinit var voiceInfo: SerializedVoiceRoomInfo

    private lateinit var dialogLectureEnd: SimpleConfirmDialog
    private lateinit var loadingDialog: LoadingDialog


    // Room State
    private var isProblemImgUploaded = false

    private var colorPallet: Dialog? = null
    private var imagePallet: Dialog? = null


    private var whiteBoardRoom: Room? = null

    private val viewModel: ClassroomViewModel by viewModels()

    private var currentApplianceName: ApplianceName = ApplianceName.PENCIL

    private val PERMISSION_REQ_ID = 22
    private val REQUESTED_PERMISSIONS = arrayOf<String>(
        Manifest.permission.RECORD_AUDIO
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClassroomBinding.inflate(layoutInflater)
        showGuideline()
        setTutoringArgument() // Extra Argument 반드시 맨 처음에 실행
        permissionCheck()
        setAgora()
        initDialog()
        setClassroomInfoUI()
        viewModel.getQuestionInfo(whiteBoardInfo.questionId)
        observeQuestionInfo()
        initLoadingDialog()
        return binding.root
    }

    private fun showGuideline() {
        DialogGuideline().show(parentFragmentManager, "dialog guideline")
    }

    private fun initDialog() {
        iniLectureEndDialog()
    }

    private fun iniLectureEndDialog() {
        dialogLectureEnd = SimpleConfirmDialog {
            viewModel.finishClass(whiteBoardInfo.tutoringId)
        }.apply {
            title = "수업을 종료할까요?"
            description = "과외 영상이 자동으로 저장됩니다"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingDialog.show()
    }

    override fun onStop() {
        super.onStop()
        Log.d("ClassroomFragment", "onStop")
        activity?.finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d("ClassroomFragment", "onDestroy")
        whiteboardView.removeAllViews()
        voiceEngine.leaveChannel()
        whiteboardView.destroy()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            onBackPressedCallback
        )
    }


    private fun permissionCheck() {
        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUESTED_PERMISSIONS,
                PERMISSION_REQ_ID
            )
        }
    }

    private fun checkSelfPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            REQUESTED_PERMISSIONS[0]
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun initLoadingDialog() {
        loadingDialog = LoadingDialog(requireContext())
    }


    private fun setClassroomInfoUI() {
        binding.tvRoomTitle.text =
            "${whiteBoardInfo.opponentName} ${if (whiteBoardInfo.isTeacher) "학생과" else "선생님과"} 수업 중"
        Glide.with(requireContext())
            .load(whiteBoardInfo.roomProfileImage)
            .into(binding.ivProfileImage)
    }

    private fun initPallet(): Dialog {
        val rect = Rect()
        binding.btnColorPen.getGlobalVisibleRect(rect)
        colorPallet = Dialog(requireContext()).apply {
            setContentView(R.layout.dialog_pallet)
            setCancelable(true)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.attributes?.gravity = Gravity.TOP or Gravity.LEFT
            window?.attributes?.x = rect.left
            window?.attributes?.y = rect.bottom
            window?.setDimAmount(0f)
            var root = findViewById<LinearLayout>(R.id.container_color_pallet)
            for (i in 0 until root.childCount) {
                root.getChildAt(i).setOnClickListener {
                    try {
                        var background = (it as MaterialCardView).cardBackgroundColor.defaultColor
                        var color = with(background) {
                            intArrayOf(Color.red(this), Color.green(this), Color.blue(this))
                        }
                        Log.d("pallet", color.toString())
                        var memberState = MemberState()
                        memberState.currentApplianceName = "pencil"
                        currentApplianceName = ApplianceName.PENCIL
                        memberState.strokeColor = color
                        whiteBoardRoom?.memberState = memberState
                    } catch (e: Exception) {
                        Log.d("pallet", e.toString())
                    }
                    dismiss()
                }
            }
        }
        return colorPallet!!
    }


    /* private fun startTimer() {
         binding.chElapsedTime.base = SystemClock.elapsedRealtime()
         binding.chElapsedTime.start()
     }*/


    fun setTutoringArgument() {
        try {
            whiteBoardInfo =
                requireActivity().intent.getSerializableExtra("whiteBoardInfo") as SerializedWhiteBoardRoomInfo
            voiceInfo =
                requireActivity().intent.getSerializableExtra("voiceRoomInfo") as SerializedVoiceRoomInfo
        } catch (e: Exception) {
            Log.e("${this@ClassroomFragment::class.java}", e.toString())
            showFinishClassDialog()
        }
    }

    private fun setAgora() {
        joinWhiteBoard()
        setupVoiceSDKEngine()
    }

    private fun setWhiteBoard() {
        //join 을 한 이후에 화이트 보드 관련 기능 세팅
        setPenButtons()
        setEraseButton()
        setImageButton()
        setSelectorButton()
        setSceneList()
        setSceneListButton()
        setRedoButton()
        setUndoButton()
        setUpFinishButton()
        //uploadProblemImg()
    }

    private fun setVoiceFunctions() {
        //join 을 한 이후에 음성 관련 기능 세팅
        setMicToggleButton()
        voiceEngine.enableLocalAudio(true)
    }


    private fun joinWhiteBoard() {
        sdkConfiguration = WhiteSdkConfiguration(whiteBoardInfo.appId, true)
        sdkConfiguration.region = Region.us
        whiteboardView = binding.white

        var whiteSdk = WhiteSdk(whiteboardView, requireContext(), sdkConfiguration)
        var newPromise = object : Promise<Room> {
            override fun then(wRoom: Room?) {
                whiteBoardRoom = wRoom!!
                var memberState = MemberState()
                memberState.currentApplianceName = ApplianceName.PENCIL.value
                memberState.strokeColor = IntArray(3) { 255;0;0; }
                wRoom.memberState = memberState
                wRoom.disableSerialization(false)
                setWhiteBoard()


            }

            override fun catchEx(t: SDKError?) {
                Toast.makeText(requireContext(), "화이트보드 서버 접속 실패", Toast.LENGTH_SHORT).show()
            }
        }
        var roomListener = object : RoomListener {
            override fun onPhaseChanged(phase: RoomPhase?) {
                if (phase == RoomPhase.disconnected) {
                    setOnlineStatus(false)
                    classFinished()
                    requireActivity().apply {
                        val mIntent = Intent().apply {
                            putExtra("opponentName", whiteBoardInfo.opponentName)
                            putExtra("tutoringId", whiteBoardInfo.tutoringId)
                            putExtra("teacherImg", whiteBoardInfo.roomProfileImage)
                        }
                        setResult(RESULT_OK, mIntent)
                    }
                }
                if (phase == RoomPhase.connected) {
                    loadingDialog.dismiss()
                }
            }

            override fun onDisconnectWithError(e: java.lang.Exception?) {
                //TODO("Not yet implemented")
            }

            override fun onKickedWithReason(reason: String?) {
                //TODO("Not yet implemented")
            }

            override fun onRoomStateChanged(modifyState: RoomState?) {
                modifyState?.roomMembers?.let {
                    val ids = it.map { member ->
                        objectToMember(member.payload)?.uid
                    }

                    Log.d("board members", ids.toString())
                    if (ids.contains(3)) {
                        setRecordingState()
                    }
                    // 1 : 학생 , 2: 선생님 모두 있으면
                    if (ids.containsAll(listOf(1, 2))) {
                        setOnlineStatus(true)
                    } else {
                        setOnlineStatus(false)
                    }
                } ?: setOnlineStatus(false)
            }

            override fun onCanUndoStepsUpdate(canUndoSteps: Long) {
                //TODO("Not yet implemented")
            }

            override fun onCanRedoStepsUpdate(canRedoSteps: Long) {
                //TODO("Not yet implemented")
            }

            override fun onCatchErrorWhenAppendFrame(userId: Long, error: java.lang.Exception?) {
                TODO("Not yet implemented")
            }
        }

        var roomParams =
            RoomParams(whiteBoardInfo.uuid, whiteBoardInfo.roomToken, whiteBoardInfo.uid)
        whiteSdk.joinRoom(roomParams, roomListener, newPromise)

    }

    private fun setupVoiceSDKEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = requireContext()
            config.mAppId = voiceInfo.appId
            config.mEventHandler = mRtcEventHandler
            voiceEngine = RtcEngine.create(config)
            joinVoiceChannel()
        } catch (e: Exception) {
            throw RuntimeException(e.toString())
        }
    }


    private fun joinVoiceChannel() {
        val options = ChannelMediaOptions()
        options.autoSubscribeAudio = true
        // Set both clients as the BROADCASTER.
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
        // Set the channel profile as BROADCASTING.
        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING

        // Join the channel with a temp token.
        // You need to specify the user ID yourself, and ensure that it is unique in the channel.
        voiceEngine.joinChannel(voiceInfo.token, voiceInfo.channelId, voiceInfo.uid, options)
        setVoiceFunctions()
    }


    fun showMessage(message: String?) {
        requireActivity().runOnUiThread {
            Toast.makeText(
                requireContext(),
                message,
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        // Listen for the remote user joining the channel.
        override fun onUserJoined(uid: Int, elapsed: Int) {
        }

        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
        }

        override fun onUserOffline(uid: Int, reason: Int) {
        }

        override fun onLeaveChannel(stats: RtcStats) {
            // Listen for the local user leaving the channel
            //showMessage("onLeaveChannel")
        }
    }

    private fun setOnlineStatus(status: Boolean) {
        if (status) {
            binding.tvOnlineStatus.text = "접속중"
            binding.tvOnlineStatus.setTextColor(resources.getColor(R.color.positive_mint))
            binding.cvOnlineStatus.setCardBackgroundColor(resources.getColor(R.color.positive_mint))
        } else {
            binding.tvOnlineStatus.text = "미접속"
            binding.tvOnlineStatus.setTextColor(resources.getColor(R.color.negative_orange))
            binding.cvOnlineStatus.setCardBackgroundColor(resources.getColor(R.color.negative_orange))
        }
    }

    private fun setSceneList() {
        binding.rvSceneList.apply {
            adapter = SceneAdapter(whiteBoardRoom!!) {
            }
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setRecordingState() {
        binding.tvRecordingStatus.apply {
            text = "녹화중"
            setTextColor(requireContext().getColor(R.color.black))
        }
        binding.cvRecordingStatus.apply {
            setCardBackgroundColor(requireContext().getColor(R.color.red))
        }

    }

    private fun observeQuestionInfo() {
        viewModel.questionInfo.observe(viewLifecycleOwner) {
            initProblemImagePallet()
            Log.d("images", viewModel.questionInfo.value?.images.toString())
        }
    }

    private fun initProblemImagePallet(): Dialog {
        val rect = Rect()
        binding.btnGetImage.getGlobalVisibleRect(rect)
        imagePallet = Dialog(requireContext()).apply {
            setContentView(R.layout.dialog_problem_image_pallet)
            setCancelable(true)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.attributes?.gravity = Gravity.TOP or Gravity.LEFT
            window?.attributes?.x = rect.left
            window?.attributes?.y = rect.bottom + 10
            window?.setDimAmount(0f)
            findViewById<AppCompatImageButton>(R.id.btn_close).setOnClickListener {
                dismiss()
            }
            var root = findViewById<LinearLayout>(R.id.container_images)
            for (i in 0 until (viewModel.questionInfo.value?.images?.size ?: 1)) {
                Log.d("images", viewModel.questionInfo.value?.images?.get(i).toString())
                root.getChildAt(i).apply {
                    try {
                        Glide.with(requireContext())
                            .load(viewModel.questionInfo.value?.images?.get(i))
                            .placeholder(R.drawable.ic_chatting_empty)
                            .into(this as ImageView)
                        setOnClickListener {
                            insertImageOnCenter(viewModel.questionInfo.value?.images?.get(i)!!)
                            imagePallet?.dismiss()
                        }
                    } catch (e: Exception) {
                        Log.e("${this@ClassroomFragment}", e.toString())
                    }
                    dismiss()
                }
            }
        }
        return imagePallet!!
    }

    private fun changeApplianceToSelector() {
        var memberState = MemberState()
        currentApplianceName = ApplianceName.SELECTOR
        memberState.currentApplianceName = ApplianceName.SELECTOR.value
        whiteBoardRoom?.memberState = memberState
        setCurrentApplianceButton(ApplianceName.SELECTOR)
    }

    private fun setPenButtons() {
        binding.btnColorPen.setOnClickListener {
            var memberState = MemberState()
            if (currentApplianceName == ApplianceName.PENCIL) {
                colorPallet?.show() ?: initPallet().show()
            }
            Log.d("pallet", "${memberState.currentApplianceName}")
            currentApplianceName = ApplianceName.PENCIL
            memberState.currentApplianceName = ApplianceName.PENCIL.value
            whiteBoardRoom?.memberState = memberState
            setCurrentApplianceButton(ApplianceName.PENCIL)
        }

    }

    private fun setEraseButton() {
        binding.btnColorErase.setOnClickListener {
            var memberState = MemberState()
            currentApplianceName = ApplianceName.ERASER
            memberState.currentApplianceName = ApplianceName.ERASER.value
            whiteBoardRoom?.memberState = memberState
            setCurrentApplianceButton(ApplianceName.ERASER)
        }
    }

    private fun setImageButton() {
        binding.btnGetImage.setOnClickListener {
            imagePallet?.show() ?: initProblemImagePallet().show()
        }

    }

    private fun insertImageOnCenter(imageUrl: String) {
        val imageInfo = ImageInformationWithUrl(
            0.0, 0.0, 200.0, 200.0,
            imageUrl
        )
        whiteBoardRoom?.insertImage(imageInfo)
    }

    private fun setSelectorButton() {
        binding.btnSelector.setOnClickListener {
            changeApplianceToSelector()

        }
    }


    private fun setSceneListButton() {
        /*binding.btnShowScenes.setOnClickListener {
            binding.containerSceneList.apply {
                if (visibility == View.VISIBLE) visibility = View.GONE
                else visibility = View.VISIBLE
            }
            (binding.rvSceneList.adapter as SceneAdapter).getPreview()
        }*/

    }

    private fun setRedoButton() {
        binding.btnRedo.setOnClickListener {
            whiteBoardRoom?.redo()
        }
    }

    private fun setUndoButton() {
        binding.btnUndo.setOnClickListener {
            whiteBoardRoom?.undo()
        }
    }

    private fun setUpFinishButton() {
        binding.btnFinishClass.setOnClickListener {
            showFinishClassDialog()
        }
    }

    private fun showFinishClassDialog() {
        dialogLectureEnd.show(requireActivity().supportFragmentManager, "lectureEnd")

    }

    private fun classFinished() {
        loadingDialog.dismiss()
        val dialog = SimpleAlertDialog().apply {
            title = "수업이 종료되었습니다."
            onDismiss = {
                activity?.finish()
            }
        }
        dialog.show(requireActivity().supportFragmentManager, "classFinished")
    }

    private fun setMicToggleButton() {

        binding.btnMic.setOnClickListener {
            with(it as ToggleButton) {
                if (isChecked) {
                    voiceEngine.enableLocalAudio(true)
                    Toast.makeText(requireContext(), "마이크가 켜졌습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    voiceEngine.enableLocalAudio(false)
                    Toast.makeText(requireContext(), "마이크가 꺼졌습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setCurrentApplianceButton(current: ApplianceName) {
        val buttons = listOf(binding.btnColorPen, binding.btnColorErase, binding.btnSelector)
        buttons.forEach {
            it.isChecked = false
        }
        when (current) {
            ApplianceName.PENCIL -> binding.btnColorPen.isChecked = true
            ApplianceName.ERASER -> binding.btnColorErase.isChecked = true
            ApplianceName.SELECTOR -> binding.btnSelector.isChecked = true
        }
    }

    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (whiteBoardRoom?.roomPhase == RoomPhase.connected)
                showFinishClassDialog()
            else {
                activity?.finish()
            }
        }
    }

    private fun objectToMember(obj: Any): MemberPayload? {
        try {
            val gson = Gson()
            return gson.fromJson(obj.toString(), MemberPayload::class.java)
        } catch (e: Exception) {
            Log.e("${this@ClassroomFragment}", e.toString())
            return null
        }
    }

    companion object {
        const val RTC_TEACHER_UID = 1
        const val RTC_STUDENT_UID = 2
    }

    enum class ApplianceName(val value: String) {
        PENCIL("pencil"),
        ERASER("eraser"),
        SELECTOR("selector"),
    }

    data class MemberPayload(
        val uid: Int,
    )


}