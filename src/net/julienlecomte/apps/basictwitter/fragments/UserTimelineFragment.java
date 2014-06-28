package net.julienlecomte.apps.basictwitter.fragments;

import android.os.Bundle;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {
	protected String user_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
	    if (bundle != null) {
	    	user_id = bundle.getString("user_id");
	    }

		super.onCreate(savedInstanceState);
	}

	@Override
	protected void getTimeline(String since_id, String max_id,
			AsyncHttpResponseHandler handler) {
		client.getUserTimeline(user_id, since_id, max_id, handler);
	}
}
