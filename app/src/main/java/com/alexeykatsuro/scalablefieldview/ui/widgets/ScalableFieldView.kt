package com.alexeykatsuro.scalablefieldview.ui.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.alexeykatsuro.scalablefieldview.R
import com.alexeykatsuro.scalablefieldview.utils.toPx

private typealias PaintConfig = Paint.() -> Unit

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

    private var paint = Paint()

    var scaleField: Float
    var cellSize: Float
    @ColorInt
    var gridLineColor: Int
    var gridLineWidth: Float

    var isShowGrid: Boolean


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
        scaleField = typedArray.getFloat(
            R.styleable.scalable_field_view_scale_field, DEFAULT_SCALE
        )

        typedArray.recycle()
    }

    private val gridLineConfig: PaintConfig = {
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
        var x = 0f
        var y = 0f

        paint.gridLineConfig()

        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()
        while (x < width) {
            canvas.drawLine(x, 0f, x, height, paint)
            x += cellSize
        }
        while (y < height) {
            canvas.drawLine(0f, y, width, y, paint)
            y += cellSize
        }
    }

}