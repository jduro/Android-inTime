package com.code;

import java.io.Serializable;


/**
 * Encapsulates information about a news entry
 */
public final class BusEntry implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String label;
	private String timeleft;
	private int icon;

	public BusEntry(String id, String name, String label, String timeleft, int icon) {
		this.id = id;
		this.name = name;
		this.label = label;
		this.timeleft = timeleft;
		this.icon = icon;
	}
	
	public BusEntry(String id, String name,String label) {
		super();
		this.id = id;
		this.name = name;
		this.label = label;
	}
	
	public BusEntry(String id, String name,String label,String timeleft) {
		super();
		this.id = id;
		this.name = name;
		this.label = label;
		this.timeleft=timeleft;
	}

	/**
	 * @return Title of news entry
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @return Author of news entry
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return Author of news entry
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return Post date of news entry
	 */
	public String getTimeleft() {
		return timeleft;
	}

	/**
	 * @return Icon of this news entry
	 */
	public int getIcon() {
		return icon;
	}

}