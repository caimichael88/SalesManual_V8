package com.oupcanada.util;

import java.io.File;
import java.util.ArrayList;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.oupcanada.util.Book;
import com.oupcanada.R;

public class SalesToolAdapter extends ArrayAdapter<Book>{

    private ArrayList<Book> books;
    private Context context;
    

    public SalesToolAdapter(Context context, int textViewResourceId, ArrayList<Book> books) {
            super(context, textViewResourceId, books);
            this.books = books;
            this.context= context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
            	
                LayoutInflater vi = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.sales_tool, null);
            }
            
            
            //need to set background color first in order to customize bckground color
            v.setBackgroundColor(Color.WHITE);
            
            if((position % 2)==0){
            	//v.setBackgroundColor(Color.rgb(0, 40, 93));
            }
            
            
           
            
            Book book = books.get(position);
            final String bookInfoVal= book.getBookInfo().trim();
            
            if (book != null) {
            	
                                         	                	
            	TextView bookInfo = (TextView) v.findViewById(R.id.bookInfo);    
            	
            	Typeface tf=Typeface.createFromAsset(context.getAssets(), Constants.CUSTOM_FONT_FACE);
            	bookInfo.setTypeface(tf);
                
                
                if(bookInfo!= null){  
                	
                	//will style the book underline if the file exists
                	//String bookInfoFileName= Constants.OUP_DATA_PATH + bookInfoVal + ".pdf";
                	//Log.d("OUP", "file name"+ bookInfoFileName);
                    //File file = new File("/sdcard/Android/data/Glossary.pdf");
                	//File bookInfoFile = new File(bookInfoFileName); 
                	
                	File pathDir= new File(Environment.getExternalStorageDirectory() + Constants.OUP_DATA_PATH);
            	    File bookInfoFile = new File(pathDir, bookInfoVal + ".pdf");
            	    
            	    
                	               	
                	if(bookInfoFile.exists()){
                		//set underline                	
                    	SpannableString bookInfoText = new SpannableString(book.getBookInfo());
                    	bookInfoText.setSpan(new UnderlineSpan(), 0, bookInfoText.length(), 0);               	
                    	bookInfo.setText(bookInfoText);
                	}else{
                		bookInfo.setText(book.getBookInfo());
                	}             		

                	bookInfo.setTextColor(Color.rgb(2, 39, 91));
                	//bookInfo.setTextColor(Color.BLACK);
                	bookInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        	//String fileName= "/sdcard/Android/data/" + isbnname + ".pdf";
                        	//String fileName= Constants.OUP_DATA_PATH + bookInfoVal + ".pdf";
                            //File file = new File("/sdcard/Android/data/Glossary.pdf");
                        	//File file = new File(fileName);
                        	
                        	File pathDir= new File(Environment.getExternalStorageDirectory() + Constants.OUP_DATA_PATH);
                    	    File file = new File(pathDir, bookInfoVal + ".pdf");
                    	    
                            if (file.exists()) {
                                Uri path = Uri.fromFile(file);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(path, "application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                try {
                                    context.startActivity(intent);
                                } 
                                catch (ActivityNotFoundException e) {
                                	//start to an error page
                                    Toast.makeText(context, 
                                        "No Application Available to View PDF", 
                                        Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });                        
                	
                	
                }
                
                
                if((position % 2)==0){
                	//bookInfo.setTextColor(Color.rgb(245, 248, 243));                	
         
                }
                
                
            	
            }

           
            return v;
    }


}
