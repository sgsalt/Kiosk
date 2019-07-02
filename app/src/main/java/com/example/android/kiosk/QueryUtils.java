package com.example.android.kiosk;

import android.text.TextUtils;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.kiosk.MainActivity.LOG_TAG;

/**
 * Helper methods for requesting and receiving data from the Guardian URL.
 */

public final class QueryUtils {

    /**
     * Create a private constructor (never a QueryUtils object). This class only holds static
     * variables and methods which can be accessed directly from the class QueryUtils.
     */
    private QueryUtils() {
    }

    /**
     * Return a list of NewsItem objects that has been built up from parsing the JSON response.
     */
    private static List<NewsItem> extractFeatureFromJson(String newsItemJSON) {
        // If the JSON string is empty, return early.
        if (TextUtils.isEmpty((newsItemJSON))) {
            return null;
        }

        // Create an empty ArrayList that we can populate with articles.
        List<NewsItem> newsItems = new ArrayList<>();

        // Try to parse the JSON response, if there's a problem an exception will be thrown.
        // Catch the exception so the app doesn't crash.
        try {

            // Create a JSON object from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsItemJSON);

            JSONObject baseJsonResults = baseJsonResponse.getJSONObject("response");

            // Extract the JSONArray associated with they key called "results"
            // Which is a list of articles (NewsItems).
            JSONArray newsItemArray = baseJsonResults.getJSONArray("results");

            // For each article in the newsItemArray, create a NewsItem object
            for (int i = 0; i < newsItemArray.length(); i++) {

                // Get a single earthquake at position i within the list of articles.
                JSONObject currentNewsItem = newsItemArray.getJSONObject(i);
                String title = currentNewsItem.getString("webTitle");
                String author = ("unknown");
                if (currentNewsItem.has("fields")) {
                    JSONObject fieldObject = currentNewsItem.getJSONObject("fields");

                    if (fieldObject.has("byline")) {
                        author = fieldObject.getString("byline");
                    }
                }

                // For a given article, extract the JSONObjects
                String section = currentNewsItem.getString("sectionName");
                String date = currentNewsItem.getString("webPublicationDate");
                String url = currentNewsItem.getString("webUrl");

                // Create a new NewsItem with the title, author, section, date and url.
                NewsItem newsItem = new NewsItem(title, author, section, date, url);

                // Add the new NewsItem to the list of articles.
                newsItems.add(newsItem);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the try block,
            // catch the exception here so that the app doesn't crash.
            // Print a log message with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        // Return the list of newsItems.
        return newsItems;

    }

    /**
     * Returns a new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating the URL", e);
        }
        return url;
    }

    /**
     * Make a HTTP request to the given URL and return the string as a response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;

    }

    /**
     * Convert the InputStream into a string which contains the whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();

    }

    /**
     * Query the Guardian data set and return a list of articles.
     */
    public static List<NewsItem> fetchNewsItemData(String requestUrl) {
        //Create the URL object
        URL url = createUrl(requestUrl);

        // Perform the HTTP request and receive JSON response
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract the relevant fields and create the list.
        List<NewsItem> newsItems = extractFeatureFromJson(jsonResponse);

        // Return the list of articles.
        return newsItems;
    }


}
