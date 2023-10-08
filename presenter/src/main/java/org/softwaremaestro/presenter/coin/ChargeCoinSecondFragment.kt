package org.softwaremaestro.presenter.coin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.coin.viewModel.CoinViewModel
import org.softwaremaestro.presenter.databinding.FragmentChargeCoinSecondBinding
import org.softwaremaestro.presenter.teacher_home.viewmodel.MyProfileViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ChargeCoinSecondFragment : Fragment() {

    private lateinit var binding: FragmentChargeCoinSecondBinding
    private val coinViewModel: CoinViewModel by activityViewModels()
    private val myProfileViewModel: MyProfileViewModel by activityViewModels()
    private lateinit var containers: List<View>
    private var selectedContainer: View? = null
    private var chargeable = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChargeCoinSecondBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initContainers()
        setContainers()
        getRemoteDate()
        setObserver()


        // Todo: 코인 구매 api 연동하기
        binding.btnChargeCoin.setOnClickListener {
            if (chargeable) {
                selectedContainer?.let {
                    when (containers.indexOf(selectedContainer)) {
                        // 무료 충전 버튼
                        0 -> {
                            coinViewModel.receiveCoinFree()
                        }
                        // 1개 충전 버튼
                        1 -> {}
                        // 5개 충전 버튼
                        else -> {}
                    }
                }
            } else {
                Toast.makeText(requireContext(), "구매할 코인을 선택해주세요", Toast.LENGTH_SHORT).show()
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
        binding.tvDetailFreeCoin.text =
            "${LocalDate.now().format(DateTimeFormatter.ofPattern(" M/d "))} 기본 제공"
    }

    private fun setBtnChargeCoinEnabled() {
        binding.btnChargeCoin.apply {
            chargeable = true
            background = resources.getDrawable(R.drawable.bg_radius_100_grad_blue, null)
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

    private fun getRemoteDate() {
        myProfileViewModel.getMyProfile()
    }

    private fun setObserver() {
        observeCoin()
        observeCoinFreeReceiveState()
    }

    private fun observeCoin() {
        myProfileViewModel.myProfile.observe(viewLifecycleOwner) {
            it.amount?.let { binding.cbMyCoin.coin = it * 100 }
        }
    }

    private fun observeCoinFreeReceiveState() {
        coinViewModel.coinFreeReceiveState.observe(viewLifecycleOwner) {
            it?.let {
                // 무료 코인 받기 성공
                if (it) {
                    Toast.makeText(requireContext(), "오늘의 코인을 받았습니다", Toast.LENGTH_SHORT).show()
                    // 코인을 업데이트하기 위해 getMyProfile() 호출
                    myProfileViewModel.getMyProfile()
                } else {
                    Log.d("hhcc", "failed")
                    Toast.makeText(requireContext(), "이미 오늘의 코인을 받았습니다.", Toast.LENGTH_SHORT).show()
                }
                // UI 업데이트
                listOf(
                    binding.ivRightArrow,
                    binding.cbMyCoinAfterCharge
                ).forEach {
                    it.visibility = View.INVISIBLE
                }
                chargeable = false
                binding.btnChargeCoin.setBackgroundResource(R.drawable.bg_radius_100_background_grey)
                binding.btnChargeCoin.setTextColor(resources.getColor(R.color.sub_text_grey, null))
            }
        }
    }
}