package com.alexeykatsuro.scalablefieldview.utils

import android.content.res.Resources

val Float.toDp: Float
    get() = (this / Resources.getSystem().displayMetrics.density)
val Float.toPx: Float
    get() = (this * Resources.getSystem().displayMetrics.density)