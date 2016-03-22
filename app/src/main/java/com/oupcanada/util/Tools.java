package com.oupcanada.util;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Tools {
	
	

	public static String getXMLFileName(String cat){
		String name= cat.replaceAll(" - ", "_");
		
		name= name.replaceAll(" / ", "_");
		name= name.replaceAll(",", "");
		name= name.replaceAll("/", "_");
		name= name.replaceAll(" ", "_");
		name= name.replaceAll("'", "_");
		name= name.replaceAll("-", "_");
		name= name.toLowerCase();
	
		return name;
	}
	
	public static String getStringArrayName(String arrayName){
		String name= arrayName.replaceAll(" - ", "_");
		
		name= name.replaceAll(",", "");
		name= name.replaceAll("/", "_");
		name= name.replaceAll(" ", "_");
		name= name.replaceAll("-", "_");
	
		return name;
	}
	
	public static boolean isStringInArray(String name, String[] stringArray){
		List<String> stringList= Arrays.asList(stringArray);
		return stringList.contains(name);
	}
	
	public static String getXMLPrefixName(String name){
		String value= name.replaceAll(" - ", " ");
		
		value= value.replaceAll(",", " ");
		value= value.replaceAll("/", "");
		value= value.toLowerCase();
		StringTokenizer st= new StringTokenizer(value);
		
		String prefix= "";
		while(st.hasMoreTokens()){
			prefix= prefix +  st.nextToken().charAt(0);
		}
		
		
		//return prefix;
		return prefix.toLowerCase();
	}
	
	public static String getSingleXMLPrefixName(String name){
		String prefix= "";
		
		StringTokenizer strToken= new StringTokenizer(name);
		if(strToken.countTokens()>1){
			String value= name.replaceAll(" - ", " ");
			
			value= value.replaceAll(",", " ");
			value= value.replaceAll("/", "");
			value= value.toLowerCase();
			StringTokenizer st= new StringTokenizer(value);
			
			
			while(st.hasMoreTokens()){
				prefix= prefix +  st.nextToken().charAt(0);
			}
		}else
			prefix= name.toLowerCase();
		
		
		return prefix;
	}

}
