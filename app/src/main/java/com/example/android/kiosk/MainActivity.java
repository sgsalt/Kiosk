package com.example.android.kiosk;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    /**Create a static object for the Guardian JSON url */
    private static final String REQUEST_URL = "https://content.guardianapis.com/search?section=games&order-by=newest&show-fields=byline&q=games&api-key=1c2934de-0ee0-446d-9daa-6caf11fdf91e";

    /** Static value for the news item loader ID, in case we add more in the future */

    private static final int NEWS_ITEM_LOADER_ID = 1;

    /** The adapter for the news item list */
    private NewsItemAdapter mAdapter;

    /** Placeholder text in case the data doesn't load, leaving an empty TextView */
    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the ListView in the layout.
        ListView newsItemListView = findViewById(R.id.news_list);

        // Set placeholder text on the empty ListView in case no List is returned.
        mEmptyTextView = findViewById(R.id.empty_view);
        newsItemListView.setEmptyView(mEmptyTextView);

        // Create a new adapter that takes in a list of news articles as input.
        mAdapter = new NewsItemAdapter(this, new ArrayList<NewsItem>());

        // Set the adapter on the ListView, so the list can be populated.
        newsItemListView.setAdapter(mAdapter);

        // Set a click listener on the ListView, which sends a web browser intent to open a
        // to open a web page with the full news article.
        newsItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the news article that was clicked on.
                NewsItem currentNewsItem = mAdapter.getItem(position);

                // Convert the URL String into a URI object to pass into the intent constructor.
                Uri newsItemUri = Uri.parse(currentNewsItem.getUrl());

                // Create a new intent to view the article URI.
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsItemUri);

                // Send the intent to launch a new activity.
                startActivity(websiteIntent);
            }
        });

        //Get a reference to the ConnectivityManager to check the state of network connectivity.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active data network.
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data.
        if (networkInfo !=null && networkInfo.isConnected()) {
            //Get a reference to the loader manager to , in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter.
            // This is valid because this activity implements the LoaderCallbacks interface.
            loaderManager.initLoader(NEWS_ITEM_LOADER_ID, null, this);
        } else {
            // Otherwise, display the error message.
            // But first, hide the loading indicator so the error message will be visible.
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);

            // Update the empty state with the no connection error message.
            mEmptyTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    // onCreateLoader instantiates and returns a new loader for the given ID.
    public Loader<List<NewsItem>> onCreateLoader(int id, Bundle args) {

        // parse breaks apart the URI string that's passed into it as a parameter.
        Uri baseUri = Uri.parse(REQUEST_URL);

        //buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append the query parameter and it's value. For example the format, limit.
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("limit", "10");

        //Return the completed uri
        return new NewsItemLoader (this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> newsItems) {
        // Hide the loading indicator because data is loaded.
        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);

        // Set the empty state to display a no news found message.
        mEmptyTextView.setText(R.string.no_news);

        // Clear the adapter of previous data.
        mAdapter.clear();

        // If a valid list of articles is found, add to the data set (triggering a ListView update).
        if (newsItems != null && !newsItems.isEmpty()) {
            mAdapter.addAll(newsItems);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        // Loader reset to clear any existing data.
        mAdapter.clear();
    }


}
