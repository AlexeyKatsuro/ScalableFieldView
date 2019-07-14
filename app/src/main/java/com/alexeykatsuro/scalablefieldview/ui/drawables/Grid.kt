package com.alexeykatsuro.scalablefieldview.ui.drawables

import android.graphics.*
import com.alexeykatsuro.scalablefieldview.utils.PaintConfig
import com.alexeykatsuro.scalablefieldview.utils.toPx
import kotlin.math.floor

class Grid(
    val cellSize: Float,
    override var scale: Float,
    override var translation: PointF,
    paintConfig: PaintConfig = defaultConfig
) : ScalableDrawable() {

    companion object {
        private val defaultConfig: PaintConfig = {
            reset()
            style = Paint.Style.STROKE
            strokeWidth = 1f.toPx
            color = Color.GRAY
        }
        private val path = Path()
        private val paint = Paint()
    }


    init {
        paint.apply(paintConfig)
    }

    override fun draw(canvas: Canvas) {
        path.reset()
        val cellSize = cellSize*scale
        val width = bounds.width().toFloat()
        val height = bounds.height().toFloat()

        val yOffset = calculateOffset(translation.y, cellSize)
        val xOffset = calculateOffset(translation.x, cellSize)

        var x = xOffset
        var y = yOffset
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

        canvas.drawPath(path, paint)
    }

    private fun calculateOffset(translation: Float, cellSize: Float): Float {
        val cellCount = floor(translation / cellSize) + 1
        return cellSize - ((cellCount) * cellSize - translation)
    }

}



