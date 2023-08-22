package com.example.construction

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.construction.Adapter.ReportAdapter
import com.example.construction.Config.Api
import com.example.construction.Config.Constant.API_KEY
import com.example.construction.Config.PdfGen
import com.example.construction.Model.Report.PageData
import com.example.construction.Model.Report.Report
import com.example.construction.Utils.MidhunUtils
import com.example.construction.databinding.ActivityEmployeeReportBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EmployeeReportActivity : AppCompatActivity() {
    lateinit var binding: ActivityEmployeeReportBinding
    lateinit var TYPE: String
    lateinit var UID: String
    lateinit var NAME: String
    lateinit var SDATE: String
    lateinit var EDATE: String
    lateinit var recyclerView: RecyclerView
    var currentDate = ""
    lateinit var reportList: ArrayList<PageData>
    lateinit var reportAdapter: ReportAdapter
    val myCalendar = Calendar.getInstance()
    private val PERMISSION_REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MidhunUtils.changeStatusBarColor(this@EmployeeReportActivity, R.color.black)
        UID = MidhunUtils.localData(this@EmployeeReportActivity, "login", "UID").toString()
        TYPE = MidhunUtils.localData(this@EmployeeReportActivity, "login", "TYPE").toString()
        NAME = MidhunUtils.localData(this@EmployeeReportActivity, "login", "NAME").toString()
        binding.print.visibility= View.GONE

        val date_start = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            val myFormat = "yyyy-MM-dd"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            currentDate = dateFormat.format(myCalendar.time)
            SDATE = currentDate
            binding.start.setText(SDATE)

        }

        val date_end = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            val myFormat = "yyyy-MM-dd"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            currentDate = dateFormat.format(myCalendar.time)
            EDATE=currentDate
            binding.end.setText(EDATE)
        }


        binding.start.setOnClickListener {
            DatePickerDialog(
                this@EmployeeReportActivity,
                date_start,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()


        }

        binding.end.setOnClickListener {
            DatePickerDialog(
                this@EmployeeReportActivity,
                date_end,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()

        }

        binding.calender.setOnClickListener {
            DatePickerDialog(
                this@EmployeeReportActivity,
                date_end,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()

        }


        binding.calender2.setOnClickListener {
            DatePickerDialog(
                this@EmployeeReportActivity,
                date_start,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()

        }


        binding.cardSearch.setOnClickListener {
            MidhunUtils.showProgress(this@EmployeeReportActivity, false)

            getReport()
        }

        recyclerView = binding.recyclerviewReport
        recyclerView.setHasFixedSize(true)
        var linearLayoutManager =
            LinearLayoutManager(this@EmployeeReportActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        binding.print.setOnClickListener {

            val pdfFilePath =
                Environment.getExternalStorageDirectory()
                    .toString() + "/documents/" +"ReportEmpList_"+SDATE+"_"+EDATE+ ".pdf"
            // below code is used for
            // checking our permissions.
            if (checkPermission()) {
                PdfGen.generatePdfEmp(reportList, pdfFilePath)
                MidhunUtils.showMessage(this@EmployeeReportActivity,"PDF saved sucessfully")

            } else {
                requestPermission()
            }



        }

    }

    private fun getReport() {
        var call = Api.create().empAttendReport(API_KEY, API_KEY, EDATE, SDATE, UID)
        call.enqueue(object : Callback<Report> {
            override fun onResponse(call: Call<Report>, response: Response<Report>) {
                reportList = response.body()!!.data.pageData
                MidhunUtils.progress.dismiss()
                reportAdapter = ReportAdapter(this@EmployeeReportActivity, reportList)
                recyclerView.adapter = reportAdapter
                binding.print.visibility= View.VISIBLE

                binding.textView5.setGravity(Gravity.LEFT);
            }

            override fun onFailure(call: Call<Report>, t: Throwable) {
                MidhunUtils.progress.dismiss()
                MidhunUtils.showMessage(this@EmployeeReportActivity, t.message)
            }

        })
    }

    private fun checkPermission(): Boolean {
        // checking of permissions.
        val permission1 =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permission2 =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}