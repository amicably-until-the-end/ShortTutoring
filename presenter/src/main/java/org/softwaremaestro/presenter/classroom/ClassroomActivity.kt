package org.softwaremaestro.presenter.classroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ActivityClassroomBinding


@AndroidEntryPoint
class ClassroomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassroomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassroomBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    companion object {
        const val EXTRA_WHITE_BOARD_INFO = "whiteBoardInfo"
        const val EXTRA_VOICE_ROOM_INFO = "voiceRoomInfo"

    }
}