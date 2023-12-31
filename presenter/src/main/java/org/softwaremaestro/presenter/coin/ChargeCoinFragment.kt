package org.softwaremaestro.presenter.coin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.FragmentChargeCoinBinding

class ChargeCoinFragment : Fragment() {

    private lateinit var binding: FragmentChargeCoinBinding
    private lateinit var containers: List<View>
    private var selectedContainer: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChargeCoinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initContainers()
        setContainers()
        setToolbar()
        setCoin()

        binding.btnChargeCoin.setOnClickListener {
            selectedContainer?.let {
                when (containers.indexOf(selectedContainer)) {
                    // 무료 충전 버튼
                    0 -> {}
                    // 1개 충전 버튼
                    1 -> {}
                    // 5개 충전 버튼
                    2 -> {}
                }
            }
        }
    }

    private fun initContainers() {
        containers = listOf(
            binding.containerFreeCharge,
            binding.containerChargeOne,
            binding.containerChargeFive
        )
    }

    private fun setContainers() {
        containers.forEach {
            it.setOnClickListener {
                resetContainers()
                selectContainer(it)
                setBtnChargeCoinEnabled()
            }
        }
    }

    private fun setBtnChargeCoinEnabled() {
        binding.btnChargeCoin.apply {
            isEnabled = true
            background = resources.getDrawable(R.drawable.bg_radius_5_grad_blue, null)
            setTextColor(resources.getColor(R.color.white, null))
        }
    }

    private fun resetContainers() {
        containers.forEach { container ->
            container.apply {
                setBackgroundResource(R.drawable.bg_radius_5_stroke_background_light_blue)
            }
        }
    }

    private fun selectContainer(view: View) {
        view.setBackgroundResource(R.drawable.bg_radius_5_light_blue_and_stroke_blue)
        selectedContainer = view
        setMyCoinAfterCharge()
    }

    private fun setMyCoinAfterCharge() {
        listOf(
            binding.ivRightArrow,
            binding.cbMyCoinAfterCharge
        ).forEach {
            it.visibility = View.VISIBLE
        }

        val increment = selectedContainer?.let {
            when (containers.indexOf(it)) {
                // 무료 충전 버튼
                0 -> 200
                // 1개 충전 버튼
                1 -> 100
                // 5개 충전 버튼
                2 -> 500
                else -> null
            }
        }

        increment?.let {
            binding.cbMyCoinAfterCharge.coin = binding.cbMyCoin.coin + it
        }
    }

    private fun setToolbar() {
        binding.btnToolbarBack.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setCoin() {
        binding.cbMyCoin.coin = requireActivity().intent.getIntExtra("my-coin", 0)
    }
}