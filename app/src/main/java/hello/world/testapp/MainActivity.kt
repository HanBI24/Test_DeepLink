package hello.world.testapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import hello.world.testapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
        private const val SEGMENT_PROMOTION = "check"
        private const val KEY_CODE = "key"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        handleDeepLink()

/*        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            // getCheckDeepLink() => 동적 링크 수정
            link = Uri.parse("https://hello18.com/")
            domainUriPrefix = "https://hellotest18.page.link/"
            // Open links with this app on Android
            androidParameters { }
            // Open links with com.example.ios on iOS
            iosParameters("com.example.ios") { }
        }
        val dynamicLinkUri = dynamicLink.uri
        Log.d(TAG, dynamicLinkUri.toString())*/

        binding.btn.setOnClickListener {
            Toast.makeText(this@MainActivity, "Created", Toast.LENGTH_SHORT).show()
            onDynamicLinkClick()
        }

        binding.btnWebView.setOnClickListener{
            startActivity(Intent(this@MainActivity, WebViewActivity::class.java))
        }

        createAdapter()
    }

    private fun createAdapter() {
        val mData = ArrayList<ListItems>()
        val adapter = RecyclerAdapter()

        mData.apply {
            add(ListItems("hello1"))
            add(ListItems("hello2"))
            add(ListItems("hello3"))
            add(ListItems("hello4"))
            add(ListItems("hello5"))
        }

        adapter.apply {
            items = mData
            notifyDataSetChanged()
        }

        binding.rv.adapter = adapter

        adapter.setItemClickListener(object : RecyclerAdapter.ItemClickListener {
            override fun onClick(view: View, position: Int) {
                Toast.makeText(this@MainActivity, position.toString(), Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                intent.putExtra("list_position", position.toString())
                startActivity(intent)
            }
        })
    }

    private fun handleDeepLink() {
        Firebase.dynamicLinks
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData ->
                    var deepLink: Uri? = null
                    // 앱으로 실행한 경우
                    if (pendingDynamicLinkData == null) {
                        return@addOnSuccessListener
                    }

                    deepLink = pendingDynamicLinkData.link
                    Log.d(TAG, "deepLink: $deepLink")

                    when(deepLink!!.lastPathSegment) {
                        SEGMENT_PROMOTION -> {
                            val code = deepLink.getQueryParameter(KEY_CODE)
//                            showCheckDialog(code)
                            val intent = Intent(this@MainActivity, SecondDeepLinkActivity::class.java)
                            intent.putExtra("deep_list_position", code)
                            startActivity(intent)
                        }
                    }

                }
                .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }
    }

    private fun showCheckDialog(code: String?) {
        AlertDialog.Builder(this@MainActivity)
                .setMessage("Key Code: $code")
                .setPositiveButton("OK", null)
                .create()
                .show()
    }

    private fun getPromotionDeepLink(): Uri {
        val promotionCode = "3"
        return Uri.parse("https://hello18.com/$SEGMENT_PROMOTION?$KEY_CODE=$promotionCode")
    }

    private fun onDynamicLinkClick() {
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            // getCheckDeepLink() => 동적 링크 수정
            link = getPromotionDeepLink()
            domainUriPrefix = "https://hellotest18.page.link/"
            // Open links with this app on Android
            androidParameters { DynamicLink.AndroidParameters.Builder(packageName).build() }
            // Open links with com.example.ios on iOS
            iosParameters("com.example.ios") { }
        }
        val dynamicLinkUri = dynamicLink.uri

        val shortLinkTask = Firebase.dynamicLinks.shortLinkAsync {
            link = dynamicLinkUri
            domainUriPrefix = "https://hellotest18.page.link/"
            // Set parameters
            // ...
        }.addOnSuccessListener { (shortLink, flowchartLink) ->
            // You'll need to import com.google.firebase.dynamiclinks.ktx.component1 and
            // com.google.firebase.dynamiclinks.ktx.component2

            // Short link created
            processShortLink(dynamicLinkUri, shortLink, flowchartLink)
        }.addOnFailureListener {
            // Error
            // ...
        }
    }

    private fun processShortLink(dynamicLink: Uri?, shortLink: Uri?, flowchartLink: Uri?) {
        Log.d(TAG, dynamicLink.toString())
        Log.d(TAG, shortLink.toString())
        Log.d(TAG, flowchartLink.toString())

        val intent = Intent(this@MainActivity, LinkActivity::class.java)
        intent.putExtra("long_link", dynamicLink.toString())
        intent.putExtra("short_link", shortLink.toString())
        intent.putExtra("debug_link", flowchartLink.toString())
        startActivity(intent)

    }

}