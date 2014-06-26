package net.julienlecomte.apps.basictwitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant end point in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1";
    public static final String REST_CONSUMER_KEY = "1zkB4CeczbniHaRH9hVT6139T";
    public static final String REST_CONSUMER_SECRET = "NDS2Oz485w3Ay4anaii2prfKgxd7tuoXwYq7ktd1jgCVZ4R6DN";
    public static final String REST_CALLBACK_URL = "oauth://cpbasictweets";
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    private void getTimeline(String api, String since_id, String max_id, AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl(api);

    	RequestParams params = new RequestParams();

    	if (since_id != null) {
    		params.put("since_id", since_id);
    	} else if (max_id != null) {
    		params.put("max_id", max_id);
    	} else {
    		params.put("since_id", "1");
    	}
   
    	client.get(apiUrl, params, handler);
    }

    /* 1. Define the end point URL with getApiUrl and pass a relative path to the end point
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */

    public void getHomeTimeline(String since_id, String max_id, AsyncHttpResponseHandler handler) {
    	getTimeline("statuses/home_timeline.json", since_id, max_id, handler);
    }

    public void getMentionsTimeline(String since_id, String max_id, AsyncHttpResponseHandler handler) {
    	getTimeline("statuses/mentions_timeline.json", since_id, max_id, handler);
    }

    public void postUpdate(String status, AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("statuses/update.json");
    	RequestParams params = new RequestParams();
    	params.put("status", status);
    	client.post(apiUrl, params, handler);
    }
}