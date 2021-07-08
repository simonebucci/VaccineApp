package it.mspc.vaccinedata.utilities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import it.mspc.vaccinedata.MainActivity
import it.mspc.vaccinedata.R


class Splash: AppCompatActivity() {
    // This is the loading time of the splash screen
    private val SPLASH_TIME_OUT: Long = 1000 // 1 sec
    val downloadManager = DownloadManager(this)
    var download = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

       // animationFlower()
        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            startActivity(Intent(this, MainActivity::class.java))

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }

    fun animationFlower(){
        val rotate = RotateAnimation(
            0F,
            180F,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 5000
        rotate.interpolator = LinearInterpolator()
        rotate.fillAfter = true
        rotate.repeatCount = Animation.INFINITE

        val image = findViewById<View>(R.id.imageView) as ImageView

        if (image != null) {
            image.startAnimation(rotate)
        }
    }

}