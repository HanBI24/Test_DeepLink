package hello.world.testapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest
import android.net.http.SslError
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import hello.world.testapp.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)

        setUpWebView()
        setBtnClickListener()
    }

    private fun setBtnClickListener() {
        binding.webviewBack.setOnClickListener(this)
        binding.webviewForward.setOnClickListener(this)
        binding.webviewReload.setOnClickListener(this)
        binding.webviewClose.setOnClickListener(this)
        binding.webviewHome.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.webviewBack -> {
                isWebViewCanGoBack()
            }

            binding.webviewClose -> {
                finish()
            }

            binding.webviewHome -> {
                homeClick()
            }

            binding.webviewForward -> {
                if (binding.sitesWebView.canGoForward()) binding.sitesWebView.goForward()
            }

            binding.webviewReload -> {
                val url = binding.sitesWebView.url
                loadWebViewUrl(url)
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() {
        binding.sitesWebView.webViewClient = MyWebViewClient()
        binding.sitesWebView.settings.javaScriptEnabled = true
        val webViewUrl = "https://www.naver.com/"
        loadWebViewUrl(webViewUrl);
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isWebViewCanGoBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun isWebViewCanGoBack() {
        if (binding.sitesWebView.canGoBack()) binding.sitesWebView.goBack()
        else finish()
    }

    private fun loadWebViewUrl(webViewUrl: String?) {
        val connectivityManager: ConnectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder = NetworkRequest.Builder()
        connectivityManager.registerNetworkCallback(builder.build(), object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                binding.sitesWebView.post {
                    webViewUrl?.let { binding.sitesWebView.loadUrl(it) }
                }
            }

            override fun onLost(network: Network) {
                binding.webviewReload.visibility = View.VISIBLE
            }
        })
    }

    private fun homeClick() {
        finish()
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.post {
                url?.let { view.loadUrl(it) }
            }
            return true
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding.webviewReload.visibility = View.GONE
            if (!binding.webViewProgressBar.isShown) binding.webViewProgressBar.visibility =
                View.VISIBLE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding.webviewReload.visibility = View.VISIBLE
            if (binding.webViewProgressBar.isShown) binding.webViewProgressBar.visibility =
                View.GONE
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            binding.webviewReload.visibility = View.VISIBLE
            if (binding.webViewProgressBar.isShown) binding.webViewProgressBar.visibility =
                View.GONE
        }

        override fun onReceivedHttpError(
            view: WebView?,
            request: WebResourceRequest?,
            errorResponse: WebResourceResponse?
        ) {
            super.onReceivedHttpError(view, request, errorResponse)
            binding.webviewReload.visibility = View.VISIBLE
            if (binding.webViewProgressBar.isShown) binding.webViewProgressBar.visibility =
                View.GONE
            Toast.makeText(this@WebViewActivity, "Fetch data", Toast.LENGTH_SHORT).show()
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            super.onReceivedSslError(view, handler, error)
            binding.webviewReload.visibility = View.VISIBLE
            if (binding.webViewProgressBar.isShown) binding.webViewProgressBar.visibility =
                View.GONE
        }
    }
}