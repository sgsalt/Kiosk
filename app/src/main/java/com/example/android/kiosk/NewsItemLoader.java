package com.example.android.kiosk;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of news articles using an AsyncTask to perform the network request.
 */

public class NewsItemLoader extends AsyncTaskLoader<List<NewsItem>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsItemLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new NewsItemLoader.
     *
     * @param context of the activity.
     * @param url     to request the data from
     */
    public NewsItemLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * On the background thread.
     */
    @Override
    public List<NewsItem> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response and extract a list of articles.
        List<NewsItem> newsItems = QueryUtils.fetchNewsItemData(mUrl);
        return newsItems;
    }
}
