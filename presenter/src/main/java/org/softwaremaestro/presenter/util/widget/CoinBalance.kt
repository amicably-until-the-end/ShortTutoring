package org.softwaremaestro.presenter.util.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import org.softwaremaestro.presenter.databinding.WidgetCoinBalanceBinding
import java.text.DecimalFormat

class CoinBalance(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val binding = WidgetCoinBalanceBinding.inflate(LayoutInflater.from(context), this, true)
    private var _coin = 0

    // Todo: coin에 대한 setter를 감춰야 할듯
    var coin: Int
        get() = _coin
        set(value) {
            _coin = value
            binding.tvCoin.text = coinFormatter.format(_coin)
        }

    companion object {
        private val coinFormatter = DecimalFormat("#,###")
    }
}