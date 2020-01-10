package com.kenargo.map_widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.*
import android.widget.*
import kotlinx.android.synthetic.main.widget_map_center_lock.view.*

class WidgetMapCenterLock @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        initSubView(context, attrs, defStyleAttr)
    }

    enum class MapCenterLockTypes(val value: Int) {

        NONE(0),
        PILOT(1),
        TAKEOFF(2),
        AIRCRAFT(3);

        companion object {
            fun findByValue(value: Int) = values().firstOrNull { it.value == value }
        }
    }

    var mapCenterLockType = MapCenterLockTypes.NONE
    var dismissOnSelection = false
    var displayOptionsFlags = 0xFF

    private var onSelectionChangeListener: MapWidgetInterfaces.WidgetMapCenterLockChange? = null

    fun setOnSelectionChange(listener: MapWidgetInterfaces.WidgetMapCenterLockChange) {
        onSelectionChangeListener = listener
    }

    private var mapCenterLockPopup: MapCenterLockPopup? = null

    val isPopupShowing: Boolean
        get() = (mapCenterLockPopup != null && mapCenterLockPopup!!.isShowing)

    private fun initSubView(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) {
        LayoutInflater.from(context).inflate(R.layout.widget_map_center_lock, this, true)

        applyAttributes(context, attrs, defStyleAttr)

        imageViewMoveTo.setOnClickListener {

            if (!isPopupShowing) {

                mapCenterLockPopup = MapCenterLockPopup(getContext(), mapCenterLockType)

                mapCenterLockPopup!!.showAsDropDown(this, (-mapCenterLockPopup!!.width), 0)

                mapCenterLockPopup!!.onSelectionChange = {
                    // Now call the creator with the new selection
                    onSelectionChangeListener?.onSelectionChange(it)
                }

                mapCenterLockPopup!!.setOnDismissListener {
                    mapCenterLockPopup = null
                }

            } else if (mapCenterLockPopup!!.isShowing) {

                mapCenterLockPopup!!.dismiss()
                mapCenterLockPopup = null
            }
        }
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {

        if (attrs == null) {
            return
        }

        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.WidgetMapCenterLock, defStyleAttr, 0
        )

        try {
            for (index in 0 until typedArray.length()) {

                val attribute = try {
                    typedArray.getIndex(index)
                } catch (ignore: Exception) {
                    continue
                }

                when (attribute) {
                    R.styleable.WidgetMapCenterLock_widgetMapCenterLockDismissOnSelection -> {
                        dismissOnSelection = typedArray.getBoolean(
                            R.styleable.WidgetMapCenterLock_widgetMapCenterLockDismissOnSelection, false
                        )
                    }
                    R.styleable.WidgetMapCenterLock_widgetMapCenterLockSelection -> {
                        mapCenterLockType = MapCenterLockTypes.findByValue(
                            typedArray.getInt(
                                R.styleable.WidgetMapCenterLock_widgetMapCenterLockSelection, MapCenterLockTypes.NONE.value
                            )
                        )!!
                    }
                    R.styleable.WidgetMapCenterLock_widgetMapCenterLockOptions -> {
                        displayOptionsFlags = typedArray.getInt(
                            R.styleable.WidgetMapCenterLock_widgetMapCenterLockOptions, 0xFF
                        )
                    }
                }
            }
        } finally {
            typedArray.recycle()
        }
    }

    private inner class MapCenterLockPopup internal constructor(
        context: Context?, mapCenterLockType: MapCenterLockTypes = MapCenterLockTypes.NONE
    ) : PopupWindow(
        View.inflate(context, R.layout.widget_map_center_lock_popup, null),
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        false
    ) {

        var onSelectionChange: ((MapCenterLockTypes?) -> Unit)? = null

        // I cannot use Kotlin extensions; apparently they don't resolve inner classes or classes in PopupWindow
        private val textViewPilot: TextView = contentView.findViewById(R.id.textViewPilot)
        private val linearLayoutPilot: View = contentView.findViewById(R.id.linearLayoutPilot)
        private val textViewTakeoff: TextView = contentView.findViewById(R.id.textViewTakeoff)
        private val linearLayoutTakeoff: View = contentView.findViewById(R.id.linearLayoutTakeoff)
        private val textViewAircraft: TextView = contentView.findViewById(R.id.textViewAircraft)
        private val linearLayoutAircraft: View = contentView.findViewById(R.id.linearLayoutAircraft)

        private fun setMapTypeSelected(selection: MapCenterLockTypes) {

            textViewPilot.setTextColor(
                if (selection == MapCenterLockTypes.PILOT) {
                    Color.WHITE
                } else {
                    Color.BLACK
                }
            )
            linearLayoutPilot.setBackgroundColor(
                if (selection == MapCenterLockTypes.PILOT) {
                    Color.BLUE
                } else {
                    Color.WHITE
                }
            )

            textViewTakeoff.setTextColor(
                if (selection == MapCenterLockTypes.TAKEOFF) {
                    Color.WHITE
                } else {
                    Color.BLACK
                }
            )
            linearLayoutTakeoff.setBackgroundColor(
                if (selection == MapCenterLockTypes.TAKEOFF) {
                    Color.BLUE
                } else {
                    Color.WHITE
                }
            )

            textViewAircraft.setTextColor(
                if (selection == MapCenterLockTypes.AIRCRAFT) {
                    Color.WHITE
                } else {
                    Color.BLACK
                }
            )
            linearLayoutAircraft.setBackgroundColor(
                if (selection == MapCenterLockTypes.AIRCRAFT) {
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
            linearLayoutPilot.visibility = if (displayOptionsFlags and 0x01 != 0) {
                View.VISIBLE
            } else {
                View.GONE
            }
            linearLayoutTakeoff.visibility = if (displayOptionsFlags and 0x02 != 0) {
                View.VISIBLE
            } else {
                View.GONE
            }
            linearLayoutAircraft.visibility = if (displayOptionsFlags and 0x03 != 0) {
                View.VISIBLE
            } else {
                View.GONE
            }

            linearLayoutPilot.setOnClickListener {
                setMapTypeSelected(MapCenterLockTypes.PILOT)
                onSelectionChange?.invoke(MapCenterLockTypes.PILOT)
                if (dismissOnSelection) {
                    dismiss()
                }
            }
            linearLayoutTakeoff.setOnClickListener {
                setMapTypeSelected(MapCenterLockTypes.PILOT)
                onSelectionChange?.invoke(MapCenterLockTypes.TAKEOFF)
                if (dismissOnSelection) {
                    dismiss()
                }
            }
            linearLayoutAircraft.setOnClickListener {
                setMapTypeSelected(MapCenterLockTypes.PILOT)
                onSelectionChange?.invoke(MapCenterLockTypes.AIRCRAFT)
                if (dismissOnSelection) {
                    dismiss()
                }
            }

            setMapTypeSelected(mapCenterLockType)
        }
    }
}