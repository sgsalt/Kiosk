package com.example.android.kiosk;

/**
 * An {@link NewsItem} object which contains a headline, byline, category and timestamp for each article.
 */

public class NewsItem {

    /** Headline of the article */
    private String mHeadline;

    /** Byline (writer) of the article */
    private String mByline;

    /** Category of the article */
    private String mCategory;

    /** Timestamp for the article */
    private String mDate;

    /** URL of the article */
    private String mUrl;

    /** This is the constructor for each new {@link com.example.android.kiosk.NewsItem} object.
     *
     * @param headline is the headline of the article.
     * @param byline is the writer of the article.
     * @param category is the category of the article.
     * @param date is the timestamp for the article.
     * @param url is the full webpage for that article.
      */
    public NewsItem(String headline, String byline, String category, String date, String url) {
        mHeadline = headline;
        mByline = byline;
        mCategory = category;
        mDate = date;
        mUrl = url;
    }

    /**
     * Returns the headline.
     */
    public String getHeadline() {
        return mHeadline;
    }

    /**
     * Returns the byline (author).
     */
    public String getByline() {
        return mByline;
    }

    /**
     * Returns the category.
     */
    public String getCategory() {
        return mCategory;
    }

    /**
     * Returns the date.
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Returns the url.
     */
    public String getUrl() {
        return mUrl;
    }
}
