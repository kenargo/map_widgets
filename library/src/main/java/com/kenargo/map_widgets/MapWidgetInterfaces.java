package com.kenargo.map_widgets;

public class MapWidgetInterfaces {

    ////////////////////////////////////////////////////////////////////
    // TODO: Is there really no way to have one callback to satisfy both??
    //  OK, there is but not until Kotlin 1.4; then I can use 'fun interface'
    //  see https://stackoverflow.com/questions/59525086/java-kotlin-callback-syntax-do-i-really-need-2-callback-definitions-after-conv/59527416#59527416
    //  MUST KEEP INTERFACES IN JAVA UNTIL KOTLIN 1.4
    ////////////////////////////////////////////////////////////////////

    public interface WidgetMapTypeChange {
        void onSelectionChange(WidgetMapType.MapDisplayTypes mapDisplayType);
    }

    public interface WidgetMapCenterLockChange {
        void onSelectionChange(WidgetMapCenterLock.MapCenterLockTypes mapCenterLockType);
    }

    public interface MapLocationUpdateChange {
        void onLocationChange(WidgetLocationCoordinate.Companion.GeoLocation location);
    }
}
