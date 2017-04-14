package com.vengalrao.android.reader.ui;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vengalrao.android.reader.R;
import com.vengalrao.android.reader.Utilities.Book;
import com.vengalrao.android.reader.data.BookContrack;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeatailFragment extends Fragment {

    private static String INTENT_RECEIVE="Data";
    private static String DIALOG_DATA="Dialog";
    ImageView mImageView;
    TextView authorNameText;
    TextView bookSizeText;
    TextView bookCategoryText;
    TextView bookLangText;
    TextView descriptionText;
    FloatingActionButton favfab;
    Book book;
    Button mButton;
    public DeatailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_deatail, container, false);
        Intent intent=getActivity().getIntent();
        book=(Book) intent.getParcelableExtra(INTENT_RECEIVE);
        mImageView=(ImageView)view.findViewById(R.id.detail_book_image);
        authorNameText=(TextView)view.findViewById(R.id.author_name_text);
        bookSizeText=(TextView)view.findViewById(R.id.book_size_text);
        bookCategoryText=(TextView)view.findViewById(R.id.book_category_text);
        bookLangText=(TextView)view.findViewById(R.id.book_lang_text);
        descriptionText=(TextView)view.findViewById(R.id.text_description);
        mButton=(Button)view.findViewById(R.id.read_book);
        Toolbar toolbar=(Toolbar) view.findViewById(R.id.detail_toolbar);
        favfab=(FloatingActionButton)view.findViewById(R.id.favorite_fab);
        favfab.setTag("tag_white");
        if(checkData()){
            favfab.setImageResource((R.drawable.gold_star));
            favfab.setTag("tag_gold");
        }

        if(book.getHighQualityImage()!=null)
        Picasso.with(getContext()).load(book.getHighQualityImage()).into(mImageView);
        else{
            if(book.getImage()!=null){
                Picasso.with(getContext()).load(book.getImage()).into(mImageView);
            }else{
                mImageView.setImageResource(R.drawable.bookcoverplaceholder);
            }
        }

        toolbar.setTitle(book.getTitle());
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        authorNameText.setText(book.getAuthors());
        bookSizeText.setText(book.getPageCount());
        bookCategoryText.setText(book.getCategory());
        bookLangText.setText(book.getLanguage());
        descriptionText.setText(book.getDescription());

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(book.getWebReaderLink()));
                startActivity(i);
            }
        });

        favfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favfab.getTag().equals("tag_white")){
                    Uri uri=insertIntoDataBase();
                    if(uri!=null){
                        Toast.makeText(getContext(),getContext().getString(R.string.toast_fav_added),Toast.LENGTH_SHORT).show();
                        favfab.setImageResource(R.drawable.gold_star);
                    }else{
                        Toast.makeText(getContext(),getContext().getString(R.string.toast_fav_failed), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    int del=deleteFromDataBase();
                    if(del>0){
                        favfab.setImageResource(R.drawable.ic_star_rate_white_18px);
                        Toast.makeText(getContext(),getContext().getString(R.string.toast_fav_removed),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(),getContext().getString(R.string.toast_fav_removed_failed),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(DIALOG_DATA,book);
    }

    public boolean checkData(){
        Cursor cursor=getContext().getContentResolver().query(BookContrack.BookFavorite.CONTENT_FAV_URI,
                null,
                null,
                null,
                null
        );
        if(cursor!=null){
            cursor.moveToFirst();
            for(int i=0;i<cursor.getCount();i++){
                if(cursor.getString(cursor.getColumnIndex(BookContrack.BookFavorite.BOOK_ID)).equals(book.getId())){
                    return true;
                }
                cursor.moveToNext();
            }
        }
        return false;
    }

    public Uri insertIntoDataBase(){
        ContentValues contentValues=new ContentValues();
        contentValues.put(BookContrack.BookFavorite.BOOK_ID,book.getId());
        contentValues.put(BookContrack.BookFavorite.BOOK_TITLE,book.getTitle());
        contentValues.put(BookContrack.BookFavorite.BOOK_AUTHOR,book.getAuthors());
        contentValues.put(BookContrack.BookFavorite.BOOK_PUBLISHED_DATE,book.getPublishedDate());
        contentValues.put(BookContrack.BookFavorite.BOOK_DESCRIPTION,book.getDescription());
        contentValues.put(BookContrack.BookFavorite.BOOK_CATEGORY,book.getCategory());
        contentValues.put(BookContrack.BookFavorite.BOOK_AVG_RATING,book.getAvgRating());
        contentValues.put(BookContrack.BookFavorite.BOOK_LANG,book.getLanguage());
        contentValues.put(BookContrack.BookFavorite.BOOK_IMAGE,book.getImage());
        contentValues.put(BookContrack.BookFavorite.BOOK_WEB_READER_LINK,book.getWebReaderLink());
        contentValues.put(BookContrack.BookFavorite.BOOK_PAGE_COUNT,book.getPageCount());
        contentValues.put(BookContrack.BookFavorite.BOOk_HIGH_QUALITY_IMG,book.getHighQualityImage());
        Uri uri=getContext().getContentResolver().insert(BookContrack.BookFavorite.CONTENT_FAV_URI,contentValues);
        return uri;
    }

    public int deleteFromDataBase(){
        Uri uri= BookContrack.BookFavorite.CONTENT_FAV_URI.buildUpon().appendPath(book.getId()).build();
        int x=getContext().getContentResolver().delete(uri,null,null);
        return x;
    }


}
