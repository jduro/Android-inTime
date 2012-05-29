package com.code;

import java.io.Serializable;


/**
 * Encapsulates information about a news entry
 */
public final class StopEntry implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String arrivalTime;
	private String lon;
	private String lat;
	
	
	public StopEntry(String id, String name, String arrivalTime, String lon,
			String lat) {
		super();
		this.id = id;
		this.name = name;
		this.arrivalTime = arrivalTime;
		this.lon = lon;
		this.lat = lat;
	}

	public StopEntry(String id, String name, String lon, String lat) {
		super();
		this.id = id;
		this.name = name;
		this.lon = lon;
		this.lat = lat;
	}




	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getArrivalTime() {
		return arrivalTime;
	}


	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}


	public String getLon() {
		return lon;
	}


	public void setLon(String lon) {
		this.lon = lon;
	}


	public String getLat() {
		return lat;
	}


	public void setLat(String lat) {
		this.lat = lat;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}