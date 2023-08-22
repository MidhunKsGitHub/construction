package com.example.construction

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.construction.Adapter.SupervisorAdapter
import com.example.construction.Config.Api
import com.example.construction.Config.Constant.API_KEY
import com.example.construction.Location.LocationActivity
import com.example.construction.Model.Status.Status
import com.example.construction.Model.Supervisor.Supervisor
import com.example.construction.Model.Supervisor.SupervisorModelItem
import com.example.construction.Utils.MidhunUtils
import com.example.construction.databinding.ActivitySupervisorBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class SupervisorActivity : AppCompatActivity() {
    lateinit var binding: ActivitySupervisorBinding
    lateinit var UID: String
    lateinit var supervisorList: List<Supervisor>
    lateinit var supervisorAdapter: SupervisorAdapter
    lateinit var recyclerView: RecyclerView
    var attendList = ArrayList<String>()
    var currentDate = ""

    var isAllCheckBoxChecked = false
    var currentDate1 = ""
    val myCalendar = Calendar.getInstance()

    var LATITUDE = ""
    var LONGITUDE = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupervisorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MidhunUtils.changeStatusBarColor(this@SupervisorActivity, R.color.black)
        UID = MidhunUtils.localData(this@SupervisorActivity, "login", "UID").toString()
        MidhunUtils.showProgress(this@SupervisorActivity, false)



        checkLocation()


        val sdf1 = SimpleDateFormat("dd-MMM-yyyy")
        currentDate1 = sdf1.format(Date())

        binding.dateTxt.text = currentDate1.replace("-", "\n")


        ///date picker
        val date = DatePickerDialog.OnDateSetListener { view, year, month, day ->
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


        binding.cardDate.setOnClickListener {
            DatePickerDialog(
                this@SupervisorActivity,
                date,
                myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()

        }

        binding.imgCalendar.setOnClickListener {
            startActivity(Intent(this@SupervisorActivity, SuperVisorReportActivity::class.java))
        }

        binding.checkAll.setOnClickListener {
            supervisorAdapter.checkBoxMarkAll()
            isAllCheckBoxChecked = true
            binding.checkAll.visibility = View.GONE
            binding.uncheckall.visibility = View.VISIBLE
        }


        binding.uncheckall.setOnClickListener {
            supervisorAdapter.umCheckBoxMarkAll()
            isAllCheckBoxChecked = false
            binding.checkAll.visibility = View.VISIBLE
            binding.uncheckall.visibility = View.GONE

        }


        binding.editTextText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                getSuperVisorEmpSort()


            }
        })

        binding.imgPower.setOnClickListener {
            val snackbar: Snackbar = Snackbar.make(
                binding.imgPower, "Do you want to logout", Snackbar.LENGTH_SHORT
            ).setAction("Yes", View.OnClickListener {

                MidhunUtils.addLocalData(this@SupervisorActivity, "login", "UID", "")
                intent = Intent()
                intent.setClass(this@SupervisorActivity, SplashActivity::class.java)
                startActivity(intent)
                finish()

            })

            snackbar.show()
        }


        val sdf = SimpleDateFormat("yyyy-MM-dd")
        currentDate = sdf.format(Date())


        recyclerView = binding.recyclerview
        recyclerView.setHasFixedSize(true)
        val linearLayoutManager =
            LinearLayoutManager(this@SupervisorActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        getSuperVisorEmp()



        binding.floatingActionButton2.setOnClickListener {
            try {
                if (isAllCheckBoxChecked == true) {
                    attendList = supervisorAdapter.markAll()
                } else {
                    attendList = supervisorAdapter.attend()
                }

                // MidhunUtils.showMessage(this@SupervisorActivity, attendList.toString())

                if (!attendList.isEmpty() && attendList != null) {
                    MidhunUtils.showProgress(this@SupervisorActivity, false)
                    addAttend()
                } else {
                    MidhunUtils.showMessage(this@SupervisorActivity, "Add People")
                }


            } catch (e: java.lang.Exception) {

            }
        }

    }

    private fun checkLocation() {

        startActivity(Intent(this@SupervisorActivity, LocationActivity::class.java))
        LONGITUDE= MidhunUtils.localData(this@SupervisorActivity, "location", "lati").toString()
        LATITUDE= MidhunUtils.localData(this@SupervisorActivity, "location", "longi").toString()

//        if (MidhunUtils.localData(this@SupervisorActivity, "location", "lati")
//                .equals("") && MidhunUtils.localData(this@SupervisorActivity, "location", "longi")
//                .equals("")
//        ) {
//
//        }
    }

    private fun getSuperVisorEmp() {
        var call = Api.create().superVisor(API_KEY, API_KEY, UID)
        call.enqueue(object : Callback<SupervisorModelItem> {
            override fun onResponse(
                call: Call<SupervisorModelItem>, response: Response<SupervisorModelItem>
            ) {
                try {
                    MidhunUtils.progress.dismiss()
                    supervisorList = response.body()!!.statuses
                    supervisorAdapter = SupervisorAdapter(this@SupervisorActivity, supervisorList)
                    recyclerView.adapter = supervisorAdapter


                } catch (e: Exception) {
                    MidhunUtils.showMessage(this@SupervisorActivity, e.message)
                }

            }

            override fun onFailure(call: Call<SupervisorModelItem>, t: Throwable) {
                MidhunUtils.showMessage(this@SupervisorActivity, t.toString())

            }

        })
    }

    private fun getSuperVisorEmpSort() {
        var call = Api.create().superVisor(API_KEY, API_KEY, UID)
        call.enqueue(object : Callback<SupervisorModelItem> {
            override fun onResponse(
                call: Call<SupervisorModelItem>, response: Response<SupervisorModelItem>
            ) {
                try {
                    supervisorList = response.body()!!.statuses
                    sort(
                        binding.editTextText.text.toString(),
                        supervisorList as ArrayList<Supervisor>
                    )
                    supervisorAdapter = SupervisorAdapter(this@SupervisorActivity, supervisorList)
                    recyclerView.adapter = supervisorAdapter


                } catch (e: Exception) {
                    MidhunUtils.showMessage(this@SupervisorActivity, e.message)
                }

            }

            override fun onFailure(call: Call<SupervisorModelItem>, t: Throwable) {
                MidhunUtils.showMessage(this@SupervisorActivity, t.toString())

            }

        })
    }

    private fun addAttend() {

        for (i in attendList.indices) {
            MidhunUtils.addLocalData(
                this@SupervisorActivity, "mark", attendList.get(i) + currentDate, "added"
            )
        }
        var call = Api.create().superAttend(API_KEY, API_KEY, currentDate,LONGITUDE,LATITUDE, attendList)
        call.enqueue(object : Callback<Status> {
            override fun onResponse(call: Call<Status>, response: Response<Status>) {
                if (response.code() == 200) {
                    startActivity(
                        intent.setClass(
                            this@SupervisorActivity, SuccessActivity::class.java
                        )
                    )
                }
                attendList.clear()
                MidhunUtils.progress.dismiss()

            }

            override fun onFailure(call: Call<Status>, t: Throwable) {
                MidhunUtils.showMessage(this@SupervisorActivity, t.message)
                attendList.clear()
                MidhunUtils.progress.dismiss()
            }

        })
    }

    fun sort(name: String?, p: ArrayList<Supervisor>) {
        val length = p.size
        var index = length - 1
        for (i in 0 until length) {
            if (p[index].name.contains(name.toString())) {
            } else {
                p.removeAt(index)
            }
            index--
        }
    }

    override fun onResume() {
        super.onResume()

    }
}