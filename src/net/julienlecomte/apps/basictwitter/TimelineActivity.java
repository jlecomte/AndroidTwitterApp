package net.julienlecomte.apps.basictwitter;

import java.util.ArrayList;

import net.julienlecomte.apps.basictwitter.models.Tweet;

import org.json.JSONArray;

import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TimelineActivity extends Activity {
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		setupViews();

		client = TwitterApp.getRestClient();

		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);

		populateTimeline();
	}

	void setupViews() {
		lvTweets = (ListView) findViewById(R.id.lvTweets);
	}

	void populateTimeline() {
		client.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray tweets) {
				aTweets.addAll(Tweet.fromJsonArray(tweets));
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s);
			}
		});
	}
}
