package com.example.construction.Model.SuperVisorReport

import com.google.gson.annotations.SerializedName

data class SuperVisorReportApiModel(

    @SerializedName("status")
    val reportsupervisors: ArrayList<Reportsupervisor>
)