package com.kenargo.mapwidgetsampleapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kenargo.map_widgets.WidgetMapCenterLock;
import com.kenargo.map_widgets.WidgetMapType;

public class ControlsJavaPreview extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controls_preview);

        final WidgetMapType widgetMapType = findViewById(R.id.widgetMapType);

        widgetMapType.setOnSelectionChange(mapDisplayType -> {
            Toast.makeText(this, "clicked: " + mapDisplayType, Toast.LENGTH_LONG).show();
        });

        final WidgetMapCenterLock widgetMapCenterLock = findViewById(R.id.widgetMapCenterLock);

        widgetMapCenterLock.setOnSelectionChange(mapCenterLockType -> {
            Toast.makeText(this, "clicked: " + mapCenterLockType, Toast.LENGTH_LONG).show();
        });
    }
}
