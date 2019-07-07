package com.alexeykatsuro.scalablefieldview.ui.widgets

interface OnMoveGestureListener {

    fun onDrag(dx: Float, dy: Float) = Unit

    fun onFling(
        startX: Float, startY: Float, velocityX: Float,
        velocityY: Float
    ) = Unit

    fun onScale(scaleFactor: Float, focusX: Float, focusY: Float) = Unit

}