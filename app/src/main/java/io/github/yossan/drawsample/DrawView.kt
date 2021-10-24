package io.github.yossan.drawsample

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Paint.FILTER_BITMAP_FLAG
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet

class DrawView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    enum class Mode {
        pen,
        eraser,
    }
    var mode: Mode = Mode.pen
        set(value) {
            field = value
            Log.d("DrawView", "mode = ${value}")
        }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.let { canvas ->
            drawPaths(canvas)
        }
    }

    private fun drawPaths(canvas: Canvas) {
        val paint = Paint().apply {
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            isDither = true
            strokeWidth = 10.0f
            strokeCap = Paint.Cap.ROUND
            flags = FILTER_BITMAP_FLAG and ANTI_ALIAS_FLAG
        }
        for (path in paths) {
            canvas.drawPath(path, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        return event?.let { event ->
            when(mode) {
                Mode.pen -> {
                    addPathWithEvent(event)
                }
                Mode.eraser -> {
                    removePathWithEvent(event)
                }
            }
        } ?: false
    }

    var paths: MutableList<Path> = mutableListOf()

    var prevPoint: PointF= PointF(0f, 0f)
    private fun addPathWithEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                val path = Path()
                path.moveTo(event.x, event.y)
                prevPoint = PointF(event.x, event.y)
                paths.add(path)
            }
            MotionEvent.ACTION_MOVE -> {
                val path = paths.last()
                path.quadTo(prevPoint.x, prevPoint.y, (prevPoint.x + event.x) / 2.0f, (prevPoint.y + event.y) / 2.0f)
                prevPoint = PointF(event.x, event.y)
            }
            MotionEvent.ACTION_UP -> {
                val path = paths.last()
                path.lineTo(event.x, event.y)
            }
        }
        invalidate()
        return true
    }

    private fun removePathWithEvent(event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_UP -> {
                for (path in paths.reversed()) {
                    if (path.doIntersect(event.x, event.y, 5f)) {
                        paths.remove(path)
                    }
                }
            }
        }
        invalidate()
        return true
    }

    fun Path.doIntersect(x: Float, y: Float, width: Float): Boolean {
        val measure = PathMeasure(this, false)
        val length = measure.length
        val delta = width / 2f
        val position = floatArrayOf(0f, 0f)
        val bounds = RectF()
        var distance = 0f
        var intersects = false
        while (distance <= length) {
            measure.getPosTan(distance, position, null)
            bounds.set(
                position[0] - delta,
                position[1] - delta,
                position[0] + delta,
                position[1] + delta
            )
            if (bounds.contains(x, y)) {
                intersects = true
                break
            }
            distance += delta / 2f
        }
        return intersects
    }
}