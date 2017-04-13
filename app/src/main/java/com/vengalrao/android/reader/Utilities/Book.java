package com.vengalrao.android.reader.Utilities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vengalrao on 12-04-2017.
 */

public class Book implements Parcelable {

    private String Id;
    private String title;
    private String authors;
    private String publishedDate;
    private String description;
    private String category;
    private String avgRating;
    private String webReaderLink;
    private String language;
    private String image;
    private String pageCount;
    private String highQualityImage;

    public Book(){
        super();
    }

    public Book(Parcel in){
        readFromParcel(in);
    }

    public void setId(String Id){
        this.Id=Id;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public void setAuthors(String authors){
        this.authors=authors;
    }

    public void setPublishedDate(String publishedDate){
        this.publishedDate=publishedDate;
    }

    public void setDescription(String description){
        this.description=description;
    }

    public void setCategory(String category){
        this.category=category;
    }

    public void setAvgRating(String avgRating){
        this.avgRating=avgRating;
    }

    public void setWebReaderLink(String webReaderLink){
        this.webReaderLink=webReaderLink;
    }

    public void setLanguage(String language){
        this.language=language;
    }

    public void setImage(String image){
        this.image=image;
    }

    public void setPageCount(String pageCount){
        this.pageCount=pageCount;
    }

    public void setHighQualityImage(String highQualityImage){
        this.highQualityImage=highQualityImage;
    }

    public String getId(){
        return Id;
    }

    public String getTitle(){
        return title;
    }

    public  String getPublishedDate(){
        return publishedDate;
    }

    public String getDescription(){
        return description;
    }

    public String getCategory(){
        return category;
    }

    public String getAvgRating(){
        return avgRating;
    }

    public String getWebReaderLink(){
        return webReaderLink;
    }

    public String getLanguage(){
        return language;
    }

    public String getImage(){
        return image;
    }

    public String getPageCount(){
        return pageCount;
    }

    public String getAuthors(){
        return authors;
    }

    public String getHighQualityImage(){
        return highQualityImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(title);
        dest.writeString(authors);
        dest.writeString(publishedDate);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(avgRating);
        dest.writeString(webReaderLink);
        dest.writeString(language);
        dest.writeString(image);
        dest.writeString(pageCount);
        dest.writeString(highQualityImage);
    }

    private void readFromParcel(Parcel in){
        Id=in.readString();
        title=in.readString();
        authors=in.readString();
        publishedDate=in.readString();
        description=in.readString();
        category=in.readString();
        avgRating=in.readString();
        webReaderLink=in.readString();
        language=in.readString();
        image=in.readString();
        pageCount=in.readString();
        highQualityImage=in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR=new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
