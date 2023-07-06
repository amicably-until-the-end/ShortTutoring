package org.softwaremaestro.shorttutoring

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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

        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar_main).also {
            setSupportActionBar(it)
        }

        val actionBar = supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}