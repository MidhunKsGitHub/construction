package com.example.construction

import android.Manifest.permission
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.BuildConfig
import com.example.construction.Adapter.SuperVisorReportAdapter
import com.example.construction.Config.Api
import com.example.construction.Config.Constant
import com.example.construction.Config.PdfGen
import com.example.construction.Location.LocationActivity
import com.example.construction.Model.SuperVisorReport.Reportsupervisor
import com.example.construction.Model.SuperVisorReport.SuperVisorReportApiModel
import com.example.construction.Utils.MidhunUtils
import com.example.construction.databinding.ActivitySuperVisorReportBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class SuperVisorReportActivity : AppCompatActivity() {

    lateinit var binding: ActivitySuperVisorReportBinding

    lateinit var TYPE: String
    lateinit var UID: String
    lateinit var NAME: String
    lateinit var SDATE: String
    lateinit var EDATE: String
    lateinit var recyclerView: RecyclerView
    var currentDate = ""
    val myCalendar = Calendar.getInstance()
    lateinit var superVisorReportAdapter: SuperVisorReportAdapter
    lateinit var supervisorreportList: ArrayList<Reportsupervisor>

    private val PERMISSION_REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuperVisorReportBinding.inflate(layoutInflater)
        setContentView(binding.root)


        MidhunUtils.changeStatusBarColor(this@SuperVisorReportActivity, R.color.black)
        UID = MidhunUtils.localData(this@SuperVisorReportActivity, "login", "UID").toString()
        TYPE = MidhunUtils.localData(this@SuperVisorReportActivity, "login", "TYPE").toString()
        NAME = MidhunUtils.localData(this@SuperVisorReportActivity, "login", "NAME").toString()
        binding.print.visibility = View.GONE


        val sdf1 = SimpleDateFormat("yyyy-MM-dd")
        var currentDate1 = sdf1.format(Date())
        EDATE = currentDate1
        SDATE = currentDate1


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
            EDATE = currentDate
            binding.end.setText(EDATE)
        }


        binding.start.setOnClickListener {
            DatePickerDialog(
                this@SuperVisorReportActivity,
                date_start,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()


        }

        binding.end.setOnClickListener {
            DatePickerDialog(
                this@SuperVisorReportActivity,
                date_end,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()

        }

        binding.calender.setOnClickListener {
            DatePickerDialog(
                this@SuperVisorReportActivity,
                date_end,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()

        }

        binding.calender2.setOnClickListener {
            DatePickerDialog(
                this@SuperVisorReportActivity,
                date_start,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()

        }


        recyclerView = binding.recyclerviewReport
        recyclerView.setHasFixedSize(true)
        var linearLayoutManager =
            LinearLayoutManager(this@SuperVisorReportActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        binding.cardSearch.setOnClickListener {
            MidhunUtils.showProgress(this@SuperVisorReportActivity, false)

            getReport()
        }
        binding.print.setOnClickListener {

            val pdfFilePath =
                Environment.getExternalStorageDirectory()
                    .toString() + "/documents/" + "ReportAllList_" + SDATE + "_" + EDATE + ".pdf"
            // below code is used for
            // checking our permissions.
            if (checkPermission()) {
                PdfGen.generatePdf(supervisorreportList, pdfFilePath)
                MidhunUtils.showMessage(this@SuperVisorReportActivity, "PDF saved sucessfully")
            } else {
                requestPermission()
            }


        }


    }


    private fun getReport() {
        var call = Api.create().supervisorAttendReport(
            Constant.API_KEY, Constant.API_KEY, EDATE, SDATE, UID
        )
        call.enqueue(object : Callback<SuperVisorReportApiModel> {
            override fun onResponse(
                call: Call<SuperVisorReportApiModel>, response: Response<SuperVisorReportApiModel>
            ) {
                supervisorreportList = response.body()!!.reportsupervisors
                MidhunUtils.progress.dismiss()
                superVisorReportAdapter =
                    SuperVisorReportAdapter(this@SuperVisorReportActivity, supervisorreportList)
                recyclerView.adapter = superVisorReportAdapter
                binding.print.visibility = View.VISIBLE
                binding.textView5.gravity = Gravity.LEFT
            }

            override fun onFailure(call: Call<SuperVisorReportApiModel>, t: Throwable) {
                MidhunUtils.progress.dismiss()
                MidhunUtils.showMessage(this@SuperVisorReportActivity, t.message)
            }

        })
    }

    private fun checkPermission(): Boolean {
        // checking of permissions.
        val permission1 =
            ContextCompat.checkSelfPermission(applicationContext, permission.WRITE_EXTERNAL_STORAGE)
        val permission2 =
            ContextCompat.checkSelfPermission(applicationContext, permission.READ_EXTERNAL_STORAGE)
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        // requesting permissions if not provided.

        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    permission.READ_MEDIA_IMAGES,
                    permission.READ_MEDIA_VIDEO,
                    permission.READ_MEDIA_AUDIO
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private val launcher: ActivityResultLauncher<Intent>? = null
    private var baseDocumentTreeUri: Uri? = null

    fun launchBaseDirectoryPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        launcher!!.launch(intent)
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

                    if (Build.VERSION.SDK_INT >= 33) {
                        val pdfFilePath =
                            Environment.getExternalStorageDirectory()
                                .toString() + "/Download/" + "ReportAllList_" + SDATE + "_" + EDATE + ".pdf"
                        // below code is used for
                        // checking our permissions.
                        if (checkPermission()) {
                            PdfGen.generatePdf(supervisorreportList, pdfFilePath)
                            MidhunUtils.showMessage(
                                this@SuperVisorReportActivity,
                                "PDF saved sucessfully"
                            )
                        }
                    }
                } else {
//                    val u = Uri.parse("package:com.example.construction")
//                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, u)
//                    startActivity(intent)
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


}