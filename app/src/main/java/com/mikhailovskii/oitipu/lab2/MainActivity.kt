package com.mikhailovskii.oitipu.lab2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
            setPenColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
        }

        iv_settings.setOnClickListener {
//            drawing_view.changeBackground()
            val dialog = SettingsBottomSheetDialogFragment.newInstance(
                drawing_view.penSize.toInt(),
                drawing_view.eraserSize.toInt(),
                drawing_view.getPenColor()
            )

            dialog.apply {
                show(supportFragmentManager, "SettingsDialog")

                eraserSeekBarCallback = {
                    drawing_view.eraserSize = it.toFloat()
                }

                penSeekBarCallback = {
                    drawing_view.penSize = it.toFloat()
                }

                penColorCallback = {
                    drawing_view.setPenColor(it)
                }

                lineCallback = {
                    drawing_view.isRectangleMode = false
                    drawing_view.isRoundMode = false
                }

                rectangleCallback = {
                    drawing_view.isRectangleMode = true
                    drawing_view.isRoundMode = false
                }

                circleCallback = {
                    drawing_view.isRectangleMode = false
                    drawing_view.isRoundMode = true
                }

            }

        }


    }
}