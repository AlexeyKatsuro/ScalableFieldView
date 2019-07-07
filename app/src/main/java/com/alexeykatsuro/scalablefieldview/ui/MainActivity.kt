package com.alexeykatsuro.scalablefieldview.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.alexeykatsuro.scalablefieldview.R
import com.alexeykatsuro.scalablefieldview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_main)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        with(binding){

            field.setOnFocusPointChangeListener{ old, new ->
                textPoint.text = "Focuse x: ${new.x}, y: ${new.y}"
            }
            field.setOnScaleChangeListener {
                textScale.text = "scale: $it"
            }

            scaleBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val realProcess = progress + 1
                    textScale.text = "scale: $realProcess"
                    //field.scaleX = progress.toFloat()
                   // field.scaleY = progress.toFloat()
                    field.invalidateAfter {
                        //scale = realProcess.toFloat()
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit

            })
        }
    }
}
