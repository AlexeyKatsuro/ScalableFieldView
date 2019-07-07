package com.alexeykatsuro.scalablefieldview.utils

import android.graphics.PointF

fun PointF.copy(x: Float = this.x, y: Float = this.y) =
        PointF(x,y)