package org.softwaremaestro.shorttutoring

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.login.LoginActivity
import org.softwaremaestro.presenter.question_upload.QuestionUploadActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_next).setOnClickListener {
            val intent = Intent(this, QuestionUploadActivity::class.java)
            startActivity(intent)
        }
    }
}