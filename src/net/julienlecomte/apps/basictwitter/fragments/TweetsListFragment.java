package net.julienlecomte.apps.basictwitter.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import net.julienlecomte.apps.basictwitter.EndlessScrollListener;
import net.julienlecomte.apps.basictwitter.R;
import net.julienlecomte.apps.basictwitter.TweetArrayAdapter;
import net.julienlecomte.apps.basictwitter.TwitterApp;
import net.julienlecomte.apps.basictwitter.TwitterClient;
import net.julienlecomte.apps.basictwitter.models.Tweet;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

public abstract class TweetsListFragment extends Fragment {
	protected SwipeRefreshLayout swipeLayout;

	protected ArrayList<Tweet> tweets;
	protected ArrayAdapter<Tweet> aTweets;
	protected ListView lvTweets;

	protected TwitterClient client;

	protected JsonHttpResponseHandler fetchOldTweetsHandler;
	protected JsonHttpResponseHandler fetchNewTweetsHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		client = TwitterApp.getRestClient();
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(getActivity(), tweets);

		fetchOldTweetsHandler = new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				aTweets.addAll(Tweet.fromJsonArray(jsonTweets));
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				// TODO: localize error message...
				Toast.makeText(getActivity(), "Oops,  an error occurred:\n" + e.toString(),
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFinish() {
				swipeLayout.setRefreshing(false);
			}
		};

		fetchNewTweetsHandler = new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				tweets.addAll(0, Tweet.fromJsonArray(jsonTweets));
				aTweets.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				// TODO: localize error message...
				Toast.makeText(getActivity(), "Oops,  an error occurred:\n" + e.toString(),
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFinish() {
				swipeLayout.setRefreshing(false);
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

		swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);

	    swipeLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				fetchNewTweets();
			}
		});

	    swipeLayout.setColorScheme(android.R.color.holo_blue_dark,
	            android.R.color.holo_green_dark,
	            android.R.color.holo_orange_dark,
	            android.R.color.holo_red_dark);

		lvTweets = (ListView) v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);

		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				String max_id = null;

				if (tweets.size() > 0) {
					max_id = String.valueOf(tweets.get(tweets.size()-1).getUid());
				}

				fetchOldTweets(max_id);
			}
		});

		populateTimeline();

		return v;
	}

	protected void populateTimeline() {
		fetchOldTweets(null);
	}

	protected void fetchOldTweets(String max_id) {
		swipeLayout.setRefreshing(true);
		getTimeline(null, max_id, fetchOldTweetsHandler);
	}

	protected void fetchNewTweets() {
		swipeLayout.setRefreshing(true);

		String since_id = null;

		if (tweets.size() > 0) {
			since_id = String.valueOf(tweets.get(0).getUid());
		}

		getTimeline(since_id, null, fetchNewTweetsHandler);
	}

	abstract protected void getTimeline(String since_id, String max_id,
			AsyncHttpResponseHandler handler);
}
