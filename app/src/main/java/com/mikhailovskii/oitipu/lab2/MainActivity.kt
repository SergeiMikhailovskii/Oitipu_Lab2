package com.mikhailovskii.oitipu.lab2

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.SeekBar
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
                    drawing_view.penSize = 10f
                    drawing_view.penColor = it
                    btn_change_color.backgroundTintList = ColorStateList.valueOf(it)
                }.build()
                .show()
        }

        btn_erase.setOnClickListener {
            drawing_view.eraserSize = 50f
        }

        sb_eraser.max = 200
        sb_pen.max = 100000

        sb_eraser.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                drawing_view.eraserSize = (progress + 10).toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        sb_pen.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                drawing_view.penSize = (progress + 1).toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

    }
}