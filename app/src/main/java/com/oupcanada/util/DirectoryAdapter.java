package com.oupcanada.util;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.util.Linkify;
import android.text.util.Linkify.TransformFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.oupcanada.R;

public class DirectoryAdapter extends ArrayAdapter<String>{

    private String[] directories;
    private Context context;
    

    public DirectoryAdapter(Context context, int textViewResourceId, String[] directories) {
            super(context, textViewResourceId, directories);
            this.directories = directories;
            this.context= context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
            	
                LayoutInflater vi = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.directory, null);
            }
            
            if((position % 2)==0){
            	v.setBackgroundColor(Color.rgb(0, 40, 93));
            }
            
            TextView directory = (TextView) v.findViewById(R.id.directory);   
            
            if(directory!=null){
            	directory.setText(directories[position]);
            	//directory.setTextColor(Color.BLACK);
            }
            
            return v;
    }


}
