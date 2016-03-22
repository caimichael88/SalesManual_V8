package com.oupcanada;




import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;
import com.oupcanada.util.Status;

public class SalesManualApp extends Application {
	
	private ArrayList<String> subjectList;
	
	private Status status;
	
	private HashMap<String, ArrayList<String>> categoryHashmap;
	
	
	
	//private HashMap<String, ArrayList<String>> CatList;

	public HashMap<String, ArrayList<String>> getCategoryHashmap() {
		return categoryHashmap;
	}

	public void setCategoryHashmap(
			HashMap<String, ArrayList<String>> categoryHashmap) {
		this.categoryHashmap = categoryHashmap;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ArrayList getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(ArrayList subjectList) {
		this.subjectList = subjectList;
	}
	
	
	
	//HashMap<String, String> meMap=new HashMap<String, String>();

	
	

}
