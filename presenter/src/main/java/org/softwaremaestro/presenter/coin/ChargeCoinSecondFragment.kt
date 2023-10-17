package org.softwaremaestro.presenter.coin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.coin.viewModel.CoinViewModel
import org.softwaremaestro.presenter.databinding.FragmentChargeCoinSecondBinding
import org.softwaremaestro.presenter.student_home.viewmodel.MyProfileViewModel
import org.softwaremaestro.presenter.util.widget.SimpleAlertDialog
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


        binding.btnChargeCoin.setOnClickListener {
            if (chargeable) {
                selectedContainer?.let {
                    when (containers.indexOf(selectedContainer)) {
                        // 무료 충전 버튼
                        0 -> {
                            coinViewModel.receiveCoinFree()
                        }
                        // 1개 충전 버튼, 5개 충전 버튼
                        else -> {
                            SimpleAlertDialog().apply {
                                title = "아직 지원하지 않는 기능입니다"
                                description = "12월 이후 서비스가 정규 운영될 예정입니다"
                            }.show(parentFragmentManager, "unimplemented function")
                            // UI 업데이트
                            listOf(
                                binding.ivRightArrow,
                                binding.cbMyCoinAfterCharge
                            ).forEach {
                                it.visibility = View.INVISIBLE
                            }
                            resetContainers()
                            chargeable = false
                            coinViewModel.resetCoinFreeReceiveState()
                        }
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
        myProfileViewModel.amount.observe(viewLifecycleOwner) {
            binding.cbMyCoin.coin = it * 100
        }
    }

    private fun observeCoinFreeReceiveState() {
        coinViewModel.coinFreeReceiveState.observe(viewLifecycleOwner) {
            it ?: return@observe

            // 무료 코인 받기 성공
            if (it) {
                SimpleAlertDialog().apply {
                    title = "오늘의 코인을 받았습니다"
                    description =
                        "현재 ${(myProfileViewModel.amount.value!! + 2) * 100}개의 코인을 보유하고 있어요"
                }.show(parentFragmentManager, "receive free coin success")
                // 코인을 업데이트하기 위해 getMyProfile() 호출
                myProfileViewModel.getMyProfile()

            } else {
                SimpleAlertDialog().apply {
                    title = "이미 오늘의 코인을 받았습니다"
                    description = "기본 코인은 매일 200개씩 제공돼요"
                }.show(parentFragmentManager, "receive free coin fail")
            }
            // UI 업데이트
            listOf(
                binding.ivRightArrow,
                binding.cbMyCoinAfterCharge
            ).forEach {
                it.visibility = View.INVISIBLE
            }
            resetContainers()
            chargeable = false
            coinViewModel.resetCoinFreeReceiveState()
        }
    }
}