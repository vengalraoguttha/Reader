package com.vengalrao.android.reader.sync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.Bundle;

import com.vengalrao.android.reader.Utilities.Book;
import com.vengalrao.android.reader.Utilities.NetworkUtilities;
import com.vengalrao.android.reader.Utilities.ServiceUtilities;
import com.vengalrao.android.reader.ui.BookAdapter;

import java.net.URL;

/**
 * Created by vengalrao on 12-04-2017.
 */

public class MyBookJobService extends com.firebase.jobdispatcher.JobService {
    private AsyncTask mBackgroundTask;
    private String KEY="QUERY";
    private Book[] books;

    @Override
    public boolean onStartJob(final com.firebase.jobdispatcher.JobParameters job) {
        final Bundle bundle=job.getExtras();
        mBackgroundTask=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                URL url= NetworkUtilities.buildUrl(bundle.getString(KEY));
                String s=NetworkUtilities.getResponseFromHttpUrl(url);
                books=NetworkUtilities.getParsedData(s);
                ServiceUtilities.updateDataBase(books);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                jobFinished(job,false);
            }
        };
        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        if(mBackgroundTask!=null){
            mBackgroundTask.cancel(true);
        }
        return true;
    }
}
