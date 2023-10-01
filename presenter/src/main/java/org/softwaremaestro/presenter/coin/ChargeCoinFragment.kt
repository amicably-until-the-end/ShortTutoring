package org.softwaremaestro.presenter.coin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.softwaremaestro.presenter.databinding.FragmentChargeCoinBinding

class ChargeCoinFragment : Fragment() {

    private lateinit var binding: FragmentChargeCoinBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChargeCoinBinding.inflate(layoutInflater)
        return binding.root
    }

}