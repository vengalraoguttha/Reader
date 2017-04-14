package com.vengalrao.android.reader.Utilities;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by vengalrao on 11-04-2017.
 */

public class NetworkUtilities {
    private static String BASE_URL="https://www.googleapis.com/books/v1/volumes";
    private static String HIGH_QUALITY="https://www.googleapis.com/books/v1/volumes";
    private static String QUERY="q";

    public static URL buildUrl(String query){
        Uri uri=Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(QUERY,query)
                .build();
        URL queryUrl=null;
        try {
            queryUrl=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return queryUrl;
    }

    public static URL buildSpecificUrl(String s1,String s2,String query){
        Uri uri=Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter(QUERY,query)
                .build();
        URL queryUrl=null;
        try {
            queryUrl=new URL(uri.toString().concat("+").concat(s1).concat(":").concat(s2));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return queryUrl;
    }

    public static URL buildHighQualityImageURL(String id){
        Uri uri=Uri.parse(HIGH_QUALITY)
                .buildUpon()
                .appendPath(id)
                .build();
        URL queryUrl=null;
        try {
            queryUrl=new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return queryUrl;
    }

    public static String getResponseFromHttpUrl(URL url){
        HttpURLConnection urlConnection=null;
        try {
            urlConnection=(HttpURLConnection)url.openConnection();
            InputStream in=urlConnection.getInputStream();
            Scanner sc=new Scanner(in);
            sc.useDelimiter("\\A");
            if(sc.hasNext()){
                return sc.next();
            }else{
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            urlConnection.disconnect();
        }
    }

    public static Book[] getParsedData(String responseMain){
        Log.v("response",responseMain);
        Book[] books=null;
        try {
            JSONObject object1=new JSONObject(responseMain);
            if(object1.has("items")){
                JSONArray array=object1.getJSONArray("items");
                books=new Book[array.length()];
                for(int i=0;i<array.length();i++){
                    books[i]=new Book();
                    JSONObject object=array.getJSONObject(i);
                    if(object.has("id"))
                    books[i].setId(object.getString("id"));
                    if(object.has("volumeInfo")){
                        JSONObject jsonObject=object.getJSONObject("volumeInfo");
                        if(jsonObject.has("title"))
                        books[i].setTitle(jsonObject.getString("title"));
                        if(jsonObject.has("authors")){
                            JSONArray jsonArray=jsonObject.getJSONArray("authors");
                            books[i].setAuthors(jsonArray.getString(0));
                        }
                        if(jsonObject.has("publishedDate"))
                        books[i].setPublishedDate(jsonObject.getString("publishedDate"));
                        if(jsonObject.has("description"))
                        books[i].setDescription(jsonObject.getString("description"));
                        if(jsonObject.has("pageCount"))
                        books[i].setPageCount(jsonObject.getString("pageCount"));
                        if(jsonObject.has("categories")){
                            JSONArray categories=jsonObject.getJSONArray("categories");
                            books[i].setCategory(categories.getString(0));
                        }
                        if(jsonObject.has("averageRating"))
                            books[i].setAvgRating(jsonObject.getString("averageRating"));
                        if(jsonObject.has("imageLinks")){
                            JSONObject imageLinks=jsonObject.getJSONObject("imageLinks");
                            books[i].setImage(imageLinks.getString("thumbnail"));
                        }
                        if(jsonObject.has("language"))
                        books[i].setLanguage(jsonObject.getString("language"));
                        if(jsonObject.has("previewLink"))
                        books[i].setWebReaderLink(jsonObject.getString("previewLink"));
                    }
                }
                return books;
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getHighQualityImage(String id){
        String response=getResponseFromHttpUrl(buildHighQualityImageURL(id));
        try {
            if(response==null)
                return null;
            JSONObject object=new JSONObject(response);
            if(object.has("volumeInfo")){
                JSONObject innerObj=object.getJSONObject("volumeInfo");
                if(innerObj.has("imageLinks")){
                    JSONObject imageLinks=innerObj.getJSONObject("imageLinks");
                    if(imageLinks.has("medium")){
                        return imageLinks.getString("medium");
                    }else if(imageLinks.has("large")){
                        return imageLinks.getString("large");
                    }else if(imageLinks.has("small")){
                        return imageLinks.getString("small");
                    }else{
                        return null;
                    }
                }
            }
            return null;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
