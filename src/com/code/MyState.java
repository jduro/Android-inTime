package com.code;

import android.app.Application;
import android.location.LocationManager;

public class MyState extends Application {
	
	LocationManager locationManager;

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}
}
