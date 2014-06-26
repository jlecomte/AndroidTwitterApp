package net.julienlecomte.apps.basictwitter.fragments;

public class HomeTimelineFragment extends TweetsListFragment {

	@Override
	protected void fetchOldTweets(String max_id) {
		swipeLayout.setRefreshing(true);
		client.getHomeTimeline(null, max_id, fetchOldTweetsHandler);
	}

	@Override
	protected void fetchNewTweets() {
		swipeLayout.setRefreshing(true);
		String since_id = String.valueOf(tweets.get(0).getUid());
		client.getHomeTimeline(since_id, null, fetchNewTweetsHandler);
	}
}
