package com.vengalrao.android.reader.ui;


import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vengalrao.android.reader.MainActivity;
import com.vengalrao.android.reader.R;
import com.vengalrao.android.reader.Utilities.Book;
import com.vengalrao.android.reader.Utilities.NetworkUtilities;

import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class GeneralFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>,BookAdapter.GridItemClickListener,SharedPreferences.OnSharedPreferenceChangeListener,SearchQueryDialog.SearchInterface {

    RecyclerView mRecyclerView;
    Book[] books;
    BookAdapter adapter;
    TextView mTextView;
    private static final String KEY="QUERY";
    private static final String KEY_IMG="QUERY_IMG";
    private static final String KEY_STYPE="S_TYPE";
    private static String INTENT_SEND="Data";
    private static String SAVE_BUNDLE="SaveBundle";
    public static int LOADER_ID=111;
    private SharedPreferences sharedPreferences;
    public GeneralFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_general, container, false);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.book_main_recycler_view);
        mTextView=(TextView)view.findViewById(R.id.no_net);
        adapter=new BookAdapter(getContext(),this);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        int columns;
        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            columns=2;
        }else{
            columns=3;
        }
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),columns);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        if(savedInstanceState==null){
            String s1=sharedPreferences.getString(getString(R.string.book_search_pref_key),getString(R.string.book_search_default_val));
            String s2=sharedPreferences.getString(getString(R.string.list_search_by_key),getString(R.string.search_by_author_key));
            searchQuery(s1,s2,"Harry Potter",false);
        }
        else {
            books=toBooksArray(savedInstanceState.getParcelableArray(SAVE_BUNDLE));
            adapter.setData(books);
        }

        return view;
    }

    public Book[] toBooksArray(Parcelable[] parcelables){
        if(parcelables!=null){
            Book[] b=new Book[parcelables.length];
            for (int i=0;i<parcelables.length;i++){
                b[i]=new Book();
                b[i]=(Book)parcelables[i];
            }
            return b;
        }else {
            return null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(SAVE_BUNDLE,books);
    }

    public void searchQuery(String s, String s2, String query, boolean flag){
        if(s!=null&&!s.equals("")) {
            Bundle bundle = new Bundle();
            if(flag){
                bundle.putString(KEY_IMG,s);
            }else{
                bundle.putString(KEY, query);
                bundle.putString(KEY_STYPE,s2);
                bundle.putString("S_VAL",s);
            }
            LoaderManager loaderManager = getActivity().getSupportLoaderManager();
            Loader<String> bookLoader = loaderManager.getLoader(LOADER_ID);
            if (bookLoader == null) {
                loaderManager.initLoader(LOADER_ID, bundle, this);
            } else {
                loaderManager.restartLoader(LOADER_ID, bundle, this);
            }
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(getContext()) {
            @Override
            public String loadInBackground() {
                if(args.containsKey(KEY)){
                    String s=null;
                    String s2=null;
                    URL url=null;
                    if(args.containsKey(KEY_STYPE)){
                        s=args.getString(KEY_STYPE);
                        s2=args.getString("S_VAL");
                        url= NetworkUtilities.buildSpecificUrl(s,s2,args.getString(KEY));
                    }else{
                        url=NetworkUtilities.buildUrl(args.getString(KEY));
                    }
                    return NetworkUtilities.getResponseFromHttpUrl(url);
                }else{
                    if(books!=null){
                        for(int i=0;i<books.length;i++){
                            books[i].setHighQualityImage(NetworkUtilities.getHighQualityImage(books[i].getId()));
                        }
                    }else{
                        //Toast.makeText(MainActivity.this, "No Books available with given query.", Toast.LENGTH_SHORT).show();
                        return null;
                    }
                    return "###";
                }
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }
        };
    }

    public void showBooksData(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.INVISIBLE);
    }

    public void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mTextView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(data!=null&&!data.equals("")&&!data.equals("###")){
            showBooksData();
            books=NetworkUtilities.getParsedData(data);
            searchQuery("###","###","###",true);
            adapter.setData(books);
        }else if(("###").equals(data)){

        }else{
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onGridItemClickListener(int clickedPosition,ImageView imageView) {
        Intent intent=new Intent(getContext(), DetailActivity.class);
        intent.putExtra(INTENT_SEND,books[clickedPosition]);
        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(getActivity(),imageView,getString(R.string.image_transition));
        startActivity(intent,options.toBundle());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String s1=sharedPreferences.getString(getString(R.string.book_search_pref_key),getString(R.string.book_search_default_val));
        String s2=sharedPreferences.getString(getString(R.string.list_search_by_key),getString(R.string.search_by_author_key));
        searchQuery(s1,s2,"books",false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void searchMethod(String s1, String s2, String s3, boolean flag) {
        searchQuery(s1,s2,s3,flag);
    }
}
