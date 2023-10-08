package org.softwaremaestro.presenter.login

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import org.softwaremaestro.presenter.R

class PrivacyPolicyNotionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy_notion)

        val privacyPolicyWebView = findViewById<WebView>(R.id.wv_privacy_policy)
        privacyPolicyWebView.loadUrl(PRIVACY_POLICY_URL)
    }

    companion object {
        private const val PRIVACY_POLICY_URL =
            "file:///android_asset/www/privacy_policy_notion.html"
    }
}