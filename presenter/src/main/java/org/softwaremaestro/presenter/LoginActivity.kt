package org.softwaremaestro.presenter

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.softwaremaestro.presenter.databinding.ActivityLoginBinding


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var fragmentLogin: LoginFragment
    private lateinit var fragmentRegisterRole: RegisterRoleFragment
    private lateinit var fragmentRegisterInfo: RegisterInfoFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
    }

}