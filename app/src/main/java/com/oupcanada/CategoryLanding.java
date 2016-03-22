package com.oupcanada;

import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParserException;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.oupcanada.R;
import com.oupcanada.SalesManualActivity;
import com.oupcanada.search.DictionaryDatabase;
import com.oupcanada.search.DictionaryProvider;
import com.oupcanada.search.WordActivity;
import com.oupcanada.util.Book;
import com.oupcanada.util.BookAdapter;
import com.oupcanada.util.Constants;
import com.oupcanada.util.OUPXMLParser;
import com.oupcanada.util.SalesToolAdapter;


public class CategoryLanding extends ListActivity  implements ActionBar.TabListener{
	private ActionBar bar;
	
	private ArrayList<Book> books;
	
	private BookAdapter m_adapter;
	
	private SalesToolAdapter m_sales_tool_adapter;
	
	
	private View mActionBarView;
	
	private String[] parentCats;
	
	private TextView mTextView;
    private ListView mListView;
	
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
        
        mTextView = (TextView) findViewById(R.id.text);
        mListView = (ListView) findViewById(R.id.list);
        
        bar = getActionBar();
        
        try {
        	Bundle extras= getIntent().getExtras();
        	if(extras!=null){
        		parentCats= extras.getStringArray("parentCats");
        		if(parentCats!=null){
        			for(int i=0; i<parentCats.length; i++){
        				//Log.d("OUP", "parent cata: "+ parentCats[i]);
        				bar.addTab(bar.newTab().setText(parentCats[i]).setTabListener(this), false);
        			}
        			
        			bar.setCustomView(mActionBarView);
    		        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO);
    		        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    		        bar.setDisplayShowHomeEnabled(true);
    		        bar.setDisplayHomeAsUpEnabled(true);
    		        int selectedTag= parentCats.length-1;
    		        bar.setSelectedNavigationItem(selectedTag);
        		}
        		String xmlFileName= extras.getString("xml_file");
        		//Log.d("OUP", "file name: "+ xmlFileName);
        		books= OUPXMLParser.parseBooks(xmlFileName);
        	}
			
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        mActionBarView = getLayoutInflater().inflate(
                R.layout.action_bar_custom, null);

       
        if(books!=null){
        	if(parentCats[parentCats.length-1].equalsIgnoreCase(Constants.SALES_TOOL)){
        		
        		this.m_sales_tool_adapter = new SalesToolAdapter(this, R.layout.sales_tool, books);
            	setListAdapter(this.m_sales_tool_adapter);
        	}else{
        		this.m_adapter = new BookAdapter(this, R.layout.book, books);
            	setListAdapter(this.m_adapter);
        	}
        	
        	 handleIntent(getIntent());
        	
        }
        	
        
        //order the list
        /*this.m_adapter.sort(new Comparator<Book>() {
        	public int compare(Book object1, Book object2) {
        		return object1.getBookInfo().compareTo(object2.getBookInfo());
        	};
        });*/
        
        
        
    }
    
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
    	
    	String selectedTabString= (String) bar.getSelectedTab().getText();
    	//Log.d("OUP", "select Tab String "+ selectedTabString);
		if(selectedTabString.equalsIgnoreCase("Home")){
			Intent intent = new Intent(CategoryLanding.this, SalesManualActivity.class);
	        startActivity(intent);
	        
		}else {
			
			if(parentCats!=null && !selectedTabString.equalsIgnoreCase(parentCats[parentCats.length-1])){
				
				//get the TabString array
				
				int selectedTabPosition= 0;
				
				for(int i=0; i<parentCats.length; i++){
					if(parentCats[i].equalsIgnoreCase(selectedTabString))
						selectedTabPosition= i;
				}
				
				String[] selectedParentCats= new String[selectedTabPosition+1];
				
				for(int i=0; i<selectedTabPosition+1; i++){
					selectedParentCats[i]= parentCats[i];
					//Log.d("OUP", "Added Tab: "+ selectedParentCats[i]);
				}
				
				Intent intent = new Intent(CategoryLanding.this, SubLanding.class);
				intent.putExtra("parentCats", selectedParentCats);
				intent.putExtra("FromTab", "FromTab");
				// parameter specifically for fourth level nav
				/*
				 * if(parentCats[2]!=null && parentCats[2].length()>0){
				 * intent.putExtra("Level4Parent", parentCats[2]); }
				 */
				startActivity(intent);

				
				/*String[] categories= getResources().getStringArray(R.array.cat);
				for(int i=0; i<categories.length; i++){
					Log.d("OUP", "parent cats 1: "+ parentCats[1]);
					Log.d("OUP", "categories: "+ categories[i]);
					if(parentCats[1].equalsIgnoreCase(categories[i])){
						
						String m_selectedItemText= "com.oupcanada.cat."+ categories[i];
						 //Log.d("OUP", "m_selectedItemText " + m_selectedItemText);
						 
						 Class<?> nIntent = null;

						 try {
							nIntent = Class.forName(m_selectedItemText);
							
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 
						 intent = new Intent(CategoryLanding.this, nIntent);
						 intent.putExtra("selectedTabString", selectedTabString);
						 intent.putExtra("FromTab", "FromTab");	
						 //parameter specifically for fourth level nav
						 if(parentCats[2]!=null && parentCats[2].length()>0){
							 intent.putExtra("Level4Parent", parentCats[2]);
						 }
						 startActivity(intent);
					}
				}*/
				//original one
				/*if(parentCats[1].equalsIgnoreCase("Sociology")){
					intent = new Intent(CatLanding.this, Sociology.class);
					intent.putExtra("selectedTabString", selectedTabString);
					intent.putExtra("FromTab", "FromTab");
					startActivity(intent);
				}*/
	        
			}
			//Intent intent = new Intent(Sociology.this, SalesManualActivity.class);
	        //startActivity(intent);
			/*bar.removeAllTabs();
			bar.addTab(bar.newTab().setText("Home").setTabListener(this));
			Intent intent = new Intent(this, SalesManualActivity.class);
	        startActivity(intent);*/
		}
        
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
    
    
    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        ActionBar bar = getActionBar();
        if(bar.getSelectedTab()!= null){
        	int category = bar.getSelectedTab().getPosition();
            outState.putInt("category", category);
        }
        
    }
    
    protected void onResume(){
    	super.onResume();
    	//Log.d("OUP", "On resume from CatLanding");
    	if(bar.getSelectedTab()!=null){
    		bar.setSelectedNavigationItem(bar.getTabCount()-1);
    		//Log.d("OUP", "selected Tab: "+ bar.getSelectedTab().getText());
    		
    		//Tab tab= bar.getSelectedTab();
    		//bar.removeTabAt(bar.getSelectedNavigationIndex());
    		//bar.addTab(tab, bar.getSelectedNavigationIndex(), false);
    		//bar.selectTab(null);
    	}
    }
	
    
  //for search
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
          } /*else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
          	Log.d("OUP", "action search");
              // handles a search query
              String query = intent.getStringExtra(SearchManager.QUERY);
              showResults(query);
          }*/
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
  	//end for search
    
    
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
 
 
	

}