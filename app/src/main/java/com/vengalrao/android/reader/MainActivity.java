package com.vengalrao.android.reader;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.vengalrao.android.reader.ui.DetailActivity;
import com.vengalrao.android.reader.ui.GeneralFragment;
import com.vengalrao.android.reader.ui.PagerAdapter;
import com.vengalrao.android.reader.ui.SearchQueryDialog;
import com.vengalrao.android.reader.ui.Settings;

import java.net.URL;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity{

    //changes
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar)findViewById(R.id.toolBar_main);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.main_pager);
        final PagerAdapter adapterPager = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapterPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_setting){
            Intent intent=new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    public void fromDataBase(){
//        Cursor cursor=getContentResolver().query(BookContrack.BookGeneral.CONTENT_GENERAL_URI,
//        null,
//        null,
//        null,
//        null);
//        if(cursor!=null) {
//            books = new Book[cursor.getCount()];
//            for (int i = 0; i < cursor.getCount(); i++) {
//                books[i] = new Book();
//                books[i].setId(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_ID)));
//                books[i].setTitle(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_TITLE)));
//                books[i].setAuthors(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_AUTHOR)));
//                books[i].setPublishedDate(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_PUBLISHED_DATE)));
//                books[i].setDescription(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_DESCRIPTION)));
//                books[i].setCategory(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_CATEGORY)));
//                books[i].setAvgRating(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_AVG_RATING)));
//                books[i].setWebReaderLink(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_WEB_READER_LINK)));
//                books[i].setLanguage(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_LANG)));
//                books[i].setImage(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_IMAGE)));
//                books[i].setPageCount(cursor.getString(cursor.getColumnIndex(BookContrack.BookGeneral.BOOK_PAGE_COUNT)));
//            }
//            adapter.setData(books);
//            adapter.notifyDataSetChanged();
//        }
//    }





}
