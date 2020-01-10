package com.kenargo.mapwidgetsampleapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kenargo.map_widgets.MapWidgetInterfaces
import kotlinx.android.synthetic.main.controls_preview.*

class ControlsKotlinPreview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.controls_preview)

        widgetClearAll.setOnClickListener {
            Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show()
        }

        widgetRecenter.setOnClickListener {
            Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show()
        }

        widgetMapType.setOnSelectionChange(MapWidgetInterfaces.WidgetMapTypeChange {
            Toast.makeText(this, "clicked: $it", Toast.LENGTH_LONG).show()
        })

        widgetMapCenterLock.setOnSelectionChange(MapWidgetInterfaces.WidgetMapCenterLockChange {
            Toast.makeText(this, "clicked: $it", Toast.LENGTH_LONG).show()
        })
    }

    override fun onResume() {
        super.onResume()

        // So the initial focus isn't on the location EditTexts
        widgetClearAll.requestFocus()
    }
}