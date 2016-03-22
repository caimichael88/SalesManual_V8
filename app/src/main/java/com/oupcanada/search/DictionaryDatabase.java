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

import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.xmlpull.v1.XmlPullParserException;

import com.oupcanada.SalesManualApp;
import com.oupcanada.util.Book;
import com.oupcanada.util.Constants;
import com.oupcanada.util.OUPXMLParser;
import com.oupcanada.util.Status;

/**
 * Contains logic to return specific words from the dictionary, and
 * load the dictionary table when it needs to be created.
 */
public class DictionaryDatabase {
    private static final String TAG = "OUP";

    //The columns we'll include in the dictionary table
    //public static final String KEY_WORD = SearchManager.SUGGEST_COLUMN_TEXT_1;
    public static final String KEY_WORD = "word";
    public static final String KEY_DEFINITION = SearchManager.SUGGEST_COLUMN_TEXT_2;
    
    //added for the book column in the db
    //public static final String AUTHOR = "author";
    public static final String AUTHOR = SearchManager.SUGGEST_COLUMN_TEXT_1;
    public static final String TYPE=  "booktype";
    public static final String PRI = "pri";
    public static final String ISBN = "isbn";
    public static final String OFF= "off";

    //private static final String DATABASE_NAME = "dictionary";
    private static final String DATABASE_NAME = "oupcategory";
    
    private static final String FTS_VIRTUAL_TABLE = "FTSdictionary_oup";
    private static final int DATABASE_VERSION = 15;

    private final DictionaryOpenHelper mDatabaseOpenHelper;
    private static final HashMap<String,String> mColumnMap = buildColumnMap();

    /**
     * Constructor
     * @param context The Context within which to work, used to create the DB
     */
    public DictionaryDatabase(Context context) {
        mDatabaseOpenHelper = new DictionaryOpenHelper(context);
    }

    /**
     * Builds a map for all columns that may be requested, which will be given to the 
     * SQLiteQueryBuilder. This is a good way to define aliases for column names, but must include 
     * all columns, even if the value is the key. This allows the ContentProvider to request
     * columns w/o the need to know real column names and create the alias itself.
     */
    private static HashMap<String,String> buildColumnMap() {
        HashMap<String,String> map = new HashMap<String,String>();
        map.put(KEY_WORD, KEY_WORD);
        map.put(KEY_DEFINITION, KEY_DEFINITION);
        
        map.put(AUTHOR, AUTHOR);
        map.put(TYPE, TYPE);
        map.put(PRI, PRI);
        map.put(ISBN, ISBN);
        map.put(OFF, OFF);
        map.put(BaseColumns._ID, "rowid AS " +
                BaseColumns._ID);
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, "rowid AS " +
                SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
        return map;
    }

    /**
     * Returns a Cursor positioned at the word specified by rowId
     *
     * @param rowId id of word to retrieve
     * @param columns The columns to include, if null then all are included
     * @return Cursor positioned to matching word, or null if not found.
     */
    public Cursor getWord(String rowId, String[] columns) {
        String selection = "rowid = ?";
        String[] selectionArgs = new String[] {rowId};

        return query(selection, selectionArgs, columns);

        /* This builds a query that looks like:
         *     SELECT <columns> FROM <table> WHERE rowid = <rowId>
         */
    }

    /**
     * Returns a Cursor over all words that match the given query
     *
     * @param query The string to search for
     * @param columns The columns to include, if null then all are included
     * @return Cursor over all words that match, or null if none found.
     */
    public Cursor getWordMatches(String query, String[] columns) {
        String selection = KEY_WORD + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);

        /* This builds a query that looks like:
         *     SELECT <columns> FROM <table> WHERE <KEY_WORD> MATCH 'query*'
         * which is an FTS3 search for the query text (plus a wildcard) inside the word column.
         *
         * - "rowid" is the unique id for all rows but we need this value for the "_id" column in
         *    order for the Adapters to work, so the columns need to make "_id" an alias for "rowid"
         * - "rowid" also needs to be used by the SUGGEST_COLUMN_INTENT_DATA alias in order
         *   for suggestions to carry the proper intent data.
         *   These aliases are defined in the DictionaryProvider when queries are made.
         * - This can be revised to also search the definition text with FTS3 by changing
         *   the selection clause to use FTS_VIRTUAL_TABLE instead of KEY_WORD (to search across
         *   the entire table, but sorting the relevance could be difficult.
         */
    }

    /**
     * Performs a database query.
     * @param selection The selection clause
     * @param selectionArgs Selection arguments for "?" components in the selection
     * @param columns The columns to return
     * @return A Cursor over all rows matching the query
     */
    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        /* The SQLiteBuilder provides a map for all possible columns requested to
         * actual columns in the database, creating a simple column alias mechanism
         * by which the ContentProvider does not need to know the real column names
         */
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);
        builder.setProjectionMap(mColumnMap);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        
        return cursor;
        //return null;
    }


    /**
     * This creates/opens the database.
     */
    private static class DictionaryOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;
        private SQLiteDatabase mDatabase;

        /* Note that FTS3 does not support column constraints and thus, you cannot
         * declare a primary key. However, "rowid" is automatically used as a unique
         * identifier, so when making requests, we will use "_id" as an alias for "rowid"
         */
        /*private static final String FTS_TABLE_CREATE =
                    "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                    " USING fts3 (" +
                    KEY_WORD + ", " +
                    KEY_DEFINITION + ");";*/
        
        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                " USING fts3 (" +
                KEY_WORD + ", " +
                KEY_DEFINITION + ", " + AUTHOR + ", " + TYPE + ", " + PRI + ", "+ ISBN + ", "+ OFF +  ");";

        DictionaryOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	//Log.d("OUP", "DictionaryOpenHelper: onCreate");
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_CREATE);
            loadDictionary();
        }
        
        public void onOpen(SQLiteDatabase db){
        	//test whenever open the DB, recreate again
        	//it will change while it see the flag in the place
        	
        	//stop change the search index.
        	/*SalesManualApp theApp= ((SalesManualApp) mHelperContext.getApplicationContext());
        	Status status= theApp.getStatus();
        	if(status.getSearch_xml().equalsIgnoreCase(Constants.SEARCH_SYNC_FLAG)){
        		//Log.d("OUP", "DictionaryOpenHelper:onOpen "+"try to create DB again while open the DB" );
                db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
                onCreate(db);
        	}*/
        	
        }

        /**
         * Starts a thread to load the database table with words
         */
        private void loadDictionary() {
            new Thread(new Runnable() {
                public void run() {
                	
                    try {
                    	loadBooks();
                    } catch (XmlPullParserException e) {
            			// TODO Auto-generated catch block
                    	throw new RuntimeException(e);
            		} catch (IOException e) {
            			// TODO Auto-generated catch block
            			throw new RuntimeException(e);
            		}
                }
            }).start();
        }

        /*private void loadWords() throws IOException {
            Log.d("oup", "Loading words...");
            File file = new File("/sdcard/oup/definitions.txt");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            //BufferedReader reader = new BufferedReader(new FileReader("/sdcard/oup/definitions.txt"));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));


            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] strings = TextUtils.split(line, "-");
                    if (strings.length < 2) continue;
                    long id = addWord(strings[0].trim(), strings[1].trim());
                    if (id < 0) {
                        Log.e(TAG, "unable to add word: " + strings[0].trim());
                    }
                }
            } finally {
                reader.close();
            }
            Log.d(TAG, "DONE loading words.");
        }*/
        
        private void loadBooks() throws IOException, XmlPullParserException {
         	//Log.d("oup", "Loading Books...");
         	//String path= "/sdcard/My SugarSync Folders/.oupfiles/full_list.xml";
         	String path= Constants.SEARCH_XML;
         	ArrayList<Book> books= OUPXMLParser.parseBooks(path);
         	
         	for(int i=0; i<books.size(); i++){
         		Book book= books.get(i);
         		 long id = addBook(book.getAuthor() + " " + book.getBookInfo(), book.getBookInfo(), book.getAuthor(), book.getBookType(), book.getPri(), book.getIsbn(), book.getOff());
                 if (id < 0) {
                     //Log.e(TAG, "unable to add word: " + book.getAuthor());
                 }
         	}
             
             //Log.d(TAG, "DONE loading words.");
         }

        /**
         * Add a word to the dictionary.
         * @return rowId or -1 if failed
         */
 /*      public long addWord(String word, String definition) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_WORD, word);
            initialValues.put(KEY_DEFINITION, definition);

            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
        }*/
        
        public long addBook(String word, String definition, String author, String bookType, String pri, String isbn, String off) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_WORD, word);
            initialValues.put(KEY_DEFINITION, definition);
            initialValues.put(AUTHOR, author);
            initialValues.put(TYPE, bookType);
            initialValues.put(PRI, pri);
            initialValues.put(ISBN, isbn);
            initialValues.put(OFF, off);

            return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }
    }

}
