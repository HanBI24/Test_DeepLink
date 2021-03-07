package hello.world.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import hello.world.testapp.databinding.ActivityLinkBinding

class LinkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLinkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_link)

        val intent = intent
        val longLink = intent.getStringExtra("long_link")
        val shortLink = intent.getStringExtra("short_link")
        val debugLink = intent.getStringExtra("debug_link")

        binding.tv1.text = longLink
        binding.tv2.text = shortLink
        binding.tv3.text = debugLink
    }
}