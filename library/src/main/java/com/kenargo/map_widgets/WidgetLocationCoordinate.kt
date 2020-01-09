package com.kenargo.map_widgets

import android.content.Context
import android.text.*
import android.util.*
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.widget_location_coordinate.view.*
import java.security.InvalidParameterException
import java.util.*
import kotlin.math.*

class WidgetLocationCoordinate @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        initSubView(context, attrs, defStyleAttr)
    }

    private var lastValidLatitude = 0.0
    private var lastValidLongitude = 0.0


    private var onLocationChangeListener: Interfaces.MapLocationUpdateChange? = null

    fun setOnLocationChange(listener: Interfaces.MapLocationUpdateChange) {
        onLocationChangeListener = listener
    }

    private fun initSubView(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) {
        LayoutInflater.from(context).inflate(R.layout.widget_location_coordinate, this, true)

        applyAttributes(context, attrs, defStyleAttr)

        if (isInEditMode) {
            return
        }

        editTextLatitude.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (isLatitudeValid) {
                    // Now call the creator with the new selection
                    val newLocation = GeoLocation(
                        editTextLatitude.text.toString().toDouble(), editTextLongitude.text.toString().toDouble()
                    )

                    onLocationChangeListener?.onLocationChange(newLocation)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        editTextLongitude.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (isLongitudeValid) {
                    // Now call the creator with the new selection
                    val newLocation = GeoLocation(
                        editTextLatitude.text.toString().toDouble(), editTextLongitude.text.toString().toDouble()
                    )

                    onLocationChangeListener?.onLocationChange(newLocation)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        joystickView.setJoystickChangeListener { power, degree ->

            try {
                val newLocation = calculateMove(
                    GeoLocation(
                        editTextLatitude.text.toString().toDouble(), editTextLongitude.text.toString().toDouble()
                    ), degree.toDouble() - 90, (COORDINATE_OFFSET_METERS * power)
                )

                setCoordinates(newLocation.latitude, newLocation.longitude)

                // Now call the creator with the new selection
                onLocationChangeListener?.onLocationChange(newLocation)

            } catch (e: InvalidParameterException) {
                // Shouldn't be possible but maybe at extreme points
            }
        }
    }

    private fun applyAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {

        if (attrs == null) {
            return
        }

        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.WidgetLocationCoordinate, defStyleAttr, 0
        )

        try {
            for (index in 0 until typedArray.length()) {

                val attribute = try {
                    typedArray.getIndex(index)
                } catch (ignore: Exception) {
                    continue
                }

                when (attribute) {
                    R.styleable.WidgetLocationCoordinate_widgetLocationCoordinateLatitude -> {
                        editTextLatitude.text = typedArray.getString(
                            R.styleable.WidgetLocationCoordinate_widgetLocationCoordinateLatitude
                        )?.toEditable()
                    }
                    R.styleable.WidgetLocationCoordinate_widgetLocationCoordinateLatitudePattern -> {
                        editTextLatitude.pattern = typedArray.getString(
                            R.styleable.WidgetLocationCoordinate_widgetLocationCoordinateLatitudePattern
                        )
                    }
                    R.styleable.WidgetLocationCoordinate_widgetLocationCoordinateLongitude -> {
                        editTextLongitude.text = typedArray.getString(
                            R.styleable.WidgetLocationCoordinate_widgetLocationCoordinateLongitude
                        )?.toEditable()
                    }
                    R.styleable.WidgetLocationCoordinate_widgetLocationCoordinateLongitudePattern -> {
                        editTextLongitude.pattern = typedArray.getString(
                            R.styleable.WidgetLocationCoordinate_widgetLocationCoordinateLongitudePattern
                        )
                    }
                }
            }
        } finally {
            typedArray.recycle()
        }
    }

    @Throws(InvalidParameterException::class)
    fun setCoordinates(latitude: Double, longitude: Double) {

        // Set longitude first so that any error in latitude is displayed first
        setLongitude(longitude)
        setLatitude(latitude)
    }

    @Throws(InvalidParameterException::class)
    fun setLatitude(latitude: Double) {
        editTextLatitude.text = String.format(Locale.getDefault(), "%.06f", latitude).toEditable()
        lastValidLatitude = latitude
    }

    val isLatitudeValid: Boolean
        get() {
            return if (!editTextLatitude.pattern.isNullOrBlank() && !editTextLatitude.isTextEqualToPattern) {

                editTextLatitude.error = context.getString(R.string.widgetLocationCoordinateMissingOrInvalidLatitude)
                false
            } else {
                true
            }
        }

    val isLongitudeValid: Boolean
        get() {
            return if (!editTextLatitude.pattern.isNullOrBlank() && !editTextLongitude.isTextEqualToPattern) {

                editTextLongitude.error = context.getString(R.string.widgetLocationCoordinateMissingOrInvalidLongitude)
                false
            } else {
                true
            }
        }

    @Throws(InvalidParameterException::class)
    fun setLongitude(longitude: Double) {
        editTextLongitude.text = String.format(Locale.getDefault(), "%.06f", longitude).toEditable()
        lastValidLongitude = longitude
    }

    companion object {
        private const val COORDINATE_OFFSET_METERS = .005

        data class GeoLocation(val latitude: Double, val longitude: Double) {
            override fun toString(): String {
                return " Latitude: " + String.format(Locale.getDefault(), "%.06f", latitude) + ", Longitude: " + String.format(
                    Locale.getDefault(), "%.06f", longitude
                )
            }
        }

        fun calculateMove(
            centerLocation: GeoLocation, fromBearing: Double, distanceMeters: Double
        ): GeoLocation {

            val bearingTheta = Math.toRadians(fromBearing % 360)

            val latitudeInRadians = Math.toRadians(centerLocation.latitude)

            val longitudeInRadians = Math.toRadians(centerLocation.longitude)

            // Radius of the earth's center
            val radiusOfEarth = 6371000.0

            val newLatitudeInRadians = asin(
                sin(latitudeInRadians) * cos(distanceMeters / radiusOfEarth) + cos(
                    latitudeInRadians
                ) * sin(distanceMeters / radiusOfEarth) * cos(bearingTheta)
            )

            val newLongitudeInRadians = longitudeInRadians - atan2(
                sin(bearingTheta) * sin(distanceMeters / radiusOfEarth) * cos(
                    latitudeInRadians
                ), cos(distanceMeters / radiusOfEarth) - sin(latitudeInRadians) * sin(
                    newLatitudeInRadians
                )
            )

            return GeoLocation(
                Math.toDegrees(newLatitudeInRadians), Math.toDegrees(newLongitudeInRadians)
            )
        }
    }
}