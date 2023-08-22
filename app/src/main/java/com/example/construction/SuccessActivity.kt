package com.example.construction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.construction.Utils.MidhunUtils
import com.example.construction.databinding.ActivitySuccessBinding

class SuccessActivity : AppCompatActivity() {

    lateinit var binding: ActivitySuccessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MidhunUtils.setStatusBarIcon(this@SuccessActivity, false)
        MidhunUtils.changeStatusBarColor(this@SuccessActivity, R.color.black)

    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(intent.setClass(this@SuccessActivity,SplashActivity::class.java))
    }
}