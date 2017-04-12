package com.vengalrao.android.reader.Utilities;

import android.content.ContentValues;

import com.vengalrao.android.reader.data.BookContrack;

/**
 * Created by vengalrao on 12-04-2017.
 */

public class ServiceUtilities {
    public static void updateDataBase(Book[] books){
        for(int i=0;i<books.length;i++){
            ContentValues contentValues=new ContentValues();
            contentValues.put(BookContrack.BookGeneral.BOOK_ID,books[i].getId());
            contentValues.put(BookContrack.BookGeneral.BOOK_TITLE,books[i].getTitle());
            contentValues.put(BookContrack.BookGeneral.BOOK_AUTHOR,books[i].getAuthors());
            contentValues.put(BookContrack.BookGeneral.BOOK_PUBLISHED_DATE,books[i].getPublishedDate());
            contentValues.put(BookContrack.BookGeneral.BOOK_DESCRIPTION,books[i].getDescription());
            contentValues.put(BookContrack.BookGeneral.BOOK_CATEGORY,books[i].getCategory());
            contentValues.put(BookContrack.BookGeneral.BOOK_AVG_RATING,books[i].getAvgRating());
            contentValues.put(BookContrack.BookGeneral.BOOK_WEB_READER_LINK,books[i].getWebReaderLink());
            contentValues.put(BookContrack.BookGeneral.BOOK_LANG,books[i].getLanguage());
            contentValues.put(BookContrack.BookGeneral.BOOK_IMAGE,books[i].getImage());
            contentValues.put(BookContrack.BookGeneral.BOOK_PAGE_COUNT,books[i].getPageCount());
        }
    }
}
