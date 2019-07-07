package com.alexeykatsuro.scalablefieldview.ui.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.alexeykatsuro.scalablefieldview.R
import com.alexeykatsuro.scalablefieldview.utils.copy
import com.alexeykatsuro.scalablefieldview.utils.toPx


private typealias PaintConfig = Paint.() -> Unit
typealias OnFocusPointChangeListener = (old: PointF, new: PointF) -> Unit
typealias OnScaleChangeListener = (scale: Float) -> Unit

class ScalableFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    companion object {
        val DEFAULT_CELL_SIZE = 5f.toPx
        val DEFAULT_GRID_LINE_WIDTH = 1f.toPx
        const val DEFAULT_GRID_LINE_COLOR = Color.GRAY

        const val DEFAULT_IS_SHOW_GRID = true
        const val DEFAULT_SCALE = 1f
    }

    private var onFocuspointChange: OnFocusPointChangeListener? = null
    private var onScaleChange: OnScaleChangeListener? = null

    private val paint = Paint()
    private val path = Path()
    private val drawMatrix = Matrix()

    var cellSize: Float

    @ColorInt
    var gridLineColor: Int
    var gridLineWidth: Float

    var isShowGrid: Boolean

    private val gestureListener = object : OnMoveGestureListener {

        override fun onDrag(dx: Float, dy: Float) {
            drawMatrix.postTranslate(-dx, -dy)
            invalidate()
        }

        override fun onScale(scaleFactor: Float, focusX: Float, focusY: Float) {
            drawMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY)
        }

    }

    private val gestureDetector: MovementGestureDetector = MovementGestureDetector(context, gestureListener)


    init {

        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.scalable_field_view,
            defStyleAttr,
            defStyleRes
        )
        cellSize = typedArray.getDimension(
            R.styleable.scalable_field_view_cell_size, DEFAULT_CELL_SIZE
        )

        isShowGrid = typedArray.getBoolean(
            R.styleable.scalable_field_view_showGrid, DEFAULT_IS_SHOW_GRID
        )
        gridLineColor = typedArray.getColor(
            R.styleable.scalable_field_view_grid_line_color, DEFAULT_GRID_LINE_COLOR
        )
        gridLineWidth = typedArray.getDimension(
            R.styleable.scalable_field_view_grid_line_width, DEFAULT_GRID_LINE_WIDTH
        )
//        scale = typedArray.getFloat(
//            R.styleable.scalable_field_view_scale_field, DEFAULT_SCALE
//        )

        typedArray.recycle()
    }

    private val gridLineConfig: PaintConfig = {
        reset()
        style = Paint.Style.STROKE
        strokeWidth = gridLineWidth
        color = gridLineColor
    }

    override fun onDraw(canvas: Canvas) {
        if (isShowGrid) {
            drawGrid(canvas)
        }
    }

    private fun drawGrid(canvas: Canvas) {
        val width = 100 * cellSize
        val height = 100 * cellSize
//        val center =
//            PointF(translationPointF.x - width / 2, translationPointF.y - height / 2)

        var x = 0f
        var y = 0f

        paint.gridLineConfig()
        path.reset()

        while (x <= width) {
            path.moveTo(+x, 0f)
            path.lineTo(x, height)
            x += cellSize
        }
        while (y <= height) {
            path.moveTo(0f, y)
            path.lineTo(width, y)
            y += cellSize
        }

        path.transform(drawMatrix)
        canvas.drawPath(path, paint)
    }

    inline fun invalidateAfter(block: ScalableFieldView.() -> Unit) {
        this.block()
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)//detector.onTouchEvent(event) || scaleDetector.onTouchEvent(event)
    }

    fun setOnFocusPointChangeListener(listener: OnFocusPointChangeListener) {
        onFocuspointChange = listener
    }

    fun setOnScaleChangeListener(listener: OnScaleChangeListener) {
        onScaleChange = listener
    }
}