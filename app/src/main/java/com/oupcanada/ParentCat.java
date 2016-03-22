package com.oupcanada;

import java.io.File;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.oupcanada.R;
import com.oupcanada.SalesManualActivity;
import com.oupcanada.util.Tools;
import com.oupcanada.util.Constants;

public class ParentCat extends ListActivity implements ActionBar.TabListener {

	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       
    }
	
	public void createBar(ActionBar bar,  String[] categories, View mActionBarView){
		//bar = getActionBar();
		
		bar.removeAllTabs();
		
        for (int i = 0; i < categories.length; i++) {
        	//need to do addTab(, setSelected)  setSelected as false
            bar.addTab(bar.newTab().setText(categories[i]).setTabListener(this), false);
            
        }
        
        
        mActionBarView = getLayoutInflater().inflate(
                R.layout.action_bar_custom, null);

        bar.setCustomView(mActionBarView);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayShowHomeEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        int selectedTag= categories.length-1;
        Log.d("OUP", "selected Tag: "+ selectedTag);
        bar.setSelectedNavigationItem(selectedTag);
	}
	
		
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
	
	

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

	public void createCatLandingIntent(String m_selectedItemText, String[] parentCats, String xmlFileName, Context sourceIntent){
		//Log.d("OUP", "ParentCat: xmlFile name is "+ xmlFileName);
		Intent intent = new Intent(sourceIntent, CategoryLanding.class);
		
		intent.putExtra("parentCats", parentCats);
		
		intent.putExtra("xml_file", xmlFileName);
	
		startActivity(intent);
		
		
		
		//if the XML file exist
		/*if(xmlFileNameID>0){
			Log.d("OUP", "ID: " +xmlFileNameID);
			intent.putExtra("xml_file", xmlFileNameID);
	        startActivity(intent);
		}*/
		
		
	}
	
	
	
	
	public void resetTab(ActionBar bar, String[] tabNameArray, View mActionBarView){
		bar.removeAllTabs();
		for(int i=0; i<tabNameArray.length; i++){
			//Log.d("OUP", "parent cata: "+ tabNameArray[i]);
			bar.addTab(bar.newTab().setText(tabNameArray[i]).setTabListener(this), false);
		}
		
		bar.setCustomView(mActionBarView);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_USE_LOGO);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayShowHomeEnabled(true);
        int selectedTag= tabNameArray.length-1;
        bar.setSelectedNavigationItem(selectedTag);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}
	
	
	public void createBrowseByIntent(String browseByLabel, String browseBy) {
		// switch to a progress animation
		Intent intent = new Intent(this, CategoryLanding.class);
		
		String[] parentCats_author= {Constants.LABEL_HOME, browseByLabel};
		intent.putExtra("parentCats", parentCats_author);
		
		int xmlFileNameID_author=  getResources().getIdentifier(Constants.BOOK_FULL_LIST, null, null);
		
		
		//if the XML file exist
		if(xmlFileNameID_author>0){
			//Log.d("OUP", "ID: " +xmlFileNameID_author);
			intent.putExtra("xml_file", xmlFileNameID_author);
			intent.putExtra("browse_by", browseBy);
		    startActivity(intent);
		}
	}
}
