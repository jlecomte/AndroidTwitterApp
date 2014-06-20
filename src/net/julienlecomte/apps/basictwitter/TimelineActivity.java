package net.julienlecomte.apps.basictwitter;

import java.util.ArrayList;

import net.julienlecomte.apps.basictwitter.models.Tweet;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;

	private static final int COMPOSE_ACTIVITY_REQUEST_CODE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		setupViews();

		client = TwitterApp.getRestClient();

		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);

		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				String max_id = String.valueOf(tweets.get(tweets.size()-1).getUid());
				fetchOldTweets(max_id);
			}
		});

		populateTimeline();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_timeline_menu, menu);
		return true;
	}

	void setupViews() {
		lvTweets = (ListView) findViewById(R.id.lvTweets);
	}

	void populateTimeline() {
		fetchOldTweets(null);
	}

	void fetchOldTweets(String max_id) {
		client.getHomeTimeline(null, max_id, new JsonHttpResponseHandler() {
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

	void fetchNewTweets() {
		String since_id = String.valueOf(tweets.get(0).getUid());
		client.getHomeTimeline(since_id, null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray tweets) {
				TimelineActivity.this.tweets.addAll(0, Tweet.fromJsonArray(tweets));
				TimelineActivity.this.aTweets.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s);
			}
		});
	}

	public void onCompose(MenuItem mi) {
		Intent i = new Intent(this, ComposeActivity.class);
		startActivityForResult(i, COMPOSE_ACTIVITY_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == COMPOSE_ACTIVITY_REQUEST_CODE &&
				resultCode == RESULT_OK) {
			String status = data.getStringExtra("status").trim();
			if (status.length() > 0) {
				client.postUpdate(status, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject jsonObject) {
						TimelineActivity.this.fetchNewTweets();
					}

					@Override
					public void onFailure(Throwable e, String s) {
						Log.d("debug", e.toString());
						Log.d("debug", s);

						// TODO: localize error message...
						Toast.makeText(TimelineActivity.this, "An error occurred!", Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
	}
}
