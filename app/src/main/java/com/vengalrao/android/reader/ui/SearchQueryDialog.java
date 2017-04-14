package com.vengalrao.android.reader.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.vengalrao.android.reader.MainActivity;
import com.vengalrao.android.reader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by vengalrao on 12-04-2017.
 */

public class SearchQueryDialog extends DialogFragment {

    EditText mEditText;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View custom = inflater.inflate(R.layout.search_query_dialog,null);
        ButterKnife.bind(this,custom);
        mEditText=(EditText)custom.findViewById(R.id.dialog_search) ;
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                findResult();
                return true;
            }
        });
        builder.setView(custom);
        builder.setMessage(getString(R.string.search_dialog_title));
        builder.setPositiveButton(getString(R.string.search_dialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        findResult();
                    }
                });
        builder.setNegativeButton(getString(R.string.search_dialog_cancel), null);
        Dialog dialog = builder.create();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        return dialog;
    }
    private void findResult(){
        Activity parent=getActivity();
        if(parent instanceof MainActivity){
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
            String s1=sharedPreferences.getString(getString(R.string.book_search_pref_key),getString(R.string.book_search_default_val));
            String s2=sharedPreferences.getString(getString(R.string.list_search_by_key),getString(R.string.search_by_author_key));
            ((MainActivity)parent).searchQuery(s1,s2,mEditText.getText().toString(),false);
        }
        dismissAllowingStateLoss();
    }
}
