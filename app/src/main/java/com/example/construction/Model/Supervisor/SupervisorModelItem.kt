package com.example.construction.Model.Supervisor

import com.google.gson.annotations.SerializedName

data class SupervisorModelItem(
    @SerializedName("status")
    val statuses: List<Supervisor>
)