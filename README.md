# Map Widgets Components Library

[![Crates.io](https://img.shields.io/crates/l/License)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![MinSdk: 17](https://img.shields.io/badge/minSdk-17-green.svg)](https://developer.android.com/about/versions/android-4.0)
[![write: Kotlin](https://img.shields.io/badge/write-Kotlin-orange.svg)](https://kotlinlang.org/)
[![](https://jitpack.io/v/kenargo/map_widgets.svg)](https://jitpack.io/#kenargo/map_widgets)

This is a collection of UI compound components that you can use with mapping applications.  There maybe other appropriate applications these are useful with too.

## Installation
*Step 1.* Add the JitPack repository to your project `build.gradle` file
```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
*Step 2.* Add the dependency in the form
```gradle
	dependencies {
	        implementation 'com.github.kenargo:map_widgets:${latest_version}'
	}

```

### Sample App Included shows Java and Kotlin use

- Full support of Android Dark Mode

#### See example images below

![](https://github.com/kenargo/map_widgets/blob/master/readmeImages/AllWidgets.png)

![](https://github.com/kenargo/map_widgets/blob/master/readmeImages/WidgetMapCenterLock.png)

![](https://github.com/kenargo/map_widgets/blob/master/readmeImages/WidgetMapType.png)

![](https://github.com/kenargo/map_widgets/blob/master/readmeImages/WidgetLocationCoordinate.png)

## Self documenting:

    <declare-styleable name="WidgetMapType">
        <attr name="widgetMapTypeSelection">
            <enum name="none" value="0" />
            <enum name="normal" value="1" />
            <enum name="satellite" value="2" />
            <enum name="terrain" value="3" />
            <enum name="hybrid" value="4" />
        </attr>

        <attr name="widgetMapTypeOptions" format="flags">
            <flag name="normal" value="0x01" />
            <flag name="satellite" value="0x02" />
            <flag name="terrain" value="0x04" />
            <flag name="hybrid" value="0x08" />
        </attr>

        <attr name="widgetMapTypeDismissOnSelection" format="boolean" />

    </declare-styleable>

    <declare-styleable name="WidgetMapCenterLock">
        <attr name="widgetMapCenterLockSelection">
            <enum name="none" value="0" />
            <enum name="pilot" value="1" />
            <enum name="takeoff" value="2" />
            <enum name="aircraft" value="3" />
        </attr>

        <attr name="widgetMapCenterLockOptions" format="flags">
            <flag name="pilot" value="0x01" />
            <flag name="takeoff" value="0x02" />
            <flag name="aircraft" value="0x04" />
        </attr>

        <attr name="widgetMapCenterLockDismissOnSelection" format="boolean" />

    </declare-styleable>

    <declare-styleable name="WidgetLocationCoordinate">
        <attr name="widgetLocationCoordinateLatitude" format="string" />
        <attr name="widgetLocationCoordinateLatitudePattern" format="string" />

        <attr name="widgetLocationCoordinateLongitude" format="string" />
        <attr name="widgetLocationCoordinateLongitudePattern" format="string" />

    </declare-styleable>

## ## Credits:

- https://github.com/AliAzaz/Edittext-Library
    - Used in WidgetLocationCoordinate
