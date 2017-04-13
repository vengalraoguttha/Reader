package com.vengalrao.android.reader.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vengalrao.android.reader.R;
import com.vengalrao.android.reader.Utilities.Book;

/**
 * Created by vengalrao on 12-04-2017.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookAdapterViewHolder> {
    private Context mContext;
    final private GridItemClickListener mGridItemClickListener;
    Book[] books;

    public BookAdapter(Context context,GridItemClickListener listener){
        mContext=context;
        mGridItemClickListener=listener;
    }

    @Override
    public BookAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int layoutId=R.layout.book_item_layout;
        LayoutInflater inflater=LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately=false;
        View view=inflater.inflate(layoutId,parent,shouldAttachToParentImmediately);
        return new BookAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookAdapterViewHolder holder, int position) {
        Book currentBook=books[position];
        if(currentBook!=null){
            if(currentBook.getImage()!=null)
            Picasso.with(mContext).load(currentBook.getImage()).into(holder.bookImage);
            else
            holder.bookImage.setBackground(mContext.getDrawable(R.drawable.bookcoverplaceholder));
            holder.bookName.setText(currentBook.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        if(books==null)
            return 0;
        else
            return books.length;
    }

    public interface GridItemClickListener{
        void onGridItemClickListener(int clickedPosition,ImageView imageView);
    }

    class BookAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView bookImage;
        TextView bookName;
        public BookAdapterViewHolder(View itemView) {
            super(itemView);
            bookImage=(ImageView)itemView.findViewById(R.id.book_main_image);
            bookName=(TextView)itemView.findViewById(R.id.main_book_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition=getAdapterPosition();
            mGridItemClickListener.onGridItemClickListener(clickedPosition,bookImage);
        }
    }

    public void setData(Book[] data){
        books=data;
        notifyDataSetChanged();
    }
}
