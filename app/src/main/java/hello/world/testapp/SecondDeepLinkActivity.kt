package hello.world.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import hello.world.testapp.databinding.ActivitySecondDeepLinkBinding

class SecondDeepLinkActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondDeepLinkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_second_deep_link)

        val intent = intent
        val position = intent.getStringExtra("deep_list_position")
        binding.tv1.text = position
    }
}