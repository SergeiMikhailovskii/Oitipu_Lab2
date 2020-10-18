package com.mikhailovskii.oitipu.lab2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawing_view.apply {
            initializePen()
            initializeEraser()
            setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
            eraserSize = 10f
            penSize = 10f
            penColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        }

        btn_change_color.setOnClickListener {
            ColorPickerDialogBuilder.with(btn_change_color.context)
                    .setTitle("Choose color")
                    .initialColor(R.color.colorPrimaryDark)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener {
                        drawing_view.penColor = it
                    }.build()
                    .show()
        }

        btn_erase.setOnClickListener {
            drawing_view.eraserSize = 50f
        }

    }
}