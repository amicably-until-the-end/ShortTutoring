package org.softwaremaestro.presenter.teacher_home

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import kotlinx.coroutines.Job
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.DialogWaitingBinding
import org.softwaremaestro.presenter.databinding.FragmentTeacherHomeBinding

class WaitingDialog(context: Context): Dialog(context) {

    init {
        // 상단바를 제거한다
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_waiting)

        findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            // 대화상자를 없앤다
            dismiss()
        }

        getReply()
    }

    private fun getReply() {

        // reply의 state가 success이면
        if (true) {

            // go to room
        }
        else {

        }
    }
}