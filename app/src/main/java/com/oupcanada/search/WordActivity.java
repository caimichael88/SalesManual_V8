/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oupcanada.search;

import java.io.File;

import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.oupcanada.R;
import com.oupcanada.SalesManualActivity;
import com.oupcanada.SubLanding;
import com.oupcanada.util.Constants;

/**
 * Displays a word and its definition.
 */
public class WordActivity extends Activity implements ActionBar.TabListener {
	
	private ActionBar bar;
	private View mActionBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newbook);
               
        Typeface tf=Typeface.createFromAsset(this.getAssets(), Constants.CUSTOM_FONT_FACE);
        
        //setContentView(R.layout.word);
        
        //Log.d("OUP", "WordActivity");

        /*ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        Uri uri = getIntent().getData();
        Cursor cursor = managedQuery(uri, null, null, null, null);

        if (cursor == null) {
            finish();
        } else {
            cursor.moveToFirst();
            
            //UI for the seach results
            TextView word = (TextView) findViewById(R.id.new_author);
            word.setTypeface(tf);
            word.setTextColor(Color.rgb(2, 39, 91));
            
        	TextView definition = (TextView) findViewById(R.id.new_bookInfo); 
        	definition.setTypeface(tf);

        	TextView bookType = (TextView) findViewById(R.id.new_booktype); 
        	bookType.setTypeface(tf);
        	
        	TextView priView = (TextView) findViewById(R.id.new_PRI);
        	priView.setTypeface(tf);
        	TextView isbnView = (TextView) findViewById(R.id.new_isbn);  
        	isbnView.setTypeface(tf);
        	TextView offView = (TextView) findViewById(R.id.new_OFF);
        	offView.setTypeface(tf);
        	
        	
            /*TextView word = (TextView) findViewById(R.id.word);
            TextView definition = (TextView) findViewById(R.id.definition);*/

           // int wIndex = cursor.getColumnIndexOrThrow(DictionaryDatabase.KEY_WORD);
        	int wIndex = cursor.getColumnIndexOrThrow(DictionaryDatabase.AUTHOR);
        	int typeIndex= cursor.getColumnIndexOrThrow(DictionaryDatabase.TYPE);
            int dIndex = cursor.getColumnIndexOrThrow(DictionaryDatabase.KEY_DEFINITION);
            int priIndex= cursor.getColumnIndex(DictionaryDatabase.PRI);
            int isbnIndex= cursor.getColumnIndex(DictionaryDatabase.ISBN);
            int offIndex= cursor.getColumnIndex(DictionaryDatabase.OFF);
            
            
            word.setText(cursor.getString(wIndex));
            
            final String isbnVal= cursor.getString(isbnIndex);
            String definitionval= cursor.getString(dIndex);
            definitionval= definitionval.trim();
            String bookInfoFileName= Environment.getExternalStorageDirectory() + Constants.OUP_DATA_PATH + isbnVal + ".pdf";
            //Log.d("OUP", "File name: "+ bookInfoFileName);
            
            //File file = new File("/sdcard/Android/data/Glossary.pdf");
        	File bookInfoFile = new File(bookInfoFileName);                	
        	if(bookInfoFile.exists()){
        		//Log.d("OUP", "File existed");
        		//set underline                	
            	SpannableString bookInfoText = new SpannableString(definitionval);
            	bookInfoText.setSpan(new UnderlineSpan(), 0, bookInfoText.length(), 0);               	
            	definition.setText(bookInfoText);
        	}else{
        		//Log.d("OUP", "File not existed");
        		definition.setText(definitionval);
        	}             		

        	definition.setTextColor(Color.rgb(2, 39, 91));
			//definition.setTextColor(Color.BLACK);
			definition.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// String fileName= "/sdcard/Android/data/" + isbnname +
					// ".pdf";
					String fileName = Environment.getExternalStorageDirectory() + Constants.OUP_DATA_PATH + isbnVal
							+ ".pdf";
					
					// File file = new
					// File("/sdcard/Android/data/Glossary.pdf");
					File file = new File(fileName);
					if (file.exists()) {
						Uri path = Uri.fromFile(file);
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(path, "application/pdf");
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

						try {
							startActivity(intent);
						} catch (ActivityNotFoundException e) {
							// start to an error page
							Toast.makeText(v.getContext(),
									"No Application Available to View PDF",
									Toast.LENGTH_SHORT).show();
						}
					}
				}
			});                       
        	
        	
			
			if(bookType!=null){
				bookType.setTextColor(Color.rgb(2, 39, 91));
            	//bookType.setTextColor(Color.BLACK);
            	bookType.setText(cursor.getString(typeIndex));
            }
           
			//Log.d("OUP", "Book Pri: "+ cursor.getString(priIndex));
            priView.setText(cursor.getString(priIndex));
            priView.setTextColor(Color.rgb(2, 39, 91));
            
            
            SpannableString bookISBNText = new SpannableString(isbnVal);
            bookISBNText.setSpan(new UnderlineSpan(), 0, bookISBNText.length(), 0); 
            isbnView.setText(bookISBNText);
            isbnView.setTextColor(Color.rgb(2, 39, 91));
            isbnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	
                	Uri uri = Uri.parse("http://www.oupcanada.com/catalog/"+ isbnVal+ ".html");
                	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                	try {
                        startActivity(intent);
                    } 
                    catch (ActivityNotFoundException e) {
                    	//start to an error page
                        Toast.makeText(getApplicationContext(), "No Application Available to View PDF", 
                            Toast.LENGTH_SHORT).show();
                    }
                	
                }
            }); 
            
            offView.setText(cursor.getString(offIndex));
            offView.setTextColor(Color.rgb(2, 39, 91));
        }
        
        bar = getActionBar();
        bar.removeAllTabs();
        String home= getResources().getString(R.string.backtohome);
        bar.addTab(bar.newTab().setText(home).setTabListener(this), false);
        mActionBarView = getLayoutInflater().inflate(
                R.layout.action_bar_custom, null);

        bar.setCustomView(mActionBarView);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayShowHomeEnabled(true);
        //bar.setSelectedNavigationItem(1);
       /* int selectedTag= categories.length-1;
        bar.setSelectedNavigationItem(selectedTag);*/

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        
        return true;
    }*/
    
    /*public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);

		return true;
	}
	
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
            	Log.d("OUP", "doing searchin...");
                onSearchRequested();
                return true;
            case android.R.id.home:
                Intent intent = new Intent(this, SearchableDictionary.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }*/
    
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);

		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                onSearchRequested();
                return true;
            case android.R.id.home:
                Intent intent = new Intent(this, SalesManualActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            default:
                return false;
        }
    }
	
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		bar = getActionBar();
		String selectedTabString= (String) bar.getSelectedTab().getText();
	
		String home = getResources().getString(R.string.backtohome);
		
		if(selectedTabString.equalsIgnoreCase(home)){
			Intent intent = new Intent(this, SalesManualActivity.class);
	        startActivity(intent);
			//finish();
		}
    }
	
	

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
    
   
}
