package com.example.construction.Location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.construction.R
import com.example.construction.Utils.MidhunUtils
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    var resultReceiver: ResultReceiver? = null
    var lati: Double? = null
    var longi: Double? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        MidhunUtils.changeStatusBarColor(this@LocationActivity, R.color.black)

        if (ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@LocationActivity,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {

            getCurrentLocation()
        }


    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun getCurrentLocation() {
        // progressBar.setVisibility(View.VISIBLE)
        val locationRequest = com.google.android.gms.location.LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 3000
        locationRequest.priority = 100
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        LocationServices.getFusedLocationProviderClient(this@LocationActivity)
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(applicationContext)
                        .removeLocationUpdates(this)
                    if (locationResult != null && locationResult.getLocations().size > 0) {
                        val latestlocIndex: Int = locationResult.getLocations().size - 1
                        lati = locationResult.getLocations().get(latestlocIndex).getLatitude()
                        longi = locationResult.getLocations().get(latestlocIndex).getLongitude()


                        val sharedPreferences = getSharedPreferences("location", MODE_PRIVATE)
                        val myEdit = sharedPreferences.edit()
                        myEdit.putString("lati", lati.toString())
                        myEdit.putString("longi", longi.toString())
                        myEdit.commit()

                        MidhunUtils.addLocalData(this@LocationActivity,"loaction","lati",lati.toString())
                        MidhunUtils.addLocalData(this@LocationActivity,"loaction","longi",longi.toString())

                        val location = Location("providerNA")
                        location.longitude = longi as Double
                        location.latitude = lati as Double
                        fetchaddressfromlocation(location)

                        finish()
                    }


                }
            }, Looper.getMainLooper())
    }

    class AddressResultReceiver(handler: Handler?) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            super.onReceiveResult(resultCode, resultData)
            if (resultCode == Constants.SUCCESS_RESULT) {
//                address.setText(resultData.getString(Constants.ADDRESS))
//                locaity.setText(resultData.getString(Constants.LOCAITY))
//                state.setText(resultData.getString(Constants.STATE))
//                district.setText(resultData.getString(Constants.DISTRICT))
//                country.setText(resultData.getString(Constants.COUNTRY))
//                postcode.setText(resultData.getString(Constants.POST_CODE))

            } else {
            }
        }
    }

    private fun fetchaddressfromlocation(location: Location) {
        val intent = Intent(this, FetchAddressIntentServices::class.java)
        intent.putExtra(Constants.RECEVIER, resultReceiver)
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location)
        startService(intent)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(Intent(this@LocationActivity,LocationActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }



}