package org.softwaremaestro.presenter.classroom

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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
import org.softwaremaestro.presenter.databinding.FragmentClassroomBinding


class ClassroomFragment : Fragment() {

    private lateinit var binding: FragmentClassroomBinding

    val appId = "ieDZoBo0Ee62CX-2JkpBKg/pl0QHB62g749Bw"
    val uuid = "962c2060289011eeb1e747d8f6d5a89a"
    val roomToken =
        "NETLESSROOM_YWs9RmJsUGdVdTFkaGZzck1mZCZleHBpcmVBdD0xNjkwMDY3MjA1MDY0Jm5vbmNlPTE2OTAwMzEyMDUwNjQwMCZyb2xlPTAmc2lnPTM5ZDgyZGE1NDI5ZDQ3ZGZiZDU2ZTdkYzllZDRlNzM4YzU3MGM1OGZmNDQ2YmNkZDg0NzQ3YTg3NDkyZTM5MGUmdXVpZD05NjJjMjA2MDI4OTAxMWVlYjFlNzQ3ZDhmNmQ1YTg5YQ"
    val uid = "j3333fzdfason"

    lateinit var whiteboardView: WhiteboardView
    var sdkConfiguration: WhiteSdkConfiguration = WhiteSdkConfiguration(appId, true)
    var roomParams = RoomParams(uuid, roomToken, uid)

    // Fill the App ID of your project generated on Agora Console.
    private val voice_appId = "8a2ba5d43c734e6e8645149b41e4b540"

    // Fill the channel name.
    private val channelName = "shortStudyDev"

    // Fill the temp token generated on Agora Console.
    private val video_token =
        "007eJxTYPhXvfZyXvx5njXHTh9gW2H1Y2FRLf+vOzsybDmXX3IVv1utwGCRaJSUaJpiYpxsbmySapZqYWZiamhimWRimGqSZGpi8EBsX0pDICNDruE+RkYGCATxeRmKM/KLSoJLSlMqXVLLGBgAMkckuA=="

    // An integer that identifies the local user.
    private val voice_uid = 0

    // Track the status of your connection
    private val voice_isJoined = false

    // Agora engine instance
    private lateinit var agoraEngine: RtcEngine

    // UI elements
    private val voice_infoText: TextView? = null
    private val voice_joinLeaveButton: Button? = null


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

        //setWhiteBoard()
        //setupVoiceSDKEngine()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTutoringId.text = requireActivity().intent.getStringExtra("tutoringId") ?: "-"
    }

    private fun setWhiteBoard() {

        sdkConfiguration.region = Region.us
        whiteboardView = binding.white
        var whiteSdk = WhiteSdk(whiteboardView, requireContext(), sdkConfiguration)
        var newPromise = object : Promise<Room> {
            override fun then(wRoom: Room?) {
                var memberState = MemberState()
                memberState.currentApplianceName = "pencil"
                memberState.strokeColor = IntArray(3) { 255;0;0; }
                wRoom?.memberState = memberState

            }

            override fun catchEx(t: SDKError?) {
                var o = t?.message
                Log.i("show Toast", o.toString())
                Toast.makeText(requireContext(), o.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        whiteSdk.joinRoom(roomParams, newPromise)

    }

    private fun setupVoiceSDKEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = requireContext()
            config.mAppId = voice_appId
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
        agoraEngine.joinChannel(video_token, channelName, voice_uid, options)

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


}