package com.vengalrao.android.reader.ui;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vengalrao.android.reader.R;
import com.vengalrao.android.reader.Utilities.Book;

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
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(DIALOG_DATA,book);
    }

}
