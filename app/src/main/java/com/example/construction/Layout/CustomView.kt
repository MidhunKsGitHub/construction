package com.example.construction.Layout
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.construction.R

class CustomView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        View.inflate(context, R.layout.custom_layout, this)

        val image_thumb = findViewById<ImageView>(R.id.image_thumb)
        val text_title = findViewById<TextView>(R.id.text_title)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.CustomView)
        try {
            val text = ta.getString(R.styleable.CustomView_text)
            val drawableId = ta.getResourceId(R.styleable.CustomView_image, 0)
            if (drawableId != 0) {
                val drawable = AppCompatResources.getDrawable(context, drawableId)
                image_thumb.setImageDrawable(drawable)
            }
            text_title.text = text
        } finally {
            ta.recycle()
        }
    }
}