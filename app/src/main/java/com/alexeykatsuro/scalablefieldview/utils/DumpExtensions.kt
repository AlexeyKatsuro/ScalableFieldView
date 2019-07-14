package com.alexeykatsuro.scalablefieldview.utils

import android.graphics.Paint
import android.graphics.PointF

typealias PaintConfig = Paint.() -> Unit

fun PointF.copy(x: Float = this.x, y: Float = this.y) =
        PointF(x,y)