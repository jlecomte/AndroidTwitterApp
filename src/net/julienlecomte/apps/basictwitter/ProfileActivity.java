package net.julienlecomte.apps.basictwitter;

import net.julienlecomte.apps.basictwitter.models.User;

import org.json.JSONObject;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		loadProfileInfo();
	}

	protected void populateProfileHeader(User u) {
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagLine = (TextView) findViewById(R.id.tvTagLine);
		TextView tvFollowersCount = (TextView) findViewById(R.id.tvFollowersCount);
		TextView tvFollowingCount = (TextView) findViewById(R.id.tvFollowingCount);

		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(u.getImageProfileUrl(), ivProfileImage);

		tvName.setText(u.getName());
		tvTagLine.setText(u.getDescription());
		// TODO: i18n the following...
		tvFollowersCount.setText(u.getFollowersCount() + " Followers");
		tvFollowingCount.setText(u.getFriendsCount() + " Following");
	}

	protected void loadProfileInfo() {
		getActionBar().setTitle(R.string.loading_profile);
		TwitterApp.getRestClient().getMyInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject jsonObject) {
				User u = User.fromJson(jsonObject);
				getActionBar().setTitle("@" + u.getScreenName());
				populateProfileHeader(u);
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Toast.makeText(ProfileActivity.this, "Oops,  an error occurred:\n" + e.toString(),
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
