package org.softwaremaestro.presenter.util.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import org.softwaremaestro.presenter.R
import org.softwaremaestro.presenter.databinding.WidgetCoinBalanceBinding
import java.text.DecimalFormat

class CoinBalance(
    private val context: Context,
    private val attrs: AttributeSet?
) : ConstraintLayout(context, attrs) {

    private val binding = WidgetCoinBalanceBinding.inflate(LayoutInflater.from(context), this, true)
    private var coin = 0

    init {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.CoinBalance).apply {
                coin = getInt(R.styleable.CoinBalance_coin, 0)
                recycle()
            }
        }
    }

    fun setCoin(coin: Int) {
        binding.tvCoin.text = coinFormatter.format(coin)
    }

    companion object {
        private val coinFormatter = DecimalFormat("#,###")
    }
}