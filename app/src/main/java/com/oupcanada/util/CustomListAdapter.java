package com.oupcanada.util;

import java.util.ArrayList;

import android.content.Context;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oupcanada.R;

public class CustomListAdapter extends ArrayAdapter<String> {

    private ArrayList<String> cats;
    private Context context;
    

    public CustomListAdapter(Context context, int textViewResourceId,  ArrayList<String> cats) {
    	 	super(context, textViewResourceId, cats);
            this.cats = cats;
            this.context= context;
    }
    
   
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	View v = convertView;
        if (v == null) {
        	
            LayoutInflater vi = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.custom_list_text, null);
        }
        
        TextView listText = (TextView) v.findViewById(R.id.custom_text);   
    	
    	Typeface tf=Typeface.createFromAsset(context.getAssets(), Constants.CUSTOM_FONT_FACE);
    	listText.setTypeface(tf);

        String cat = cats.get(position);

        
        
        listText.setTextColor(Color.rgb(2, 39, 91));
        listText.setText(cat.toString()); // use whatever method you want for the label
        
        ImageView icon=(ImageView)v.findViewById(R.id.list_arrow);  
        icon.setImageResource(R.drawable.new_arrow);
        //icon.setImageResource(R.drawable.arrow);
        
        
        
        return v;
    }



    

}
