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
import com.herewhite.sdk.CommonCallback
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
import org.softwaremaestro.presenter.classroom.item.SerializedWhiteBoardRoomInfo
import okhttp3.internal.notify
import org.json.JSONObject
import org.softwaremaestro.presenter.classroom.adapter.SceneAdapter
import org.softwaremaestro.presenter.classroom.viewmodel.ClassroomViewModel
import org.softwaremaestro.presenter.databinding.FragmentClassroomBinding
import org.softwaremaestro.presenter.student_home.adapter.LectureAdapter
import org.softwaremaestro.presenter.student_home.item.Lecture


@AndroidEntryPoint
class ClassroomFragment : Fragment() {

    private lateinit var binding: FragmentClassroomBinding

    lateinit var whiteboardView: WhiteboardView
    lateinit var sdkConfiguration: WhiteSdkConfiguration;


    private lateinit var whiteBoardInfo: SerializedWhiteBoardRoomInfo
    private lateinit var voiceInfo: SerializedWhiteBoardRoomInfo

    // Fill the App ID of your project generated on Agora Console.
    /*
        private var voice_appId = "8a2ba5d43c734e6e8645149b41e4b540"

        // Fill the channel name.
        private var channelName = "shortStudyDev"

        // Fill the temp token generated on Agora Console.
        private var voiceRoomToken =
            "007eJxTYPhXvfZyXvx5njXHTh9gW2H1Y2FRLf+vOzsybDmXX3IVv1utwGCRaJSUaJpiYpxsbmySapZqYWZiamhimWRimGqSZGpi8EBsX0pDICNDruE+RkYGCATxeRmKM/KLSoJLSlMqXVLLGBgAMkckuA=="

        // An integer that identifies the local user.
        private var voice_uid = 0

        // Track the status of your connection
        private var voice_isJoined = false*/

    // Agora engine instance
    private lateinit var agoraEngine: RtcEngine

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
    ): View? {

        binding = FragmentClassroomBinding.inflate(layoutInflater)
        if (!checkSelfPermission()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUESTED_PERMISSIONS,
                PERMISSION_REQ_ID
            );
        }
        startTimer()
        setAgora()
        //setupVoiceSDKEngine()

        return binding.root
    }


    private fun startTimer() {
        binding.chElapsedTime.base = SystemClock.elapsedRealtime()
        binding.chElapsedTime.start()
    }


    fun setTutoringArgument() {
        whiteBoardInfo =
            SerializedWhiteBoardRoomInfo(
                "Rxin0CqBEe6G57e1KJqeHw/oPircsyuDTAGMg",
                "0b4520802b0311eeb1e747d8f6d5a89a",
                "NETLESSROOM_YWs9S2NIcGQ2U1Rodlc2RXBpWCZleHBpcmVBdD0xNjkxNDg5NTU0ODU1Jm5vbmNlPTE2OTE0NTM1NTQ4NTUwMCZyb2xlPTAmc2lnPWM5MTdkMjYzNTQxOTBjMTZkZTVkMzMwNDViYjZhYzViZDJhYjUxZjA2MjI5N2ZhMjM1ZTM4YWJmODM4MzUzNTMmdXVpZD0wYjQ1MjA4MDJiMDMxMWVlYjFlNzQ3ZDhmNmQ1YTg5YQ",
                (0..100000).random().toString()
            )
        //requireActivity().intent.getSerializableExtra("whiteBoardInfo") as SerializedWhiteBoardRoomInfo
        /*
        voiceInfo =
            requireActivity().intent.getSerializableExtra("voiceInfo") as SerializedWhiteBoardRoomInfo
           */
        if (!whiteBoardInfo.uuid.isNullOrEmpty()) binding.tvTutoringId.text = "과외를 진행해주세요"
    }

    private fun setAgora() {
        joinWhiteBoard()
        //setupVoiceSDKEngine()
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
                wRoom?.memberState = memberState
                wRoom?.disableSerialization(false)
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
            agoraEngine = RtcEngine.create(config)
            joinChannel()
        } catch (e: Exception) {
            throw RuntimeException(e.toString())
        }
    }


    private fun joinChannel() {
        val options = ChannelMediaOptions()
        options.autoSubscribeAudio = true
        // Set both clients as the BROADCASTER.
        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
        // Set the channel profile as BROADCASTING.
        options.channelProfile = Constants.CHANNEL_PROFILE_LIVE_BROADCASTING

        // Join the channel with a temp token.
        // You need to specify the user ID yourself, and ensure that it is unique in the channel.
        //agoraEngine.joinChannel(voiceRoomToken, channelName, voice_uid, options)

    }


    override fun onDestroy() {
        super.onDestroy()
        whiteboardView.removeAllViews()
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
        binding.btnShowScenes.setOnClickListener {
            binding.containerSceneList.apply {
                if (visibility == View.VISIBLE) visibility = View.GONE
                else visibility = View.VISIBLE
            }
            (binding.rvSceneList.adapter as SceneAdapter).getPreview()
        }

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
        binding.btnFinish.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext()).apply {
                setTitle("과외를 종료하시겠습니까?")
                setPositiveButton("종료") { _, _ ->
                    viewModel.finishClass(whiteBoardInfo.uuid)
                }
                setNegativeButton("취소") { _, _ ->
                }
            }
            dialog.show()
        }
    }

    private fun classFinshed() {
        binding.chElapsedTime.stop()
        val dialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("과외가 종료되었습니다.")
            setPositiveButton("확인") { _, _ ->
                requireActivity().finish()
            }
        }
        dialog.show()
    }

}