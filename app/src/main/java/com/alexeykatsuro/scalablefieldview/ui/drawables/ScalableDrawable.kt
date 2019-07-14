package com.alexeykatsuro.scalablefieldview.ui.drawables

import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

abstract class ScalableDrawable : Drawable(), Translatable{
    protected var alphaValue: Int = 0xFF

    override fun setAlpha(alpha: Int) {
        alphaValue = alpha
    }

    override fun getOpacity(): Int {
        return if (alphaValue != 0) PixelFormat.TRANSLUCENT else PixelFormat.TRANSPARENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) = Unit
}