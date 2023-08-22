package com.example.construction.Layout
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.construction.R

class XLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    CardView(context, attrs, defStyleAttr) {

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.custom_xlayout, this)

        val icon = findViewById<ImageView>(R.id.pro)
        val name = findViewById<TextView>(R.id.name)
        val address = findViewById<TextView>(R.id.addrees)
        val phone = findViewById<TextView>(R.id.phone)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.XLayout)
        try {
            val name_txt= ta.getString(R.styleable.XLayout_name)
            val address_txt= ta.getString(R.styleable.XLayout_address)
            val phone_txt = ta.getString(R.styleable.XLayout_phone)
            val drawableId = ta.getResourceId(R.styleable.XLayout_icon, 0)
            if (drawableId != 0) {
                val drawable = AppCompatResources.getDrawable(context, drawableId)
                icon.setImageDrawable(drawable)
            }
            name.text = name_txt
            address.text = address_txt
            phone.text = phone_txt
        } finally {
            ta.recycle()
        }
    }
}