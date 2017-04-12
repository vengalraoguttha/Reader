package com.vengalrao.android.reader;

import android.content.res.Configuration;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.vengalrao.android.reader.Utilities.Book;
import com.vengalrao.android.reader.Utilities.NetworkUtilities;
import com.vengalrao.android.reader.data.BookContrack;
import com.vengalrao.android.reader.sync.MyBookJobService;
import com.vengalrao.android.reader.ui.BookAdapter;
import com.vengalrao.android.reader.ui.SearchQueryDialog;

import java.net.URL;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,BookAdapter.GridItemClickListener{

    Toolbar toolbar;
    RecyclerView mRecyclerView;
    TextView mTextView;
    Book[] books;
    BookAdapter adapter;
    private static final String KEY="QUERY";
    public static int LOADER_ID=111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar)findViewById(R.id.toolBar_main);
        setSupportActionBar(toolbar);
        mRecyclerView=(RecyclerView)findViewById(R.id.book_main_recycler_view);
        mTextView=(TextView)findViewById(R.id.no_net);
        adapter=new BookAdapter(this,this);
        int columns;
        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT){
            columns=2;
        }else{
            columns=3;
        }
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,columns);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        loadData();
        //Bundle bundle=new Bundle();
        //bundle.putString(KEY,"Novels");
        //service can be implemented by uncommenting given lines.
        /*FirebaseJobDispatcher jobDispatcher=new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job bookLoadJob=jobDispatcher.newJobBuilder()
                .setService(MyBookJobService.class)
                .setTag("BOOKS_JOB")
                .setRecurring(false)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(0,600))
                .setExtras(bundle)
                .build();
        jobDispatcher.mustSchedule(bookLoadJob);
        fromDataBase();*/
    }

    public void button(View view){
        new SearchQueryDialog().show(getFragmentManager(),"Search Dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    public void fromDataBase(){
        Cursor cursor=getContentResolver().query(BookContrack.BookGeneral.CONTENT_GENERAL_URI,
        null,
        null,
        null,
        null);
        if(cursor!=null) {
            books = new Book[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++) {
                books[i] = new Book();
                books[i].setId(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_ID)));
                books[i].setTitle(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_TITLE)));
                books[i].setAuthors(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_AUTHOR)));
                books[i].setPublishedDate(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_PUBLISHED_DATE)));
                books[i].setDescription(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_DESCRIPTION)));
                books[i].setCategory(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_CATEGORY)));
                books[i].setAvgRating(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_AVG_RATING)));
                books[i].setWebReaderLink(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_WEB_READER_LINK)));
                books[i].setLanguage(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_LANG)));
                books[i].setImage(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_IMAGE)));
                books[i].setPageCount(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_PAGE_COUNT)));
            }
            adapter.setData(books);
            adapter.notifyDataSetChanged();
        }
    }

    public void searchQuery(String s){
        if(s!=null&&!s.equals("")) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY, s);
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> bookLoader = loaderManager.getLoader(LOADER_ID);
            if (bookLoader == null) {
                loaderManager.initLoader(LOADER_ID, bundle, this);
            } else {
                loaderManager.restartLoader(LOADER_ID, bundle, this);
            }
        }
    }

    public void loadData(){
        Bundle bundle=new Bundle();
        bundle.putString(KEY,"Android Programming");
        LoaderManager loaderManager=getSupportLoaderManager();
        Loader<String> bookLoader=loaderManager.getLoader(LOADER_ID);
        if(bookLoader==null){
            loaderManager.initLoader(LOADER_ID,bundle,this);
        }else{
            loaderManager.restartLoader(LOADER_ID,bundle,this);
        }
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
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            public String loadInBackground() {
                URL url=NetworkUtilities.buildUrl(args.getString(KEY));
                return NetworkUtilities.getResponseFromHttpUrl(url);
            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(data!=null&&!data.equals("")){
            showBooksData();
            books=NetworkUtilities.getParsedData(data);
            adapter.setData(books);
        }else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onGridItemClickListener(int clickedPosition) {

    }
}
