package hello.world.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import hello.world.testapp.databinding.ActivityYoutubeViewBinding

class YoutubeViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityYoutubeViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_youtube_view)
    }
}