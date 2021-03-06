package net.julienlecomte.apps.basictwitter;

import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;

import net.julienlecomte.apps.basictwitter.fragments.HomeTimelineFragment;
import net.julienlecomte.apps.basictwitter.fragments.MentionsTimelineFragment;
import net.julienlecomte.apps.basictwitter.listeners.FragmentTabListener;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class TimelineActivity extends FragmentActivity {
	private TwitterClient client;

	private static final int COMPOSE_ACTIVITY_REQUEST_CODE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterApp.getRestClient();
		setupTabs();
	}

	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
			.newTab()
			.setText(R.string.home_tab_lbl)
			.setIcon(R.drawable.ic_home)
			.setTag("HomeTimelineFragment")
			.setTabListener(
				new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "home_timeline",
						HomeTimelineFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText(R.string.mentions_tab_lbl)
			.setIcon(R.drawable.ic_mentions)
			.setTag("MentionsTimelineFragment")
			.setTabListener(
			    new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "mentions_timeline",
			    		MentionsTimelineFragment.class));

		actionBar.addTab(tab2);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_timeline_menu, menu);
		return true;
	}

	public void onCompose(MenuItem mi) {
		Intent i = new Intent(this, ComposeActivity.class);
		startActivityForResult(i, COMPOSE_ACTIVITY_REQUEST_CODE);
	}

	public void onProfileView(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		startActivity(i);
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
						// Refresh home timeline...
						HomeTimelineFragment frag1 = (HomeTimelineFragment) getSupportFragmentManager().findFragmentByTag("home_timeline");
						if (frag1 != null) {
							frag1.fetchNewTweets();
						}
						// I suppose we should check whether I am mentioned to figure out
						// whether I also need to refresh the mentions timeline...
					}

					@Override
					public void onFailure(Throwable e, String s) {
						Log.d("debug", e.toString());
						// TODO: localize error message...
						Toast.makeText(TimelineActivity.this, "Oops,  an error occurred:\n" + e.toString(),
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
	}
}
