package org.softwaremaestro.presenter.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.ActivityLoginBinding
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {


    private lateinit var context: Context
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}