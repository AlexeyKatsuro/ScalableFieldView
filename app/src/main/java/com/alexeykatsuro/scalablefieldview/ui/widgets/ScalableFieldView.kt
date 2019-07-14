package com.alexeykatsuro.scalablefieldview.ui.widgets

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import com.alexeykatsuro.scalablefieldview.R
import com.alexeykatsuro.scalablefieldview.ui.drawables.Grid
import com.alexeykatsuro.scalablefieldview.ui.drawables.ScalableDrawable
import com.alexeykatsuro.scalablefieldview.ui.drawables.Translatable
import com.alexeykatsuro.scalablefieldview.utils.PaintConfig
import com.alexeykatsuro.scalablefieldview.utils.copy
import com.alexeykatsuro.scalablefieldview.utils.toPx


typealias OnFocusPointChangeListener = (old: PointF, new: PointF) -> Unit
typealias OnScaleChangeListener = (scale: Float) -> Unit

class ScalableFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes), Translatable {
    companion object {
        val DEFAULT_CELL_SIZE = 5f.toPx

        const val DEFAULT_IS_SHOW_GRID = true
        const val DEFAULT_SCALE = 1f
    }

    private var onFocuspointChange: OnFocusPointChangeListener? = null
    private var onScaleChange: OnScaleChangeListener? = null

    private val paint = Paint()
    private val path = Path()
    private val drawMatrix = Matrix()

    var cellSize: Float
    override var scale: Float = DEFAULT_SCALE
    override var translation: PointF = PointF(0f, 0f)

    val drawables: MutableList<ScalableDrawable> = mutableListOf()
    val gridDrawable: ScalableDrawable
    /*@ColorInt
    var gridLineColor: Int
    var gridLineWidth: Float*/

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
        /* gridLineColor = typedArray.getColor(
             R.styleable.scalable_field_view_grid_line_color, DEFAULT_GRID_LINE_COLOR
         )
         gridLineWidth = typedArray.getDimension(
             R.styleable.scalable_field_view_grid_line_width, DEFAULT_GRID_LINE_WIDTH
         )*/
        scale = typedArray.getFloat(
            R.styleable.scalable_field_view_scale_field, DEFAULT_SCALE
        )

        gridDrawable = Grid(cellSize, scale, translation)
        typedArray.recycle()
    }

    private val gestureListener = object : OnMoveGestureListener {

        override fun onDrag(dx: Float, dy: Float) {
            Log.d("onDrag", "dx $dx, dy $dy")
            translation.x -= dx
            translation.y -= dy
            //drawMatrix.postTranslate(-dx, -dy)
            Log.d("onDrag", "translation x:${translation.x}, y ${translation.y}")
            gridDrawable.translation = translation
            invalidate()
        }

        override fun onScale(scaleFactor: Float, focusX: Float, focusY: Float) {
            scale += scaleFactor - 1
            gridDrawable.scale = scale
            invalidate()
            //drawMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY)
        }

    }
    private val gestureDetector: MovementGestureDetector =
        MovementGestureDetector(context, gestureListener)

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (changed) {
            gridDrawable.setBounds(left, top, right, bottom)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (isShowGrid) {

            gridDrawable.draw(canvas)
            // drawGrid(canvas)
        }
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