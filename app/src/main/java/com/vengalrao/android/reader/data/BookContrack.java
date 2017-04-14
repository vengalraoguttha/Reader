package com.vengalrao.android.reader.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by vengalrao on 12-04-2017.
 */

public class BookContrack {
    public static final String AUTHORITY="com.vengalrao.android.reader";
    public static final Uri BASE_CONTENTURI=Uri.parse("content://"+AUTHORITY);

    public static final String PATH_GENERAL="general";
    public static final String PATH_FAV="favorite";

    public static final class BookGeneral implements BaseColumns{
        public static final Uri CONTENT_GENERAL_URI=BASE_CONTENTURI.buildUpon().appendPath(PATH_GENERAL).build();
        public static final String TABLE_NAME="General";
        public static final String BOOK_ID="BookId";
        public static final String BOOK_TITLE="BookTitle";
        public static final String BOOK_AUTHOR="BookAuthor";
        public static final String BOOK_PUBLISHED_DATE="publishedDate";
        public static final String BOOK_DESCRIPTION="description";
        public static final String BOOK_CATEGORY="category";
        public static final String BOOK_AVG_RATING="rating";
        public static final String BOOK_LANG="language";
        public static final String BOOK_IMAGE="image";
        public static final String BOOK_WEB_READER_LINK="webReaderLink";
        public static final String BOOK_PAGE_COUNT="pageCount";
        public static final String BOOk_HIGH_QUALITY_IMG="highQualityImg";
    }

    public static final class BookFavorite implements BaseColumns{
        public static final Uri CONTENT_FAV_URI=BASE_CONTENTURI.buildUpon().appendPath(PATH_FAV).build();
        public static final String TABLE_NAME="Favorite";
        public static final String BOOK_ID="BookId";
        public static final String BOOK_TITLE="BookTitle";
        public static final String BOOK_AUTHOR="BookAuthor";
        public static final String BOOK_PUBLISHED_DATE="publishedDate";
        public static final String BOOK_DESCRIPTION="description";
        public static final String BOOK_CATEGORY="category";
        public static final String BOOK_AVG_RATING="rating";
        public static final String BOOK_LANG="language";
        public static final String BOOK_IMAGE="image";
        public static final String BOOK_WEB_READER_LINK="webReaderLink";
        public static final String BOOK_PAGE_COUNT="pageCount";
        public static final String BOOk_HIGH_QUALITY_IMG="highQualityImg";
    }
}
