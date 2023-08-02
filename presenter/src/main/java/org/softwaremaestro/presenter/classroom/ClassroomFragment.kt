package org.softwaremaestro.presenter.classroom

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.herewhite.sdk.Room
import com.herewhite.sdk.RoomParams
import com.herewhite.sdk.WhiteSdk
import com.herewhite.sdk.WhiteSdkConfiguration
import com.herewhite.sdk.WhiteboardView
import com.herewhite.sdk.domain.MemberState
import com.herewhite.sdk.domain.Promise
import com.herewhite.sdk.domain.Region
import com.herewhite.sdk.domain.SDKError
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import org.softwaremaestro.presenter.classroom.item.SerializedWhiteBoardRoomInfo
import org.softwaremaestro.presenter.databinding.FragmentClassroomBinding


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

        setAgora()
        //setupVoiceSDKEngine()

        return binding.root
    }


    fun setTutoringArgument() {
        whiteBoardInfo =
            requireActivity().intent.getSerializableExtra("whiteBoardInfo") as SerializedWhiteBoardRoomInfo
        Log.d("agora", whiteBoardInfo.toString())
        //voiceInfo = requireActivity().intent.getSerializableExtra("voiceInfo") as SerializedWhiteBoardRoomInfo
        if (!whiteBoardInfo.uuid.isNullOrEmpty()) binding.tvTutoringId.text = "과외를 진행해주세요"
    }

    private fun setAgora() {
        setTutoringArgument()
        setWhiteBoard()
        setColorButtons()
        //setupVoiceSDKEngine()
    }

    private fun setWhiteBoard() {
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

            }

            override fun catchEx(t: SDKError?) {
                Log.i("agora", t.toString())
                Toast.makeText(requireContext(), "화이트보드 서버 접속 실패", Toast.LENGTH_SHORT).show()
            }
        }
        var roomParams =
            RoomParams(whiteBoardInfo.uuid, whiteBoardInfo.roomToken, whiteBoardInfo.uid)
        whiteSdk.joinRoom(roomParams, newPromise)

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

    private fun setColorButtons() {
        binding.btnColorPen.setOnClickListener {
            var memberState = MemberState()
            memberState.currentApplianceName = "pencil"
            memberState.strokeColor = IntArray(3) { 255;0;0; }
            whiteBoardRoom?.memberState = memberState
        }
        binding.btnColorErase.setOnClickListener {
            var memberState = MemberState()
            memberState.currentApplianceName = "eraser"
            whiteBoardRoom?.memberState = memberState
        }
    }


}