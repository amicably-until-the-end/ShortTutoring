package org.softwaremaestro.presenter.question_upload

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ActivityLoginBinding
import org.softwaremaestro.presenter.databinding.ActivityQuestionUploadBinding

@AndroidEntryPoint
class QuestionUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAppBar()

    }

    private fun setAppBar() {
        val toolbar: Toolbar = binding.toolbar
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        toolbar.setupWithNavController(navHostFragment.navController)
        if (toolbar != null)
            setSupportActionBar(toolbar)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }


}