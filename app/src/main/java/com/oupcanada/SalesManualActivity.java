package com.oupcanada;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.oupcanada.search.DictionaryDatabase;
import com.oupcanada.search.DictionaryProvider;
import com.oupcanada.search.WordActivity;
import com.oupcanada.util.CustomListAdapter;
import com.oupcanada.util.OUPXMLParser;
import com.oupcanada.util.Constants;
import com.oupcanada.util.Status;
import com.oupcanada.util.Tools;

import com.oupcanada.R;

public class SalesManualActivity extends ListActivity {
	
	//private OUPCategoryDB oupCategoryDB;
	//private static final String CAT_VALUE="category";
	//private ProgressDialog pd = null;
	
	private TextView mTextView;
    private ListView mListView;
	
	HashMap<String, ArrayList<String>> oupCatHashmap= null;
	
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /*SharedPreferences prefs = getSharedPreferences("ShortCutPrefs", MODE_PRIVATE);
        if(!prefs.getBoolean("isFirstTime", false)){
            addShortcut();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstTime", true);
            editor.commit();g
        } */
        
        //oupCategoryDB=  new OUPCategoryDB(this);        
        //update category DB according to the status XML  
 
        
        //String categoryListXMLFile= Constants.OUP_DATA_PATH +  Constants.CATEGORY_LIST_NAME + Constants.XML;          
    	//String statusPath = Constants.OUP_DATA_PATH + Constants.STATUS_FILE + Constants.XML;
       
    	 
        Status status = getStatus();
        SalesManualApp theApp= ((SalesManualApp) getApplicationContext());
        theApp.setStatus(status);   
        
        mTextView = (TextView) findViewById(R.id.text);
        mListView = (ListView) findViewById(R.id.list);
       
        //File file= Environment.getExternalStoragePublicDirectory(".oupcanada2");
       
      
 
        try {
			oupCatHashmap= OUPXMLParser.parseCategoryHashmapFromFile(this);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        
        if(oupCatHashmap!=null){
        	theApp.setCategoryHashmap(oupCatHashmap);
        	ArrayList<String> categoryList= oupCatHashmap.get("cat");
        	//ArrayAdapter<String> arrAdapt= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categoryList);
        	setContentView(R.layout.category);
        	CustomListAdapter arrAdapt = new CustomListAdapter(this, R.layout.custom_list_text,  categoryList);
        	
        	setListAdapter(arrAdapt);	           
	        getListView().setTextFilterEnabled(true);
	        
	        handleIntent(getIntent());
        }else{
        	Log.d("OUP", "category is null");
        }
        	
        /*if(!getSharedPreferences(Constants.APP_PREFERENCE, this.MODE_PRIVATE).getBoolean(Constants.IS_ICON_CREATED, false)){
            addShortcut();
            getSharedPreferences(Constants.APP_PREFERENCE, this.MODE_PRIVATE).edit().putBoolean(Constants.IS_ICON_CREATED, true);
        }*/
        
        
        
        
    }
	
	
	 
	
	
	
	 @Override
	    protected void onNewIntent(Intent intent) {
	        // Because this activity has set launchMode="singleTop", the system calls this method
	        // to deliver the intent if this actvity is currently the foreground activity when
	        // invoked again (when the user executes a search from this activity, we don't create
	        // a new instance of this activity, so the system delivers the search intent here)
	        handleIntent(intent);
	    }

	    private void handleIntent(Intent intent) {
	        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
	            // handles a click on a search suggestion; launches activity to show word
	            Intent wordIntent = new Intent(this, WordActivity.class);
	            wordIntent.setData(intent.getData());
	            startActivity(wordIntent);
	            finish();
	        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	        	/*Log.d("OUP", "action search");
	            // handles a search query
	            String query = intent.getStringExtra(SearchManager.QUERY);
	            showResults(query);*/
	        }
	    }

	    /**
	     * Searches the dictionary and displays results for the given query.
	     * @param query The search query
	     */
	    private void showResults(String query) {
	        Cursor cursor = managedQuery(DictionaryProvider.CONTENT_URI, null, null,
	                                new String[] {query}, null);

	        if (cursor == null) {
	            // There are no results
	            mTextView.setText(getString(R.string.no_results, new Object[] {query}));
	        } else {
	            // Display the number of results
	            int count = cursor.getCount();
	            //Log.d("OUP", "count: "+ count);
	            String countString = getResources().getQuantityString(R.plurals.search_results,
	                                    count, new Object[] {count, query});
	            mTextView.setText(countString);

	            // Specify the columns we want to display in the result
	            String[] from = new String[] { DictionaryDatabase.KEY_WORD,
	                                           DictionaryDatabase.KEY_DEFINITION };

	            // Specify the corresponding layout elements where we want the columns to go
	            int[] to = new int[] { R.id.word,
	                                   R.id.definition };

	            // Create a simple cursor adapter for the definitions and apply them to the ListView
	            SimpleCursorAdapter words = new SimpleCursorAdapter(this,
	                                          R.layout.result, cursor, from, to);
	            mListView.setAdapter(words);

	            // Define the on-click listener for the list items
	            mListView.setOnItemClickListener(new OnItemClickListener() {
	                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                    // Build the Intent used to open WordActivity with a specific word Uri
	                    Intent wordIntent = new Intent(getApplicationContext(), WordActivity.class);
	                    Uri data = Uri.withAppendedPath(DictionaryProvider.CONTENT_URI,
	                                                    String.valueOf(id));
	                    wordIntent.setData(data);
	                    startActivity(wordIntent);
	                }
	            });
	        }
	    }
	
	
	
	 protected void onListItemClick(ListView l, View v, int position, long id) {
		 String m_selectedItemText = (String) l.getItemAtPosition(position);
		 //double check whether the category has the subcategory first
		 
		 if(oupCatHashmap!=null){
			 
			 
			ArrayList<String> subList = oupCatHashmap.get(Tools
					.getXMLFileName(m_selectedItemText));
			// Log.d("OUP", "elements: " +subList.size());
			if (subList != null) {
				//Log.d("OUP", "it is not null");
				Intent intent = new Intent(this, SubLanding.class);

				intent.putExtra(Constants.MANUAL_SUBJECT, m_selectedItemText);

				startActivity(intent);
			} else {
				// go to the category Landing directly
				// Log.d("OUP", "it is null");

				String[] parentCats = { m_selectedItemText };

				String arrayName = Tools.getXMLFileName(m_selectedItemText);

				createCatLandingIntent(m_selectedItemText, parentCats,
						arrayName, this);

			}			 
	
			 
		 }
		 
		 
	     
	}
	 
	/*public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.main_menu, menu);
	
			return super.onCreateOptionsMenu(menu);
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
            default:
                return false;
        }
    }
	 
	
	
	/*
	 * For synchronization
	 */
	public Status getStatus(){
    	Status status= null;

        try {
        	status= OUPXMLParser.parseStatusFromFile();
        	
        	        		
        	
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        return status;
    }
	
	/*private class SyncTask extends AsyncTask<String, Void, Object> {
        protected Object doInBackground(String... args) {
            Log.d("OUP", "Background thread starting");

            oupCategoryDB.syncWithOUPFile();  

            return "replace this with new category object";
        }

        protected void onPostExecute(Object result) {
            // Pass the result data back to the main activity
        	//SalesManualActivity.this.data = result;

            if (SalesManualActivity.this.pd != null) {
            	SalesManualActivity.this.pd.dismiss();
            }
        }
   } */
	
	public void createCatLandingIntent(String m_selectedItemText, String[] parentCats, String xmlFileName, Context sourceIntent){
		//Log.d("OUP", "ParentCat: xmlFile name is "+ xmlFileName);
		Intent intent = new Intent(sourceIntent, CategoryLanding.class);
		
		intent.putExtra("parentCats", parentCats);
		
		intent.putExtra("xml_file", xmlFileName);
	
		startActivity(intent);
		
	}
	
	private void addShortcut() {
	    //Adding shortcut for MainActivity 
	    //on Home screen
	    /*Intent shortcutIntent = new Intent(getApplicationContext(),
	    		SalesManualActivity.class);

	    shortcutIntent.setAction(Intent.ACTION_MAIN);

	    Intent addIntent = new Intent();
	    addIntent
	            .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
	    addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "OUP Sales Manual");
	    addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
	            Intent.ShortcutIconResource.fromContext(getApplicationContext(),
	                    R.drawable.ic_launcher_3));

	    addIntent
	            .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
	    getApplicationContext().sendBroadcast(addIntent);*/
		
		Intent HomeScreenShortCut= new Intent(getApplicationContext(),
				SalesManualActivity.class);

        HomeScreenShortCut.setAction(Intent.ACTION_MAIN);
        HomeScreenShortCut.putExtra("duplicate", false);
        //shortcutIntent is added with addIntent
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, HomeScreenShortCut);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "AppName");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
            Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.drawable.ic_launcher_3));

        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT"); 

        getApplicationContext().sendBroadcast(addIntent);
	}
	 
	 
}