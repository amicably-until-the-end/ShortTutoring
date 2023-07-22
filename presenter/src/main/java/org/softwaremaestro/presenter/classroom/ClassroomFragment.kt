package org.softwaremaestro.presenter.classroom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.herewhite.sdk.Room
import com.herewhite.sdk.RoomParams
import com.herewhite.sdk.WhiteSdk
import com.herewhite.sdk.WhiteSdkConfiguration
import com.herewhite.sdk.WhiteboardView
import com.herewhite.sdk.domain.MemberState
import com.herewhite.sdk.domain.Promise
import com.herewhite.sdk.domain.Region
import com.herewhite.sdk.domain.SDKError
import org.softwaremaestro.presenter.R
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentClassroomBinding.inflate(layoutInflater)
        setWhiteBoard()
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

    override fun onDestroy() {
        super.onDestroy()
        whiteboardView.removeAllViews()
        whiteboardView.destroy()
    }
}