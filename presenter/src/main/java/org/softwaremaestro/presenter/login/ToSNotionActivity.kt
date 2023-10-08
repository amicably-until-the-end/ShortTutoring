package org.softwaremaestro.presenter.login

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import org.softwaremaestro.presenter.R

class ToSNotionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tos_notion)

        val tosWebView = findViewById<WebView>(R.id.wv_tos)
        tosWebView.loadUrl(TOS_URL)
    }

    companion object {
        private const val TOS_URL = "file:///android_asset/www/tos_notion.html"
    }
}