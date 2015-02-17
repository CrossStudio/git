package com.example.news;

public class NewsResource {
	
	private String name;
	
	private String URL;
	
	private int logoID;
	
	private int positionInList;
	
	public NewsResource(String name, String URL, int logoID){
		this.name = name;
		this.URL = URL;
		this.logoID = logoID;
	}
	
	/**
	 * @return name of the news resource
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @return url of the news resource
	 */
	public String getURL(){
		return URL;
	}
	
	/**
	 * @return id of the logo of the news resource
	 */
	public int getLogoID(){
		return logoID;
	}
	
	/**
	 * @return position of the news resource in current list of resources
	 */
	public int getPositionInList(){
		return positionInList;
	}
	
	/**
	 * Sets the news resource's position id
	 * @param position - the news resource's position id to be set
	 */
	public void setPositionInList(int position){
		this.positionInList = position;
	}
}
