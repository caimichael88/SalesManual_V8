package com.oupcanada.util;

public class Status {

	private String category;
	private String cat_time_stamp;
	private String search_xml;
	private String search_time_stamp;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCat_time_stamp() {
		return cat_time_stamp;
	}
	public void setCat_time_stamp(String cat_time_stamp) {
		this.cat_time_stamp = cat_time_stamp;
	}
	public String getSearch_xml() {
		return search_xml;
	}
	public void setSearch_xml(String search_xml) {
		this.search_xml = search_xml;
	}
	public String getSearch_time_stamp() {
		return search_time_stamp;
	}
	public void setSearch_time_stamp(String search_time_stamp) {
		this.search_time_stamp = search_time_stamp;
	}
	
	@Override
	public String toString() {
		return "Status [category=" + category + ", cat_time_stamp="
				+ cat_time_stamp + ", search_xml=" + search_xml
				+ ", search_time_stamp=" + search_time_stamp + "]";
	}
	
	
	
	
}
