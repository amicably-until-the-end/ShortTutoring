package org.softwaremaestro.presenter.classroom

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
import org.softwaremaestro.presenter.classroom.adapter.SceneAdapter
import org.softwaremaestro.presenter.classroom.item.SerializedVoiceRoomInfo
import org.softwaremaestro.presenter.classroom.item.SerializedWhiteBoardRoomInfo
import org.softwaremaestro.presenter.classroom.viewmodel.ClassroomViewModel
import org.softwaremaestro.presenter.databinding.FragmentClassroomBinding
import org.softwaremaestro.presenter.util.widget.SimpleYesOrNoDialog


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


    // Room State
    private var isMicOn = true
    private var isProblemImgUploaded = false


    private var whiteBoardRoom: Room? = null

    private val viewModel: ClassroomViewModel by viewModels()

    private val PERMISSION_REQ_ID = 22
    private val REQUESTED_PERMISSIONS = arrayOf<String>(
        Manifest.permission.RECORD_AUDIO
    )

    private fun checkSelfPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            REQUESTED_PERMISSIONS[0]
        ) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentClassroomBinding.inflate(layoutInflater)
        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUESTED_PERMISSIONS,
                PERMISSION_REQ_ID
            )
        }
        startTimer()
        setAgora()

        return binding.root
    }


    private fun startTimer() {
        binding.chElapsedTime.base = SystemClock.elapsedRealtime()
        binding.chElapsedTime.start()
    }


    fun setTutoringArgument() {
        whiteBoardInfo =
            requireActivity().intent.getSerializableExtra("whiteBoardInfo") as SerializedWhiteBoardRoomInfo
        voiceInfo =
            requireActivity().intent.getSerializableExtra("voiceRoomInfo") as SerializedVoiceRoomInfo
//        if (!whiteBoardInfo.uuid.isNullOrEmpty()) binding.tvTutoringId.text = "과외를 진행해주세요"
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
    }


    private fun joinWhiteBoard() {
        setTutoringArgument()
        sdkConfiguration = WhiteSdkConfiguration(whiteBoardInfo.appId, true)
        sdkConfiguration.region = Region.us
        whiteboardView = binding.white

        var whiteSdk = WhiteSdk(whiteboardView, requireContext(), sdkConfiguration)
        var newPromise = object : Promise<Room> {
            override fun then(wRoom: Room?) {
                whiteBoardRoom = wRoom!!
                var memberState = MemberState()
                memberState.currentApplianceName = "pencil"
                memberState.strokeColor = IntArray(3) { 255;0;0; }
                wRoom.memberState = memberState
                wRoom.disableSerialization(false)
                setWhiteBoard()


            }

            override fun catchEx(t: SDKError?) {
                Log.i("agora", t.toString())
                Toast.makeText(requireContext(), "화이트보드 서버 접속 실패", Toast.LENGTH_SHORT).show()
            }
        }
        var roomListener = object : RoomListener {
            override fun onPhaseChanged(phase: RoomPhase?) {
                if (phase == RoomPhase.disconnected) {
                    classFinshed()
                }
            }

            override fun onDisconnectWithError(e: java.lang.Exception?) {
                //TODO("Not yet implemented")
            }

            override fun onKickedWithReason(reason: String?) {
                //TODO("Not yet implemented")
            }

            override fun onRoomStateChanged(modifyState: RoomState?) {
                //TODO("Not yet implemented")
            }

            override fun onCanUndoStepsUpdate(canUndoSteps: Long) {
                //TODO("Not yet implemented")
            }

            override fun onCanRedoStepsUpdate(canRedoSteps: Long) {
                //TODO("Not yet implemented")
            }

            override fun onCatchErrorWhenAppendFrame(userId: Long, error: java.lang.Exception?) {
                //TODO("Not yet implemented")
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


    override fun onDestroy() {
        super.onDestroy()
        whiteboardView.removeAllViews()
        voiceEngine.leaveChannel()
        whiteboardView.destroy()
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
            showMessage("Remote user joined the channel, uid: " + uid + " elapsed: " + elapsed)
        }

        override fun onJoinChannelSuccess(channel: String, uid: Int, elapsed: Int) {
            // Successfully joined a channel
            showMessage("Joined Channel $channel")
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            // Listen for remote users leaving the channel
            showMessage("Remote user offline $uid $reason")
        }

        override fun onLeaveChannel(stats: RtcStats) {
            // Listen for the local user leaving the channel
            showMessage("onLeaveChannel")
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

    private fun setPenButtons() {
        binding.btnColorPen.setOnClickListener {
            var memberState = MemberState()
            memberState.currentApplianceName = "pencil"
            memberState.strokeColor = IntArray(3) { 255;0;0; }
            whiteBoardRoom?.memberState = memberState
        }

    }

    private fun setEraseButton() {
        binding.btnColorErase.setOnClickListener {
            var memberState = MemberState()
            memberState.currentApplianceName = "eraser"
            whiteBoardRoom?.memberState = memberState
        }
    }

    private fun setImageButton() {
        binding.btnGetImage.setOnClickListener {
            whiteBoardRoom?.cleanScene(true)
            val imageInfo = ImageInformationWithUrl(
                0.0, 0.0, 200.0, 200.0,
                "https://4.bp.blogspot.com/-TTRkTOT6oRY/WiA37N0gqpI/AAAAAAAAP_E/rHjroHUXRN4pMHmOkY41-yl39O0uYibAwCLcBGAs/s640/KSAT_Math_GA_Q14.JPG"
            )
            whiteBoardRoom?.insertImage(imageInfo)
        }

    }

    private fun setSelectorButton() {
        binding.btnSelector.setOnClickListener {
            var memberState = MemberState()
            memberState.currentApplianceName = "selector"
            whiteBoardRoom?.memberState = memberState
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
        binding.btnToolbarBack.setOnClickListener {
            val dialogLectureEnd = SimpleYesOrNoDialog("수업을 종료할까요?", "과외 영상이 자동으로 저장됩니다") {
                viewModel.finishClass(voiceInfo.channelId)
                activity?.finish()
            }
            dialogLectureEnd.show(requireActivity().supportFragmentManager, "lectureEnd")
        }
    }

    private fun classFinshed() {
        binding.chElapsedTime.stop()
        val dialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("과외가 종료되었습니다.")
            setPositiveButton("확인") { _, _ ->
                voiceEngine.leaveChannel()
                requireActivity().finish()
            }
        }
        dialog.show()
    }

    private fun setMicToggleButton() {

        /*binding.btnMic.setOnClickListener {
            if (!isMicOn) {
                isMicOn = true
                voiceEngine.enableLocalAudio(true)
                Toast.makeText(requireContext(), "마이크가 켜졌습니다.", Toast.LENGTH_SHORT).show()
            } else {
                isMicOn = false
                voiceEngine.enableLocalAudio(false)
                Toast.makeText(requireContext(), "마이크가 꺼졌습니다.", Toast.LENGTH_SHORT).show()
            }
        }*/
    }


    private fun uploadProblemImg() {
        if (whiteBoardInfo.uid == "1" && !isProblemImgUploaded) {
            val imgUrl = requireActivity().intent.getStringExtra("problemImgUrl")
            val imageInfo = ImageInformationWithUrl(0.0, 0.0, 400.0, 400.0, imgUrl)
            whiteBoardRoom?.insertImage(imageInfo)
            isProblemImgUploaded = true
        }
    }

    companion object {
        const val RTC_TEACHER_UID = 1
        const val RTC_STUDENT_UID = 2
    }

}