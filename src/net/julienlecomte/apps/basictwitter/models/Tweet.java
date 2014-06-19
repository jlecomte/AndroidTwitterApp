package net.julienlecomte.apps.basictwitter.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {
	private long uid;
	private String body;
	private String createdAt;
	private User user;

	public static Tweet fromJson(JSONObject jsonObject) {
		Tweet tweet = new Tweet();

		try {
			tweet.uid = jsonObject.getLong("id");
			tweet.body = jsonObject.getString("text");
			tweet.createdAt = jsonObject.getString("created_at");
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return tweet;
	}

	public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObject;

			try {
				jsonObject = jsonArray.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}

			Tweet tweet = Tweet.fromJson(jsonObject);
			if (tweet != null) {
				tweets.add(tweet);
			}
		}

		return tweets;
	}

	public long getUid() {
		return uid;
	}

	public String getBody() {
		return body;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}
}
