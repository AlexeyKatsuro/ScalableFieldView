package com.alexeykatsuro.scalablefieldview.ui.widgets

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector

class MovementGestureDetector(context: Context, moveGestureListener: OnMoveGestureListener) {

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            moveGestureListener.onFling(e1.x, e1.y, velocityX, velocityY )
            return false
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            moveGestureListener.onDrag(distanceX,distanceY)
            return false
        }
    }
    private val detector: GestureDetector = GestureDetector(context, gestureListener)

    private val scaleGestureListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
//            focusPoint = PointF(detector.focusX, detector.focusY)
//            invalidate()
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor

            if (scaleFactor.isNaN() || scaleFactor.isInfinite())
                return false

            if (scaleFactor >= 0) {
              moveGestureListener.onScale(scaleFactor, detector.focusX, detector.focusY)
            }
            return true

        }
    }
    private val scaleDetector: ScaleGestureDetector = ScaleGestureDetector(context, scaleGestureListener)

    fun onTouchEvent(event: MotionEvent): Boolean {
        return detector.onTouchEvent(event) || scaleDetector.onTouchEvent(event)
    }
}