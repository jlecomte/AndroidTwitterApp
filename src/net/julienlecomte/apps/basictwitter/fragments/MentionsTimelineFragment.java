package net.julienlecomte.apps.basictwitter.fragments;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class MentionsTimelineFragment extends TweetsListFragment {

	@Override
	protected void getTimeline(String since_id, String max_id,
			AsyncHttpResponseHandler handler) {
		client.getMentionsTimeline(since_id, max_id, handler);
	}
}
