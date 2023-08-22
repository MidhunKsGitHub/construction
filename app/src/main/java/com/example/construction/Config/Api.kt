package com.example.construction.Config

import com.example.construction.Model.Login.LoginModel
import com.example.construction.Model.Report.Report
import com.example.construction.Model.Status.Status
import com.example.construction.Model.SuperVisorReport.SuperVisorReportApiModel
import com.example.construction.Model.Supervisor.SupervisorModelItem
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


interface Api {


    //login
    @FormUrlEncoded
    @POST("common/authenticate")
    fun login(
        @Header("Authorization") authorization: String,
        @Field("Authorization") Authorization: String,
        @Field("username") username: String,
        @Field("password") password: String,


        ): Call<LoginModel>


    //suoervisor

    @FormUrlEncoded
    @POST("commonData/getSupervisorEmployeesByApi")
    fun superVisor(
        @Header("Authorization") authorization: String,
        @Field("Authorization") Authorization: String,
        @Field("supervisor_id") supervisor_id: String,


        ): Call<SupervisorModelItem>


    //add attend

    @FormUrlEncoded
    @POST("employee/markAttendanceBySupervisorApi")
    fun superAttend(
        @Header("Authorization") authorization: String,
        @Field("Authorization") Authorization: String,
        @Field("date") date: String,
        @Field("longitude") longitude: String,
        @Field("latitude") latitude: String,
        @Field("array_list[]") array_list: ArrayList<String>,


        ): Call<Status>


    @FormUrlEncoded
    @POST("employee/markAttendanceByApi")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun empAttend(
        @Header("Authorization") authorization: String,
        @Field("Authorization") Authorization: String,
        @Field("date") date: String,
        @Field("longitude") longitude: String,
        @Field("latitude") latitude: String,
        @Field("employee_id") employee: String,


        ): Call<Status>


    //report

    @FormUrlEncoded
    @POST("employee/getAttendanceReportByEmployeeApi")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun empAttendReport(
        @Header("Authorization") authorization: String,
        @Field("Authorization") Authorization: String,
        @Field("tdate") tdate: String,
        @Field("fdate") fdate: String,
        @Field("employee_id") employee: String,


        ): Call<Report>



    //report

    @FormUrlEncoded
    @POST("commonData/getSupervisorEmployeesAttendaceReportByApi")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun supervisorAttendReport(
        @Header("Authorization") authorization: String,
        @Field("Authorization") Authorization: String,
        @Field("tdate") tdate: String,
        @Field("fdate") fdate: String,
        @Field("supervisor_id") supervisor_id: String,


        ): Call<SuperVisorReportApiModel>


    companion object{
        fun create():Api{
//            val gson = GsonBuilder()
//                .setLenient()
//                .create()


            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .build()
            return  retrofit.create(Api::class.java)

        }
    }

}