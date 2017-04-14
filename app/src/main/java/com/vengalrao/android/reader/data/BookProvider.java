package com.vengalrao.android.reader.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;
import android.util.Log;

/**
 * Created by vengalrao on 12-04-2017.
 */

public class BookProvider extends ContentProvider{
    public static final int GEN=100;
    public static final int GEN_ID=101;
    public static final int FAV=200;
    public static final int FAV_ID=201;

    private BookDBHelper mBookDBHelper;
    private static final UriMatcher sUriMacher=buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(BookContrack.AUTHORITY,BookContrack.PATH_GENERAL,GEN);
        uriMatcher.addURI(BookContrack.AUTHORITY,BookContrack.PATH_GENERAL+"/*",GEN_ID);
        uriMatcher.addURI(BookContrack.AUTHORITY,BookContrack.PATH_FAV,FAV);
        uriMatcher.addURI(BookContrack.AUTHORITY,BookContrack.PATH_FAV+"/*",FAV_ID);

        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        Context context=getContext();
        mBookDBHelper=new BookDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db=mBookDBHelper.getReadableDatabase();
        int match=sUriMacher.match(uri);
        Cursor retCursor;
        switch (match){
            case GEN:
                retCursor=db.query(
                        BookContrack.BookGeneral.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FAV:
                retCursor=db.query(
                        BookContrack.BookFavorite.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Operation not supported.");
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db=mBookDBHelper.getWritableDatabase();
        int match=sUriMacher.match(uri);
        Uri returnUri;
        switch (match){
            case GEN:
                long id=db.insert(BookContrack.BookGeneral.TABLE_NAME,null,values);
                if(id>0){
                    returnUri= ContentUris.withAppendedId(BookContrack.BookGeneral.CONTENT_GENERAL_URI,id);
                }else{
                    throw new android.database.SQLException("Failed to insert into General table "+uri);
                }
                break;
            case FAV:
                long id2=db.insert(BookContrack.BookFavorite.TABLE_NAME,null,values);
                if(id2>0){
                    returnUri=ContentUris.withAppendedId(BookContrack.BookFavorite.CONTENT_FAV_URI,id2);
                }else{
                    throw new android.database.SQLException("Failed to insert into Favorite table "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Operation Not Supported");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db=mBookDBHelper.getWritableDatabase();
        int match=sUriMacher.match(uri);
        int retDelRows;
        switch (match){
            case GEN:
                retDelRows=db.delete(BookContrack.BookFavorite.TABLE_NAME,null,null);
                break;
            case FAV_ID:
                String id2=uri.getPathSegments().get(1);
                String mSelection2="BookId=?";
                String[] mSelectionArgs2=new String[]{id2};
                retDelRows=db.delete(
                        BookContrack.BookFavorite.TABLE_NAME,
                        mSelection2,
                        mSelectionArgs2
                );
                break;
            default:
                throw new UnsupportedOperationException("Operation not supported");
        }
        if(retDelRows!=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return retDelRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
