package com.example.construction

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.construction.Config.Api
import com.example.construction.Config.Constant.API_KEY
import com.example.construction.Location.LocationActivity
import com.example.construction.Model.Status.Status
import com.example.construction.Utils.MidhunUtils
import com.example.construction.databinding.ActivityEmployeeBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EmployeeActivity : AppCompatActivity() {
    lateinit var binding: ActivityEmployeeBinding
    val myCalendar = Calendar.getInstance()

    var currentDate = ""
    var currentDate1 = ""
    lateinit var TYPE: String
    lateinit var UID: String
    lateinit var NAME: String

    var LATITUDE = ""
    var LONGITUDE = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        MidhunUtils.changeStatusBarColor(this@EmployeeActivity, R.color.black)
        UID = MidhunUtils.localData(this@EmployeeActivity, "login", "UID").toString()
        TYPE = MidhunUtils.localData(this@EmployeeActivity, "login", "TYPE").toString()
        NAME = MidhunUtils.localData(this@EmployeeActivity, "login", "NAME").toString()
        LONGITUDE = MidhunUtils.localData(this@EmployeeActivity, "location", "lati").toString()
        LATITUDE = MidhunUtils.localData(this@EmployeeActivity, "location", "longi").toString()

        checkLocation()

        binding.type.text = TYPE
        binding.username.text = NAME.uppercase(Locale.ROOT)

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        currentDate = sdf.format(Date())

        val sdf1 = SimpleDateFormat("dd-MMM-yyyy")
        currentDate1 = sdf1.format(Date())

        binding.dateTxt.text = currentDate1.replace("-", "\n")

        ///date picker
        val date = OnDateSetListener { view, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            val myFormat = "yyyy-MM-dd"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            currentDate = dateFormat.format(myCalendar.time)

            val myFormat1 = "dd-MMM-yyyy"
            val dateFormat1 = SimpleDateFormat(myFormat1, Locale.US)
            currentDate1 = dateFormat1.format(myCalendar.time)
            binding.dateTxt.text = currentDate1.replace("-", "\n")

        }

        binding.imgPower.setOnClickListener {
            val snackbar: Snackbar = Snackbar.make(
                binding.imgPower, "Do you want to logout", Snackbar.LENGTH_SHORT
            ).setAction("Yes", View.OnClickListener {

                MidhunUtils.addLocalData(this@EmployeeActivity, "login", "UID", "")
                intent = Intent()
                intent.setClass(this@EmployeeActivity, SplashActivity::class.java)
                startActivity(intent)
                finish()

            })

            snackbar.show()
        }

        binding.cardCal.setOnClickListener {
            DatePickerDialog(
                this@EmployeeActivity,
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        binding.imgCal.setOnClickListener {

            startActivity(Intent(this@EmployeeActivity, EmployeeReportActivity::class.java))

        }
        binding.floatingActionButton.setOnClickListener {



               MidhunUtils.showProgress(this@EmployeeActivity, false)
                setEmpAttend()


        }

    }

    private fun checkLocation() {

        startActivity(Intent(this@EmployeeActivity, LocationActivity::class.java))

    }

    private fun setEmpAttend() {
        var call = Api.create().empAttend(API_KEY, API_KEY, currentDate, LATITUDE, LONGITUDE, UID)

        call.enqueue(object : Callback<Status> {
            override fun onResponse(call: Call<Status>, response: Response<Status>) {
                startActivity(intent.setClass(this@EmployeeActivity, SuccessActivity::class.java))
                MidhunUtils.progress.dismiss()

            }

            override fun onFailure(call: Call<Status>, t: Throwable) {
                MidhunUtils.showMessage(this@EmployeeActivity, t.message)
                MidhunUtils.progress.dismiss()
            }

        })
    }

    override fun onResume() {
        super.onResume()



    }
}