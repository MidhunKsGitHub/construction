package com.example.construction.Model.Supervisor

import com.google.gson.annotations.SerializedName

data class SupervisorModel(

    @SerializedName("statuses")
  var status: SupervisorModelItem
)
