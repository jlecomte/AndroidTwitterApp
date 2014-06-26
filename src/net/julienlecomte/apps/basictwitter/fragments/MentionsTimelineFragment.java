package net.julienlecomte.apps.basictwitter.fragments;

public class MentionsTimelineFragment extends TweetsListFragment {

	@Override
	protected void fetchOldTweets(String max_id) {
		swipeLayout.setRefreshing(true);
		client.getMentionsTimeline(null, max_id, fetchOldTweetsHandler);
	}

	@Override
	protected void fetchNewTweets() {
		swipeLayout.setRefreshing(true);

		String since_id = null;

		if (tweets.size() > 0) {
			since_id = String.valueOf(tweets.get(0).getUid());
		}

		client.getMentionsTimeline(since_id, null, fetchNewTweetsHandler);
	}
}
