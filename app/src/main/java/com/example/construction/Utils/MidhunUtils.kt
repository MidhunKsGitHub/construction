package com.example.construction.Utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.example.construction.R

class MidhunUtils {

    companion object {
        lateinit var progress: ProgressDialog

        fun setFullScreen(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val decor: View = activity.window.decorView
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                //decor.setSystemUiVisibility(0)
            }

            if (Build.VERSION.SDK_INT >= 21) {
                val window: Window = activity.getWindow()
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.statusBarColor = activity.getResources().getColor(R.color.white)
            }
        }

        fun setTransparentScreen(activity: Activity){
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
               MidhunUtils.setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true,activity)
            }
            if (Build.VERSION.SDK_INT >= 19) {
                activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            if (Build.VERSION.SDK_INT >= 21) {
                MidhunUtils.setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false,activity)
                activity.window.statusBarColor = Color.TRANSPARENT
            }
        }

        fun showMessage(c: Context?, msg: String?) {
            Toast.makeText(c, msg, Toast.LENGTH_SHORT).show()
        }

        fun round(view: View, color: Int, color2: Int, radius: Float) {
            val shape = GradientDrawable(
                GradientDrawable.Orientation.BL_TR, intArrayOf(
                    color,
                    color2
                )
            )
            shape.cornerRadius = radius


            // now find your view and add background to it
            //View view = (LinearLayout) findViewById( R.id.);
            view.background = shape
        }

        fun setStatusBarIcon(activity: Activity, bw: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val decor = activity.window.decorView
                if (bw) {
                    decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    decor.systemUiVisibility = 0
                }
            }
        }

        fun changeStatusBarColor(activity: Activity,color: Int){
            if (Build.VERSION.SDK_INT >= 21) {
                val window: Window = activity.getWindow()
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.statusBarColor = activity.getResources().getColor(color)
            }
        }

        fun changeProgressBarColor(progressBar: ProgressBar, color: Int, activity: Activity) {
            progressBar.indeterminateDrawable.setColorFilter(
                activity.resources.getColor(color),
                PorterDuff.Mode.SRC_IN
            )
        }

        fun localData(activity: Activity, sname: String?, key: String?): String? {
            val sharedPreferences = activity.getSharedPreferences(sname, Context.MODE_PRIVATE)
            return sharedPreferences.getString(key, "")
        }

        fun localDataCtx(activity: Context, sname: String?, key: String?): String? {
            val sharedPreferences = activity.getSharedPreferences(sname, Context.MODE_PRIVATE)
            return sharedPreferences.getString(key, "")
        }

        fun addLocalData(context: Context, sname: String?, name: String?, key: String?) {
            val sharedPreferences = context.getSharedPreferences(sname, Context.MODE_PRIVATE)
            val myEdit = sharedPreferences.edit()
            myEdit.putString(name, key)
            myEdit.commit()
        }

        fun colorFilter(activity: Activity, image: ImageView, color: Int) {
            image.setColorFilter(activity.resources.getColor(color))
        }

        fun colorFilterContex(activity: Context, image: ImageView, color: Int) {
            image.setColorFilter(activity.resources.getColor(color))
        }

        fun showProgress(context: Activity, t: Boolean) {
            progress = ProgressDialog.show(context, null, null, true)
            progress.setContentView(R.layout.progress_layout)
            progress.setCancelable(t)
            progress.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            MidhunUtils.changeProgressBarColor(progress.findViewById(R.id.progressBar), R.color.white,context)
            progress.show()
        }

        private fun setWindowFlag(bits: Int, on: Boolean,activity: Activity) {
            val win = activity.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }
}