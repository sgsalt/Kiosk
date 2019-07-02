package com.example.android.kiosk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * This is an adapter that knows how to create a list item layout for each article in the data source.
 *
 * These list item layouts will be provided to an adapter view (ListView) to be displayed on the screen.
 */

public class NewsItemAdapter extends ArrayAdapter<NewsItem> {

    /**
     * Constructs a new NewsItem adapter
     *
     * @param context of the app
     * @param newsItems is the list of articles (the data source)
     */
    public NewsItemAdapter(Context context, List<NewsItem> newsItems) {
        super(context, 0, newsItems);
    }

    /**
     * Returns a ListItem view that displays the articles at a given position
     * in the list of earthquakes.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Find the article at the given position in the list of articles
        NewsItem currentNewsItem = getItem(position);

        // Find the TextView with the ID: headline
        TextView headlineView = listItemView.findViewById(R.id.headline);
        // Get the headline from the NewsItem object
        String headlineText = currentNewsItem.getHeadline();
        // Display the headline of the article in that TextView
        headlineView.setText(headlineText);

        // Find the TextView with the ID: byline
        TextView bylineView = listItemView.findViewById(R.id.byline);
        // Get the byline from the NewsItem object
        String bylineText = currentNewsItem.getByline();
        // Display the byline of the article in that TextView
        bylineView.setText(bylineText);

        // Find the TextView with the ID: category
        TextView categoryView = listItemView.findViewById(R.id.category);
        // Get the category from the NewsItem object
        String categoryText = currentNewsItem.getCategory();
        // Display the category of the article in that TextView
        categoryView.setText(categoryText);

        // Find the TextView with the ID: date
        TextView dateView = listItemView.findViewById(R.id.date);
        // Get the date from the NewsItem object
        String dateText = currentNewsItem.getDate();
        // Display the date of the article in that TextView
        dateView.setText(dateText);
        //TODO: Format the date if needed.

        // Return the ListView which now should be showing the appropriate data
        return listItemView;
    }
}
