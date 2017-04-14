package com.vengalrao.android.reader.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by vengalrao on 12-04-2017.
 */

public class BookDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="book.db";
    private static final int VERSION=1;

    public BookDBHelper(Context context){
        super(context,DB_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_GENERAL_TABLE="CREATE TABLE "+BookContrack.BookGeneral.TABLE_NAME+" ("+
                BookContrack.BookGeneral._ID+" INTEGER PRIMARY KEY, "+
                BookContrack.BookGeneral.BOOK_ID+" TEXT NOT NULL, "+
                BookContrack.BookGeneral.BOOK_TITLE+" TEXT NOT NULL, "+
                BookContrack.BookGeneral.BOOK_AUTHOR+" TEXT NOT NULL, "+
                BookContrack.BookGeneral.BOOK_PUBLISHED_DATE+" TEXT , "+
                BookContrack.BookGeneral.BOOK_DESCRIPTION+" TEXT , "+
                BookContrack.BookGeneral.BOOK_CATEGORY+" TEXT , "+
                BookContrack.BookGeneral.BOOK_AVG_RATING+" TEXT , "+
                BookContrack.BookGeneral.BOOK_WEB_READER_LINK+" TEXT , "+
                BookContrack.BookGeneral.BOOK_LANG+" TEXT , "+
                BookContrack.BookGeneral.BOOK_IMAGE+" TEXT , "+
                BookContrack.BookGeneral.BOOK_PAGE_COUNT+" TEXT , "+
                BookContrack.BookGeneral.BOOk_HIGH_QUALITY_IMG+" TEXT  );";
        db.execSQL(CREATE_GENERAL_TABLE);
        final String CREATE_FAV_TABLE="CREATE TABLE "+BookContrack.BookFavorite.TABLE_NAME+" ("+
                BookContrack.BookFavorite._ID+" INTEGER PRIMARY KEY, "+
                BookContrack.BookFavorite.BOOK_ID+" TEXT NOT NULL, "+
                BookContrack.BookFavorite.BOOK_TITLE+" TEXT NOT NULL, "+
                BookContrack.BookFavorite.BOOK_AUTHOR+" TEXT NOT NULL, "+
                BookContrack.BookFavorite.BOOK_PUBLISHED_DATE+" TEXT , "+
                BookContrack.BookFavorite.BOOK_DESCRIPTION+" TEXT , "+
                BookContrack.BookFavorite.BOOK_CATEGORY+" TEXT , "+
                BookContrack.BookFavorite.BOOK_AVG_RATING+" TEXT , "+
                BookContrack.BookFavorite.BOOK_WEB_READER_LINK+" TEXT , "+
                BookContrack.BookFavorite.BOOK_LANG+" TEXT , "+
                BookContrack.BookFavorite.BOOK_IMAGE+" TEXT , "+
                BookContrack.BookFavorite.BOOK_PAGE_COUNT+" TEXT , "+
                BookContrack.BookFavorite.BOOk_HIGH_QUALITY_IMG+" TEXT  );";
        db.execSQL(CREATE_FAV_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DEL_GENERAL_TABLE="DROP TABLE IF EXITS "+BookContrack.BookGeneral.TABLE_NAME;
        final String DEL_FAV_TABLE="DROP TABLE IF EXITS "+BookContrack.BookFavorite.TABLE_NAME;
        db.execSQL(DEL_GENERAL_TABLE);
        db.execSQL(DEL_FAV_TABLE);
        onCreate(db);
    }
}
