package com.example.construction

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.construction.Config.Api
import com.example.construction.Config.Constant.API_KEY
import com.example.construction.Model.Login.LoginModel
import com.example.construction.Model.Login.LoginModelItem
import com.example.construction.Utils.MidhunUtils
import com.example.construction.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    lateinit var userList:List<LoginModelItem>

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MidhunUtils.changeStatusBarColor(this@LoginActivity, R.color.black)

        binding.btn.setOnClickListener {

            if (binding.phone.text.isEmpty()) {
                MidhunUtils.showMessage(this@LoginActivity, "Enter Phone")
            } else if (binding.password.text.isEmpty()) {
                MidhunUtils.showMessage(this@LoginActivity, "Enter Password")
            } else {
                MidhunUtils.showProgress(this@LoginActivity, false)
                userLogin()
            }
        }
    }

    private fun userLogin(){
        val call = Api.create().login(API_KEY, API_KEY,binding.phone.text.toString(),binding.password.text.toString())
        call.enqueue(object : Callback<LoginModel>{
            override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
               userList= response.body()!!

                if(!userList.get(0).user_type.equals("invalid")){

                    if(userList.get(0).value.equals("Supervisor")){
                        MidhunUtils.addLocalData(this@LoginActivity,"login","UID",userList.get(0).id)
                        MidhunUtils.addLocalData(this@LoginActivity,"login","TYPE",userList.get(0).value)
                        MidhunUtils.addLocalData(this@LoginActivity,"login","NAME",userList.get(0).name)

                        intent = Intent()
                        intent.setClass(this@LoginActivity , SupervisorActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        MidhunUtils.addLocalData(this@LoginActivity,"login","UID",userList.get(0).id)
                        MidhunUtils.addLocalData(this@LoginActivity,"login","TYPE",userList.get(0).value)
                        MidhunUtils.addLocalData(this@LoginActivity,"login","NAME",userList.get(0).name)

                        intent = Intent()
                        intent.setClass(this@LoginActivity, EmployeeActivity::class.java)
                        startActivity(intent)
                        finish()

                    }

                    MidhunUtils.progress.dismiss()
                }
                else{
                    MidhunUtils.showMessage(this@LoginActivity,userList.get(0).error)
                    MidhunUtils.progress.dismiss()

                }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                MidhunUtils.showMessage(this@LoginActivity,t.message)
               MidhunUtils.progress.dismiss()
            }

        })
    }
}