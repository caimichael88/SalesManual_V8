package com.oupcanada.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;




import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Environment;
import android.util.Log;


public class OUPXMLParser {
	
	static final String BOOKS = "books";
    static final String BOOK = "book";
	static final String BOOK_INFO = "book_info";
	static final  String BOOK_TYPE = "book_type";
	static final  String PRI = "pri";
	static final  String ISBN = "isbn";
	static final  String OFF = "off";
	static final String PG= "pg";
	static final String FLAG= "flag";
	static final String AUTHOR= "author";
	static final String COLOR= "color";
	
	//tag for subject xml
	static final String SUBJECT= "sub";
	static final String ITEM= "item";
	
	static final String CATEGORY= "category";
	static final String CAT_TIME_STAMP=  "cat_time_stamp";
	static final String SEARCH_XML = "search_xml";
	static final String SEARCH_TIME_STAMP = "search_time_stamp";
	
	public static ArrayList<Book> parseBooks(String path)
			throws XmlPullParserException, IOException {
		ArrayList<Book> books = null;
		Book currentBook = null;
		boolean done = false;
		
		
		//added for the syncing feature
		//path= Constants.OUP_DATA_PATH + path + ".xml";
		//Log.d("OUP", "parseBooks path: "+ path);		
		//File file = new File(path);
		
		File pathDir= new File(Environment.getExternalStorageDirectory() + Constants.OUP_DATA_PATH);
	    File file = new File(pathDir, path + Constants.XML);
		
		
		if (file.exists()) {

			BufferedInputStream in = null;
			try {
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();

				in = new BufferedInputStream(new FileInputStream(file));
				xpp.setInput(in, "UTF-8");
				// xpp.next();
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT && !done)
    	    	{
    	    		String name= null;
    	    		
    	    		switch(eventType){
	    	    		case XmlPullParser.START_DOCUMENT:
							books = new ArrayList<Book>();
							break;
	    	    		case XmlPullParser.START_TAG:
							name = xpp.getName();
							if (name.equalsIgnoreCase(BOOK)){
								currentBook = new Book();
							} else if (currentBook != null){
								if (name.equalsIgnoreCase(BOOK_INFO)){
									currentBook.setBookInfo(xpp.nextText());
								} else if (name.equalsIgnoreCase(BOOK_TYPE)){
									currentBook.setBookType(xpp.nextText());
								} else if (name.equalsIgnoreCase(PRI)){
									currentBook.setPri(xpp.nextText());
								} else if (name.equalsIgnoreCase(ISBN)){
									currentBook.setIsbn(xpp.nextText());
								}else if(name.equalsIgnoreCase(OFF)){
									currentBook.setOff(xpp.nextText());
								}else if(name.equalsIgnoreCase(PG)){
									currentBook.setPg(xpp.nextText());
								}else if(name.equalsIgnoreCase(FLAG)){
									currentBook.setFlag(xpp.nextText());
								}else if(name.equalsIgnoreCase(AUTHOR)){
									currentBook.setAuthor(xpp.nextText());
								}else if(name.equalsIgnoreCase(COLOR)){
									currentBook.setColor(xpp.nextText());
								}
							}
							break;
	    	    		case XmlPullParser.END_TAG:
							name = xpp.getName();
							if (name.equalsIgnoreCase(BOOK) && currentBook != null){
								//make sure the book is not empty
								if(!currentBook.getBookInfo().equals("")){
									books.add(currentBook);
									//Log.d("OUP", currentBook.toString());
								}
							} else if (name.equalsIgnoreCase(BOOKS)){
								done = true;
							}
							break;
    	    		}
    	    		
    	    		eventType = xpp.next();
    	    		
    	    		
    	    	}

			}

			finally {
				if (in != null) {
					in.close();
				}

			}

		}

		return books;
	}
	
	public static HashMap<String, ArrayList<String>> parseCategoryHashmapFromFile(Activity activity)
			throws XmlPullParserException, IOException {
		HashMap<String, ArrayList<String>> hashmap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> subList = null;
				
		//boolean done = false;
		
		//File file = new File(path);

		File pathDir= new File(Environment.getExternalStorageDirectory() + Constants.OUP_DATA_PATH);
		Log.d("OUP", "current state "+ Environment.getExternalStorageState());
		Log.d("OUP", "pathdir "+ Environment.getExternalStorageDirectory() + Constants.OUP_DATA_PATH);
	    File file = new File(pathDir, Constants.CATEGORY_LIST_NAME + Constants.XML);
		
		if (file.exists()) {
			
			BufferedInputStream in = null;
			try {
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();

				in = new BufferedInputStream(new FileInputStream(file));
				xpp.setInput(in, "UTF-8");
				// xpp.next();
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					//Log.d("OUP", "inside loop");
					String name = null;

					switch (eventType) {
					/*case XmlPullParser.START_DOCUMENT:
						Log.d("OUP", "start document");
						subList = new ArrayList<String>();
						break;*/
					case XmlPullParser.START_TAG:
						name = xpp.getName();
						//Log.d("OUP", "start tag name is "+ name);
						if (!name.equalsIgnoreCase(ITEM)) {
							subList = new ArrayList<String>();
							
						} else if (subList != null && name.equalsIgnoreCase(ITEM)){
							//Log.d("OUP", "VALUE "+ xpp.nextText());
							subList.add(xpp.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						name = xpp.getName();
						if (!name.equalsIgnoreCase(ITEM) ){
							//make sure the book is not empty
							hashmap.put(name, subList);
							//done = true;
						}
						break;
						
					}

					eventType = xpp.next();

				}

			}

			finally {
				if (in != null) {
					in.close();
				}

			}

		}else{
			Log.d("OUP", "File not existed");
		}
		
		return hashmap;
	}
	
	public static Status parseStatusFromFile()
			throws XmlPullParserException, IOException {
		
		Status status= null; 
		
		String catName= "";
		
		//File file = new File(path);
		
		File pathDir= new File(Environment.getExternalStorageDirectory() + Constants.OUP_DATA_PATH);
	    File file = new File(pathDir, Constants.STATUS_FILE + Constants.XML);
	    
		if (file.exists()) {

			BufferedInputStream in = null;
			try {
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();

				in = new BufferedInputStream(new FileInputStream(file));
				xpp.setInput(in, "UTF-8");
				// xpp.next();
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					//Log.d("OUP", "inside loop");
					String name = null;

					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						//Log.d("OUP", "start document");
						status = new Status();
						break;
					case XmlPullParser.START_TAG:
						name = xpp.getName();
						//Log.d("OUP", "start tag name is "+ name);
						
						//Log.d("OUP", xpp.nextText());
					
						if(name.equalsIgnoreCase(CATEGORY)){
							status.setCategory(xpp.nextText());
						}else if(name.equalsIgnoreCase(CAT_TIME_STAMP)){
							status.setCat_time_stamp(xpp.nextText());
						}else if(name.equalsIgnoreCase(SEARCH_XML)){
							status.setSearch_xml(xpp.nextText());
						}else if(name.equalsIgnoreCase(SEARCH_TIME_STAMP)){
							status.setSearch_time_stamp(xpp.nextText());
						}
						
						break;
					case XmlPullParser.END_TAG:						
						break;
						
					}

					eventType = xpp.next();

				}

			}

			finally {
				if (in != null) {
					in.close();
				}

			}

		}/*else
			Log.d("OUP", "File not existed at "+ path);*/

		return status;
	}
	
	/*public static ArrayList<Book> parse(Activity activity, int xmlID)
    	    throws XmlPullParserException, IOException
    	    {
    	ArrayList<Book> books = null;
     	    	Resources res = activity.getResources();
    	    	Book currentBook = null;
    			boolean done = false;
    	    	XmlResourceParser xpp = res.getXml(xmlID);
    	    	xpp.next();
    	    	int eventType = xpp.getEventType();
    	    	while (eventType != XmlPullParser.END_DOCUMENT && !done)
    	    	{
    	    		String name= null;
    	    		
    	    		switch(eventType){
	    	    		case XmlPullParser.START_DOCUMENT:
							books = new ArrayList<Book>();
							break;
	    	    		case XmlPullParser.START_TAG:
							name = xpp.getName();
							if (name.equalsIgnoreCase(BOOK)){
								currentBook = new Book();
							} else if (currentBook != null){
								if (name.equalsIgnoreCase(BOOK_INFO)){
									currentBook.setBookInfo(xpp.nextText());
								} else if (name.equalsIgnoreCase(LEVEL)){
									currentBook.setLevel(xpp.nextText());
								} else if (name.equalsIgnoreCase(EDITION)){
									currentBook.setEditon(xpp.nextText());
								} else if (name.equalsIgnoreCase(YEAR)){
									currentBook.setYear(xpp.nextText());
								}else if(name.equalsIgnoreCase(PUB)){
									currentBook.setPub(xpp.nextText());
								}else if(name.equalsIgnoreCase(ISBN)){
									currentBook.setIsbn(xpp.nextText());
								}
							}
							break;
	    	    		case XmlPullParser.END_TAG:
							name = xpp.getName();
							if (name.equalsIgnoreCase(BOOK) && currentBook != null){
								//make sure the book is not empty
								if(!currentBook.getBookInfo().equals(""))
									books.add(currentBook);
							} else if (name.equalsIgnoreCase(BOOKS)){
								done = true;
							}
							break;
    	    		}
    	    		
    	    		eventType = xpp.next();
    	    		
    	    		
    	    	}

    	    	return books;
    	    }*/
	
	
	
	
		
	
	/*public static ArrayList<String> parseSubject(Activity activity, String path)
			throws XmlPullParserException, IOException {
		ArrayList<String> subList = null;
		Book currentSub = null;
		boolean done = false;

		
		File file = new File(path);
		if (file.exists()) {

			BufferedInputStream in = null;
			try {
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();

				in = new BufferedInputStream(new FileInputStream(file));
				xpp.setInput(in, "UTF-8");
				// xpp.next();
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT  && !done) {
					//Log.d("OUP", "inside loop");
					String name = null;

					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						//Log.d("OUP", "start document");
						subList = new ArrayList<String>();
						break;
					case XmlPullParser.START_TAG:
						name = xpp.getName();
						if (name.equalsIgnoreCase(SUBJECT)) {
							subList.add(xpp.nextText());
						} 
						break;
					case XmlPullParser.END_TAG:
						name = xpp.getName();
						if (name.equalsIgnoreCase(SUBJECT) ){
							//make sure the book is not empty
							done = true;
						}
						break;
						
					}

					eventType = xpp.next();

				}

			}

			finally {
				if (in != null) {
					in.close();
				}

			}

		}

		return subList;
	}*/
	
	/*public static ArrayList<String> parseOUPCategory(String path)
			throws XmlPullParserException, IOException {
		ArrayList<String> subList = null;
		Book currentSub = null;
		boolean done = false;

		
		File file = new File(path);
		if (file.exists()) {

			BufferedInputStream in = null;
			try {
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();

				in = new BufferedInputStream(new FileInputStream(file));
				xpp.setInput(in, "UTF-8");
				// xpp.next();
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT  && !done) {
					//Log.d("OUP", "inside loop");
					String name = null;

					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						//Log.d("OUP", "start document");
						subList = new ArrayList<String>();
						break;
					case XmlPullParser.START_TAG:
						name = xpp.getName();
						if (name.equalsIgnoreCase(SUBJECT)) {
							subList.add(xpp.nextText());
						} 
						break;
					case XmlPullParser.END_TAG:
						name = xpp.getName();
						if (name.equalsIgnoreCase(SUBJECT) ){
							//make sure the book is not empty
							done = true;
						}
						break;
						
					}

					eventType = xpp.next();

				}

			}

			finally {
				if (in != null) {
					in.close();
				}

			}

		}

		return subList;
	}*/
	
	/*public static ArrayList<String> parseValueFromFile(Activity activity, String path, String xmlName)
			throws XmlPullParserException, IOException {
		ArrayList<String> subList = null;
		
		boolean done = false;
		
		File file = new File(path);
		if (file.exists()) {

			BufferedInputStream in = null;
			try {
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();

				in = new BufferedInputStream(new FileInputStream(file));
				xpp.setInput(in, "UTF-8");
				// xpp.next();
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT && !done) {
					//Log.d("OUP", "inside loop");
					String name = null;

					switch (eventType) {
					
					case XmlPullParser.START_TAG:
						name = xpp.getName();
						//Log.d("OUP", "start tag name is "+ name);
						if (name.equalsIgnoreCase(xmlName)) {
							subList = new ArrayList<String>();
							
						} else if (subList != null && name.equalsIgnoreCase(ITEM)){
							//Log.d("OUP", "VALUE "+ xpp.nextText());
							subList.add(xpp.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						name = xpp.getName();
						if (name.equalsIgnoreCase(xmlName) ){
							//make sure the book is not empty
							done = true;
						}
						break;
						
					}

					eventType = xpp.next();

				}

			}

			finally {
				if (in != null) {
					in.close();
				}

			}

		}
		
		return subList;
	}*/
	
	
	
	
	/*public static ArrayList<OUPCat> parseCategoryFromFile(String path)
			throws XmlPullParserException, IOException {
		
		ArrayList<OUPCat> catList =  new ArrayList<OUPCat>(); 
		
		String catName= "";
		
		File file = new File(path);
		if (file.exists()) {
			//Log.d("OUP", "after");
			BufferedInputStream in = null;
			try {
				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();

				in = new BufferedInputStream(new FileInputStream(file));
				xpp.setInput(in, "UTF-8");
				// xpp.next();
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					//Log.d("OUP", "inside loop");
					String name = null;

					switch (eventType) {
					
					case XmlPullParser.START_TAG:
						name = xpp.getName();
						//Log.d("OUP", "start tag name is "+ name);
						if(!name.equalsIgnoreCase("item")){
							catName= name;
						}else{
							OUPCat cat= new OUPCat(catName, xpp.nextText());
							catList.add(cat);
						}
						
						break;
					case XmlPullParser.END_TAG:
						name = xpp.getName();
						if (name.equalsIgnoreCase(catName) ){
							//make sure the book is not empty
							catName= "";
						}
						break;
						
					}

					eventType = xpp.next();

				}

			}

			finally {
				if (in != null) {
					in.close();
				}

			}

		}/else
			Log.d("OUP", "File not existed at "+ path);

		return catList;
	}*/
	


}
