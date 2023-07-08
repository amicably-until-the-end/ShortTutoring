package org.softwaremaestro.presenter.teacher_home

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Button
import org.softwaremaestro.presenter.R

class WaitingDialog(context: Context): Dialog(context) {

    init {
        // 상단바를 제거한다
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_waiting)

        findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dismiss()
        }
    }
}