package com.kenargo.map_widgets

import android.content.Context
import android.graphics.Color
import android.util.*
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.widget_map_type.view.*

class WidgetMapType @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        initSubView(context, attrs, defStyleAttr)
    }

    enum class MapDisplayTypes(val value: Int) {

        NONE(0),
        NORMAL(1),
        SATELLITE(2),
        TERRAIN(3),
        HYBRID(4);

        companion object {
            fun findByValue(value: Int) = values().firstOrNull { it.value == value }
        }
    }

    var mapDisplayType = MapDisplayTypes.NORMAL
    var dismissOnSelection = false
    var displayOptionsFlags = 0xFF

    private var onSelectionChangeListener: MapWidgetInterfaces.WidgetMapTypeChange? = null

    fun setOnSelectionChange(listener: MapWidgetInterfaces.WidgetMapTypeChange) {
        onSelectionChangeListener = listener
    }

    private var mapTypeSettingPopup: MapTypeSettingPopup? = null

    val isPopupShowing: Boolean
        get() = (mapTypeSettingPopup != null && mapTypeSettingPopup!!.isShowing)

    private fun initSubView(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) {
        LayoutInflater.from(context).inflate(R.layout.widget_map_type, this, true)

        applyAttributes(context, attrs, defStyleAttr)

        imageViewMapType.setOnClickListener {

            if (!isPopupShowing) {

                mapTypeSettingPopup = MapTypeSettingPopup(getContext(), mapDisplayType)

                mapTypeSettingPopup!!.showAsDropDown(this, (-mapTypeSettingPopup!!.width), 0)

                mapTypeSettingPopup!!.onSelectionChange = {
                    // Now call the creator with the new selection
                    onSelectionChangeListener?.onSelectionChange(it)
                }

                mapTypeSettingPopup!!.setOnDismissListener {
                    mapTypeSettingPopup = null
                }

            } else {

                mapTypeSettingPopup!!.dismiss()
                mapTypeSettingPopup = null
            }
        }
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {

        if (attrs == null) {
            return
        }

        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.WidgetMapType, defStyleAttr, 0)

        try {
            for (index in 0 until typedArray.length()) {

                val attribute = try {
                    typedArray.getIndex(index)
                } catch (ignore: Exception) {
                    continue
                }

                when (attribute) {
                    R.styleable.WidgetMapType_widgetMapTypeDismissOnSelection -> {
                        dismissOnSelection = typedArray.getBoolean(
                            R.styleable.WidgetMapType_widgetMapTypeDismissOnSelection, false
                        )
                    }
                    R.styleable.WidgetMapType_widgetMapTypeSelection -> {
                        mapDisplayType = MapDisplayTypes.findByValue(
                            typedArray.getInt(
                                R.styleable.WidgetMapType_widgetMapTypeSelection, MapDisplayTypes.NONE.value
                            )
                        )!!
                    }
                    R.styleable.WidgetMapType_widgetMapTypeOptions -> {
                        displayOptionsFlags = typedArray.getInt(R.styleable.WidgetMapType_widgetMapTypeOptions, 0xFF)
                    }
                }
            }
        } finally {
            typedArray.recycle()
        }
    }

    private inner class MapTypeSettingPopup internal constructor(
        context: Context?, mapDisplayType: MapDisplayTypes = MapDisplayTypes.NONE
    ) : PopupWindow(
        View.inflate(context, R.layout.widget_map_type_popup, null),
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        false
    ) {

        var onSelectionChange: ((MapDisplayTypes?) -> Unit)? = null

        // I cannot use Kotlin extensions; apparently they don't resolve inner classes or classes in PopupWindow
        private val textViewNormal: TextView = contentView.findViewById(R.id.textViewNormal)
        private val linearLayoutNormal: View = contentView.findViewById(R.id.linearLayoutNormal)
        private val textViewSatellite: TextView = contentView.findViewById(R.id.textViewSatellite)
        private val linearLayoutSatellite: View = contentView.findViewById(R.id.linearLayoutSatellite)
        private val textViewTerrain: TextView = contentView.findViewById(R.id.textViewTerrain)
        private val linearLayoutTerrain: View = contentView.findViewById(R.id.linearLayoutTerrain)
        private val textViewHybrid: TextView = contentView.findViewById(R.id.textViewHybrid)
        private val linearLayoutHybrid: View = contentView.findViewById(R.id.linearLayoutHybrid)

        private fun setMapTypeSelected(selection: MapDisplayTypes) {

            textViewNormal.setTextColor(
                if (selection == MapDisplayTypes.NORMAL) {
                    Color.WHITE
                } else {
                    Color.BLACK
                }
            )
            linearLayoutNormal.setBackgroundColor(
                if (selection == MapDisplayTypes.NORMAL) {
                    Color.BLUE
                } else {
                    Color.WHITE
                }
            )

            textViewSatellite.setTextColor(
                if (selection == MapDisplayTypes.SATELLITE) {
                    Color.WHITE
                } else {
                    Color.BLACK
                }
            )
            linearLayoutSatellite.setBackgroundColor(
                if (selection == MapDisplayTypes.SATELLITE) {
                    Color.BLUE
                } else {
                    Color.WHITE
                }
            )

            textViewTerrain.setTextColor(
                if (selection == MapDisplayTypes.TERRAIN) {
                    Color.WHITE
                } else {
                    Color.BLACK
                }
            )
            linearLayoutTerrain.setBackgroundColor(
                if (selection == MapDisplayTypes.TERRAIN) {
                    Color.BLUE
                } else {
                    Color.WHITE
                }
            )

            textViewHybrid.setTextColor(
                if (selection == MapDisplayTypes.HYBRID) {
                    Color.WHITE
                } else {
                    Color.BLACK
                }
            )
            linearLayoutHybrid.setBackgroundColor(
                if (selection == MapDisplayTypes.HYBRID) {
                    Color.BLUE
                } else {
                    Color.WHITE
                }
            )
        }

        init {

            // Don't set 'isOutsideTouchable = true' or the popup will not close when the user touches
            //  the top widget icon
            //isOutsideTouchable = true

            // Process the 'displayOptionsFlags'
            linearLayoutNormal.visibility = if (displayOptionsFlags and 0x01 != 0) {
                View.VISIBLE
            } else {
                View.GONE
            }
            linearLayoutSatellite.visibility = if (displayOptionsFlags and 0x02 != 0) {
                View.VISIBLE
            } else {
                View.GONE
            }
            linearLayoutTerrain.visibility = if (displayOptionsFlags and 0x03 != 0) {
                View.VISIBLE
            } else {
                View.GONE
            }
            linearLayoutHybrid.visibility = if (displayOptionsFlags and 0x04 != 0) {
                View.VISIBLE
            } else {
                View.GONE
            }

            linearLayoutNormal.setOnClickListener {
                setMapTypeSelected(MapDisplayTypes.NORMAL)
                onSelectionChange?.invoke(MapDisplayTypes.NORMAL)
                if (dismissOnSelection) {
                    dismiss()
                }
            }
            linearLayoutSatellite.setOnClickListener {
                setMapTypeSelected(MapDisplayTypes.SATELLITE)
                onSelectionChange?.invoke(MapDisplayTypes.SATELLITE)
                if (dismissOnSelection) {
                    dismiss()
                }
            }
            linearLayoutTerrain.setOnClickListener {
                setMapTypeSelected(MapDisplayTypes.TERRAIN)
                onSelectionChange?.invoke(MapDisplayTypes.TERRAIN)
                if (dismissOnSelection) {
                    dismiss()
                }
            }
            linearLayoutHybrid.setOnClickListener {
                setMapTypeSelected(MapDisplayTypes.HYBRID)
                onSelectionChange?.invoke(MapDisplayTypes.HYBRID)
                if (dismissOnSelection) {
                    dismiss()
                }
            }

            setMapTypeSelected(mapDisplayType)
        }
    }
}