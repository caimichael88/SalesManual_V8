package com.oupcanada.util;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.text.util.Linkify.TransformFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import com.oupcanada.CategoryLanding;
import com.oupcanada.util.Book;
import com.oupcanada.R;

public class BookAdapter extends ArrayAdapter<Book>{

    private ArrayList<Book> books;
    private Context context;
    

    public BookAdapter(Context context, int textViewResourceId, ArrayList<Book> books) {
            super(context, textViewResourceId, books);
            this.books = books;
            this.context= context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
            	
                LayoutInflater vi = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.book, null);
            }
            
            
            //need to set background color first in order to customize bckground color
            v.setBackgroundColor(Color.WHITE);
            
            Typeface tf=Typeface.createFromAsset(context.getAssets(), Constants.CUSTOM_FONT_FACE);
        	
            
            /*if((position % 2)==0){
            	v.setBackgroundColor(Color.rgb(0, 40, 93));
            }*/
            
            
            
            Book book = books.get(position);
            final String isbnname= book.getIsbn();
            
            if (book != null) {
            	
            	//if the book is special book, change background into special color
            	/*if(!book.getFlag().equalsIgnoreCase("")){
            		v.setBackgroundColor(Color.rgb(255, 153, 0));
            	}
            	*/
            	//Log.d("OUP", "color " + book.getColor());
            	if(book.getColor().equalsIgnoreCase("y")){
            		v.setBackgroundColor(Color.YELLOW);
            		
            	}else if(book.getColor().equalsIgnoreCase("g")){
            		v.setBackgroundColor(Color.rgb(217, 217, 217));
            		
            	}
            	
            	
                                 	                	
            	TextView bookInfo = (TextView) v.findViewById(R.id.bookInfo);   
            	bookInfo.setTypeface(tf);
                TextView bookType = (TextView) v.findViewById(R.id.bookType);
                bookType.setTypeface(tf);
                TextView pri = (TextView) v.findViewById(R.id.PRI);    
                pri.setTypeface(tf);
                TextView isbn= (TextView) v.findViewById(R.id.isbn); 
                isbn.setTypeface(tf);
                TextView off= (TextView) v.findViewById(R.id.OFF);
                off.setTypeface(tf);
                TextView  pg= (TextView) v.findViewById(R.id.PG);
                pg.setTypeface(tf);
                
                if(bookInfo!= null){  
                	
                	//will style the book underline if the file exists
                	String bookInfoFileName= "";
                	if(isbnname!=null && !isbnname.equals(""))
                		bookInfoFileName= Environment.getExternalStorageDirectory() + Constants.OUP_DATA_PATH + isbnname + ".pdf";
                	else
                		bookInfoFileName = Environment.getExternalStorageDirectory() + Constants.OUP_DATA_PATH + book.getBookInfo() + ".pdf";
                	
                    //File file = new File("/sdcard/Android/data/Glossary.pdf");
                	File bookInfoFile = new File(bookInfoFileName);                	
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
                        	String fileName= Environment.getExternalStorageDirectory() + Constants.OUP_DATA_PATH + isbnname + ".pdf";
                            //File file = new File("/sdcard/Android/data/Glossary.pdf");
                        	File file = new File(fileName);
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
                
                if(bookType!=null){
                	//bookType.setTextColor(Color.rgb(0, 40, 93));
                	bookType.setTextColor(Color.rgb(2, 39, 91));
                	//bookType.setTextColor(Color.BLACK);
                	bookType.setText(book.getBookType());
                }
                
                if(pri!=null){
                	pri.setTextColor(Color.rgb(2, 39, 91));
                	//pri.setTextColor(Color.BLACK);
                	pri.setText(book.getPri());
                }
                
                if(isbn!=null){
                	final String bookISBN= book.getIsbn();
                	//set underline
                	SpannableString bookISBNText = new SpannableString(bookISBN);
                	bookISBNText.setSpan(new UnderlineSpan(), 0, bookISBNText.length(), 0);               	
                	    
                	isbn.setText(bookISBNText);
                	isbn.setTextColor(Color.rgb(2, 39, 91));
                	//isbn.setTextColor(Color.BLACK);
                	isbn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        	
                        	Uri uri = Uri.parse("http://www.oupcanada.com/catalog/"+ bookISBN+ ".html");
                        	Intent intent = new Intent(Intent.ACTION_VIEW, uri);
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
                    });                        
                	
                	/*TransformFilter mentionFilter = new TransformFilter() {
                        public final String transformUrl(final Matcher match, String url) {
                            return url + ".html";
                        }
                    };
                    //add as hyper link to the catalogue page
                    Pattern pattern = Pattern.compile("[A-Za-z0-9]+");
                    Linkify.addLinks(isbn, pattern, "http://www.oupcanada.com/catalog/", null, mentionFilter);*/
                	//Linkify.addLinks(isbn, Linkify.ALL);
                	
                }
                
                if(off!=null){
                	off.setTextColor(Color.rgb(2, 39, 91));
                	//off.setTextColor(Color.BLACK);
                	off.setText(book.getOff());
                }
                
                if(pg!=null){
                	pg.setTextColor(Color.rgb(2, 39, 91));
                	//pg.setTextColor(Color.BLACK);
                	pg.setText(book.getPg());
                }
              
                
                /*if((position % 2)==0){
                	bookInfo.setTextColor(Color.rgb(245, 248, 243));
                	bookType.setTextColor(Color.WHITE);
                	pri.setTextColor(Color.WHITE);
                	isbn.setTextColor(Color.rgb(245, 248, 243));
                	off.setTextColor(Color.WHITE);
                	pg.setTextColor(Color.WHITE);
                }*/
            	
            }

           
            return v;
    }


}
