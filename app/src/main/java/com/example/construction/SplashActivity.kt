package com.example.construction

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.construction.Utils.MidhunUtils
import com.example.construction.databinding.ActivitySplashBinding
import java.util.MissingFormatArgumentException

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    lateinit var UID: String
    lateinit var TYPE: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        MidhunUtils.setStatusBarIcon(this@SplashActivity, false)
        MidhunUtils.changeStatusBarColor(this@SplashActivity, R.color.black)

        UID = MidhunUtils.localData(this@SplashActivity, "login", "UID").toString()
        TYPE = MidhunUtils.localData(this@SplashActivity, "login", "TYPE").toString()

        Handler(Looper.getMainLooper()).postDelayed({

            if (UID.isEmpty()) {
                intent = Intent()
                intent.setClass(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {

                if (TYPE.equals("Supervisor")) {

                    intent = Intent()
                    intent.setClass(this@SplashActivity, SupervisorActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {

                    intent = Intent()
                    intent.setClass(this@SplashActivity, EmployeeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        }, 1500)


    }
}