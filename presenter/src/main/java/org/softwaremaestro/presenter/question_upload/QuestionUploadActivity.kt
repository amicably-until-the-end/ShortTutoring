package org.softwaremaestro.presenter.question_upload

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.ActivityQuestionUploadBinding

@AndroidEntryPoint
class QuestionUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionUploadBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAppBar()
    }

    private fun setAppBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController
        binding.toolbar.setupWithNavController(navHostFragment.navController)
        setSupportActionBar(binding.toolbar!!)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                return navController.popBackStack() || super.onOptionsItemSelected(item)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}