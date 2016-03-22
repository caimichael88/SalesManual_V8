package com.oupcanada;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.ArrayAdapter;
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
import com.oupcanada.util.Constants;
import com.oupcanada.util.CustomListAdapter;
import com.oupcanada.util.OUPXMLParser;
import com.oupcanada.util.Tools;


public class SubLanding extends ParentCat{

	private View mActionBarView;
	
	private ActionBar bar;
	
	private String HOME;
	
	private ArrayList<String> subjectLandList= null;
	
	HashMap<String, ArrayList<String>> oupCatHashma= null;
	
	private TextView mTextView;
    private ListView mListView;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		//setContentView(R.layout.category);
        
        mTextView = (TextView) findViewById(R.id.text);
        mListView = (ListView) findViewById(R.id.list);
    
        
        Bundle extras= getIntent().getExtras();
        
        if(extras!=null){
        	String SUB= extras.getString(Constants.MANUAL_SUBJECT);
        	
        	if(SUB!=null){
        		        		                
                //get the categoryList from HashMap
                SalesManualApp theApp= ((SalesManualApp) getApplicationContext());
                oupCatHashma= theApp.getCategoryHashmap();
                
                if(oupCatHashma!=null){
                	subjectLandList= oupCatHashma.get(Tools.getXMLFileName(SUB));
                	
                	if(subjectLandList!=null){
                		//ArrayAdapter<String> arrAdapt= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subjectLandList);
                		CustomListAdapter arrAdapt = new CustomListAdapter(this, R.layout.custom_list_text,  subjectLandList);
            	        setListAdapter(arrAdapt);
         	           
            	        getListView().setTextFilterEnabled(true);
            	        
            	        handleIntent(getIntent());
            	        
                	}
                	
                }
                
                bar = getActionBar();
                
                HOME= getResources().getString(R.string.home);
            
                //String[] categories= {HOME, SUB};
                String[] categories= {SUB};
                createBar(bar, categories, mActionBarView);
        	}
        	
        }        
       
        
        
       /* SUB_PREFIX= SUB.toLowerCase()+ "_";
        
        if(extras!=null){
        	
        	try{
        		String subjectXMLFileName= Constants.OUP_XML_PATH + Constants.OUP_SUBJECT_FILE_NAME+ Constants.XML;
            	subjectLandList= OUPXMLParser.parseValueFromFile(this,  subjectXMLFileName, SUB);
    	        ArrayAdapter<String> arrAdapt= 
    	                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subjectLandList);
    	        
    	        setListAdapter(arrAdapt);
 	           
    	        getListView().setTextFilterEnabled(true);
            }catch (XmlPullParserException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }*/
        
        
        
        /*setListAdapter(ArrayAdapter.createFromResource(this, R.array.anthropology, android.R.layout.simple_list_item_1));
        getListView().setTextFilterEnabled(true);
        
        
        bar = getActionBar();
        
        HOME= getResources().getString(R.string.home);
        SUB= getResources().getString(R.string.anthropology);
        SUB_PREFIX= SUB.toLowerCase()+ "_";
        
        String[] categories= {HOME, SUB};
        createBar(bar, categories, mActionBarView);*/
        
        
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
	
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		//Log.d("OUP", "------------SubLanding onListItemClick--------------------");
		String m_selectedItemText = (String) l.getItemAtPosition(position);
		
		String[] parentCats = new String[bar.getTabCount()+1];
		bar = getActionBar();
		String arrayName= "";
		String hashName_m_selectedItemText= "";
		for(int i=0; i<bar.getTabCount(); i++){
			//Log.d("OUP", "Tab:"+ i + " "+ bar.getTabAt(i).getText());
			parentCats[i]= bar.getTabAt(i).getText().toString();
			//if(i>0){
			arrayName= arrayName + Tools.getXMLFileName(bar.getTabAt(i).getText().toString()) +"_";
			hashName_m_selectedItemText= hashName_m_selectedItemText+  Tools.getStringArrayName(Tools.getXMLFileName(bar.getTabAt(i).getText().toString())) + "_";
			//}
		}
		parentCats[parentCats.length-1]= m_selectedItemText;
		arrayName= arrayName + Tools.getXMLFileName(m_selectedItemText);
		hashName_m_selectedItemText= hashName_m_selectedItemText + Tools.getXMLFileName(m_selectedItemText);
		//Log.d("OUP", "ArrayName: "+ arrayName);
		
		
		
		//String hashName_m_selectedItemText= SUB_PREFIX + Tools.getStringArrayName(m_selectedItemText);
		//Log.d("OUP", "Selected ITEM name: "+ hashName_m_selectedItemText);
		//check oupCatHashmap to see if the selected item has the subcategory
		
		if(oupCatHashma!=null){
			 ArrayList<String> subList= oupCatHashma.get(hashName_m_selectedItemText.toLowerCase());
			 //Log.d("OUP", "elements: " +subList.size());
			 if(subList!=null){
				 //Log.d("OUP", "sublist is not null");
				 //sub landing page, update the list and tab
				//ArrayAdapter<String> arrAdapt= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subList); 
				CustomListAdapter arrAdapt = new CustomListAdapter(this, R.layout.custom_list_text,  subList);
     	        setListAdapter(arrAdapt);  	      
     	        
     	        getListView().setTextFilterEnabled(true);
     	       /*if(bar.getTabCount()==2)
					bar.addTab(bar.newTab().setText(m_selectedItemText).setTabListener(this), true);
				else if(bar.getTabCount()==3){
					bar.addTab(bar.newTab().setText(m_selectedItemText).setTabListener(this), 3);
					bar.setSelectedNavigationItem(3);
				}*/
     	        //Logic (not sure it is true or not): the listItemclick will add to the end of parent tab
     	       bar.addTab(bar.newTab().setText(m_selectedItemText).setTabListener(this), true);
				 
			 }else{
				 createCatLandingIntent(m_selectedItemText, parentCats, arrayName, this);
			 }
			 
			 
			 
			 
			 
			 /*else{
				// detemine if the selected cat still a sub landing page
				String selectedParentCat = bar.getSelectedTab().getText().toString();
				Log.d("OUP", "selectedParentCat: " + selectedParentCat);
				// Checking if the selected come from the third level navigation
				// or not
				// Logic: if there is anthing like history (parentlevel short
				// name)_selectedTextName, it has the third level nav
				// otherwise go to the cat landing page directly
				String arrayName = SUB_PREFIX
						+ Tools.getXMLFileName(selectedParentCat) + "_"
						+ Tools.getXMLFileName(m_selectedItemText);
				Log.d("OUP", "Array Name: "+ arrayName);
				subList= oupCatHashma.get(arrayName.toLowerCase());
				if(subList!=null){
					//Third level landing page
					String[] parentCats= {HOME, SUB,  selectedParentCat, m_selectedItemText};
					//reset Tab
					resetTab(bar, parentCats, mActionBarView);
					ArrayAdapter<String> arrAdapt= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subList);     
					setListAdapter(arrAdapt);
					getListView().setTextFilterEnabled(true);
				}else{
					// go to the category Landing directly
					//fourth level landing page
					//determine if it is the fourth level landing page
					
					String[] parentCats = new String[bar.getTabCount()+1];
					bar = getActionBar();
					arrayName= "";
					for(int i=0; i<bar.getTabCount(); i++){
						Log.d("OUP", "Tab:"+ i + " "+ bar.getTabAt(i).getText());
						parentCats[i]= bar.getTabAt(i).getText().toString();
						if(i>0)
							arrayName= arrayName + Tools.getXMLFileName(bar.getTabAt(i).getText().toString()) +"_";
					}
					parentCats[parentCats.length-1]= m_selectedItemText;
					arrayName= arrayName + Tools.getXMLFileName(m_selectedItemText);
					Log.d("OUP", "ArrayName: "+ arrayName);
					
					String selectedParentParentCat= bar.getTabAt(bar.getSelectedNavigationIndex()-1).getText().toString();
					//String[] parentCats= {HOME, SUB, selectedParentParentCat, selectedParentCat, m_selectedItemText};
					//get XML ID
					arrayName= SUB_PREFIX + Tools.getXMLPrefixName(selectedParentParentCat)+"_"+ Tools.getXMLFileName(selectedParentCat);
					String xmlFileName= Tools.getXMLFileName(m_selectedItemText);
					xmlFileName= arrayName+ "_"+ xmlFileName;
					Log.d("OUP", "file name: " + xmlFileName);
					//createCatLandingIntent(m_selectedItemText, parentCats, xmlFileName, this);
					createCatLandingIntent(m_selectedItemText, parentCats, arrayName, this);
					//Log.d("OUP", "it is null");
				}
				
				
				 
			 }*/
			 
			 
			 
		 }
		
		
		/*try{
			String subjectXMLFileName= Constants.OUP_XML_PATH + Constants.OUP_SUBJECT_FILE_NAME+ Constants.XML;
			subjectLandList= OUPXMLParser.parseValueFromFile(this,  subjectXMLFileName, m_selectedItemText);
			//if the selected item is the category landing page
			if(subjectLandList!=null){
				
			}else{
				String[] parentCats= {HOME, SUB, m_selectedItemText};
				//get XML ID
				String xmlFileName= Tools.getXMLFileName(m_selectedItemText);
				xmlFileName= Constants.OUP_XML_PATH +  SUB_PREFIX+ xmlFileName + Constants.XML;
				Log.d("OUP", xmlFileName);
				createCatLandingIntent(m_selectedItemText, parentCats, xmlFileName, this);
			}
			
		}catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		
		 
    }
	
	
	
	//Fixing for the Standby issue
	protected void onPause(){
		super.onPause();
		
		String[] selectedParentCats= new String[bar.getTabCount()];
		for(int i=0; i<bar.getTabCount(); i++){
			selectedParentCats[i]= (String) bar.getTabAt(i).getText();
			//Log.d("OUP", "Added Tab: "+ selectedParentCats[i]);
		}
		//intent.putExtra("parentCats", selectedParentCats);
		//intent.putExtra("FromTab", "FromTab");
		this.getIntent().putExtra("parentCats", selectedParentCats);
		String currentSubTitle= (String) bar.getTabAt(bar.getTabCount()-1).getText();
		this.getIntent().putExtra("FromTab", currentSubTitle);
		
	}
	
		
	protected void onResume(){
		super.onResume();
		//Log.d("OUP", "------------SubLanding onResume--------------------");
		Bundle extras= getIntent().getExtras();
		if(extras!=null){
			
		
			String FromTab= (String) extras.getString("FromTab");
			
			if(FromTab!=null && FromTab.equals("FromTab")){
				String[] parentCats= extras.getStringArray("parentCats");
				
				if(parentCats!=null){
					String arrayName= "";
					for(int i=0; i<parentCats.length; i++){
						//Log.d("OUP", "Tab: "+ parentCats[i]);
						arrayName= arrayName + Tools.getXMLFileName(parentCats[i])+ "_";
					}
					
					arrayName= arrayName.substring(0, arrayName.length()-1);
					//Log.d("OUP", "arrayName: "+ arrayName);
					
					//get the categoryList from HashMap
	                SalesManualApp theApp= ((SalesManualApp) getApplicationContext());
	                oupCatHashma= theApp.getCategoryHashmap();
					
					
					subjectLandList = oupCatHashma.get(arrayName.toLowerCase());
					if (subjectLandList != null) {
						//ArrayAdapter<String> arrAdapt = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, subjectLandList);
						CustomListAdapter arrAdapt = new CustomListAdapter(this, R.layout.custom_list_text,  subjectLandList);
						setListAdapter(arrAdapt);

						getListView().setTextFilterEnabled(true);
		            }
				
					bar = getActionBar();
					
					createBar(bar, parentCats, mActionBarView);
					
        		}
				
				
												
				
	    		
			}
		}else
			bar.setSelectedNavigationItem(1);
			


		
		/*Log.d("OUP", "-------------------On resume-------------");
		Bundle extras= getIntent().getExtras();
		if(extras!=null){
			
		
			String FromTab= (String) extras.getString("FromTab");
			
			if(FromTab!=null && FromTab.equals("FromTab")){
				String selectedTabString= (String) extras.getString("selectedTabString");
				Log.d("OUP", "selected Tab String: * "+ selectedTabString);
	    		if(selectedTabString.equalsIgnoreCase(getResources().getString(R.string.anthropology))){
	    			if(bar.getTabCount()>2){
	    				for(int i=2; i<bar.getTabCount()+1; i++){
	    					bar.removeTabAt(i);
	    				}
	    			}	  
	    			bar.setSelectedNavigationItem(1);
	    			setListAdapter(ArrayAdapter.createFromResource(this, R.array.anthropology, android.R.layout.simple_list_item_1));
	    		}else if(selectedTabString.equalsIgnoreCase("Home")){
	    			Intent intent = new Intent(CategoryLanding.this, SalesManualActivity.class);
	    	        startActivity(intent);
	    		}
	    		
			}
		}else
			bar.setSelectedNavigationItem(1);*/
    	
	}
	
	
	
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		
		bar = getActionBar();
		
		
		
		String selectedTabString= (String) bar.getSelectedTab().getText();
		//Log.d("OUP", "selected Tab String **: " + selectedTabString);
		
		HOME= getResources().getString(R.string.home);
		
		if(selectedTabString.equalsIgnoreCase(HOME)){
			Intent intent = new Intent(SubLanding.this, SalesManualActivity.class);
	        startActivity(intent);
		}else{
			
			String arrayName= "";
	
			for(int i=0; i<(bar.getSelectedNavigationIndex()+1); i++){
				//Log.d("OUP", "nav: "+ i+ ": "+ bar.getTabAt(i).getText());
				arrayName= arrayName + Tools.getXMLFileName((String)bar.getTabAt(i).getText())+ "_";
			}
			
			arrayName= arrayName.substring(0, arrayName.length()-1);
			//Log.d("OUP", "Array name: "+ arrayName);
			
			//get the categoryList from HashMap
            SalesManualApp theApp= ((SalesManualApp) getApplicationContext());
            oupCatHashma= theApp.getCategoryHashmap();
			
			
			subjectLandList = oupCatHashma.get(arrayName.toLowerCase());
			if (subjectLandList != null) {
				//ArrayAdapter<String> arrAdapt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subjectLandList);				
	        	CustomListAdapter arrAdapt = new CustomListAdapter(this, R.layout.custom_list_text,  subjectLandList);
				setListAdapter(arrAdapt);

				getListView().setTextFilterEnabled(true);
            }
			
			
			int test= bar.getSelectedTab().getPosition();
			
			//Log.d("OUP", "test position: "+ test);
			
			/*for(int i=0; i<bar.getTabCount(); i++){
				Log.d("OUP", "Tab "+ i + ": "+ bar.getTabAt(i).getText());
			}*/
			
			int total= bar.getTabCount();
			
			for(int i= total-1; i>test; i--){
				
				//Log.d("OUP", "remvoe: " + i + ": "+ bar.getTabAt(i).getText());
				bar.removeTabAt(i);
			}
			
			/*Log.d("OUP", "length of the real bar: "+ bar.getTabCount());
			
			Log.d("OUP", "length of the bar "+ bar.getSelectedNavigationIndex());
			
			String[] parentCats= new String[bar.getSelectedNavigationIndex()+1];
			
			
			
			for(int i=0; i<bar.getSelectedNavigationIndex()+1; i++){
				parentCats[i]= (String)bar.getTabAt(i).getText();
				Log.d("OUP", "parent cat: "+ parentCats[i]);
				
			}
			
			if(bar.getTabCount()>0)
				bar.removeAllTabs();
			
			createBar(bar, parentCats, mActionBarView);*/
		}
		
		
		/*String[] anthropologies= getResources().getStringArray(R.array.anthropology);
		
		int tabCount= bar.getTabCount();
		
		//Log.d("OUP", "onTabSelected tabCount: " + tabCount);
		//Log.d("OUP", "onTabSelected string: " + selectedTabString);
		
		if(selectedTabString.equalsIgnoreCase(SUB)){
			if(bar.getTabCount()>2){
				for(int i=2; i<(bar.getTabCount()); i++){
					bar.removeTabAt(i);
				}
			}
			
			if(subjectLandList!=null){
				ArrayAdapter<String> arrAdapt= 
    	                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, subjectLandList);
				setListAdapter(arrAdapt);
			}
			
		}else if(selectedTabString.equalsIgnoreCase(HOME)){
			Intent intent = new Intent(SubLanding.this, SalesManualActivity.class);
	        startActivity(intent);
		}*/
        
    }
	
	
	
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
