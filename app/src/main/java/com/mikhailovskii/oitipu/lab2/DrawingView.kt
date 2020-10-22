package com.mikhailovskii.oitipu.lab2

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.abs

class DrawingView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val TOUCH_TOLERANCE = 4
    }

    var penSize = 10f
        set(value) {
            field = value
            initializePen()
        }

    var eraserSize = 10f
        set(value) {
            field = value
            initializeEraser()
        }

    private var localX = 0f
    private var localY = 0f
    private var drawMode = true

    private val path = Path()
    private val bitmapPaint = Paint(Paint.DITHER_FLAG)

    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null

    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = penSize
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SCREEN)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        }

        bitmap?.let {
            canvas = Canvas(it)
        }

        canvas?.drawColor(Color.TRANSPARENT)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        bitmap?.let {
            canvas?.drawBitmap(it, 0f, 0f, bitmapPaint)
        }
        canvas?.drawPath(path, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                paint.xfermode =
                    PorterDuffXfermode(if (drawMode) PorterDuff.Mode.SCREEN else PorterDuff.Mode.CLEAR)
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)

                if (!drawMode) {
                    path.lineTo(this.localX, this.localY)
                    path.reset()
                    path.moveTo(x ?: 0f, y ?: 0f)
                }

                canvas?.drawPath(path, paint)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }

        return true
    }

    private fun touchUp() {
        path.lineTo(localX, localY)
        canvas?.drawPath(path, paint)
        path.reset()
        paint.xfermode =
            PorterDuffXfermode(if (drawMode) PorterDuff.Mode.SCREEN else PorterDuff.Mode.CLEAR)
    }

    private fun touchMove(x: Float?, y: Float?) {
        val dx = abs((x ?: 0f) - localX)
        val dy = abs((y ?: 0f) - localY)

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(localX, localY, ((x ?: 0f) + localX) / 2, ((y ?: 0f) + localY) / 2)
            localX = x ?: 0f
            localY = y ?: 0f
        }

        canvas?.drawPath(path, paint)
    }

    private fun touchStart(x: Float?, y: Float?) {
        path.apply {
            reset()
            moveTo(x ?: 0f, y ?: 0f)
        }

        localX = x ?: 0f
        localY = y ?: 0f

        canvas?.drawPath(path, paint)
    }

    fun initializePen() {
        drawMode = true
        paint.apply {
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = penSize
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SCREEN)
        }
    }

    fun initializeEraser() {
        drawMode = false
        paint.apply {
            color = Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = eraserSize
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }

    fun clear() {
        canvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    }

    fun changeBackground(color: Int = Color.WHITE) {
        canvas?.drawColor(color)
    }

    fun setPenColor(@ColorInt color: Int) {
        paint.color = color
    }

    fun getPenColor() = paint.color

    fun loadImage(bitmap: Bitmap) {
        this.bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        canvas?.setBitmap(this.bitmap)
        bitmap.recycle()
        invalidate()
    }

    fun saveImage(
        filePath: String?,
        filename: String,
        format: CompressFormat?,
        quality: Int
    ): Boolean {
        if (quality > 100) {
            Log.d("saveImage", "quality cannot be greater that 100")
            return false
        }

        val file: File
        var out: FileOutputStream? = null
        try {
            return when (format) {
                CompressFormat.PNG -> {
                    file = File(filePath, "$filename.png")
                    out = FileOutputStream(file)
                    bitmap?.compress(CompressFormat.PNG, quality, out) ?: false
                }
                CompressFormat.JPEG -> {
                    file = File(filePath, "$filename.jpg")
                    out = FileOutputStream(file)
                    bitmap?.compress(CompressFormat.JPEG, quality, out) ?: false
                }
                else -> {
                    file = File(filePath, "$filename.png")
                    out = FileOutputStream(file)
                    bitmap?.compress(CompressFormat.PNG, quality, out) ?: false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return false
    }


}