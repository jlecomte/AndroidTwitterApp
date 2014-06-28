package net.julienlecomte.apps.basictwitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.nostra13.universalimageloader.core.ImageLoader;

import net.julienlecomte.apps.basictwitter.models.Tweet;
import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	public TweetArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = getItem(position);

		View v;
		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			v = inflator.inflate(R.layout.tweet_item, parent, false);
		} else {
			v = convertView;
		}

		ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
		TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
		TextView tvTweetBody = (TextView) v.findViewById(R.id.tvTweetBody);
		TextView tvTimestamp = (TextView) v.findViewById(R.id.tvTimestamp);

		ivProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(tweet.getUser().getImageProfileUrl(), ivProfileImage);

		tvUserName.setText("@" + tweet.getUser().getScreenName());
		tvTweetBody.setText(tweet.getBody());
		tvTimestamp.setText(getRelativeTimeAgo(tweet.getCreatedAt()));

		return v;
	}

	// getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
	public String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);

		String relativeDate = "";
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return relativeDate;
	}
}
