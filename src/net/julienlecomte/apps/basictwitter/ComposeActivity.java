package net.julienlecomte.apps.basictwitter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class ComposeActivity extends Activity {
	private static final int STATUS_MAX_LENGTH = 140;
	private EditText etStatus;
	private TextView tvCharCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);

		setupViews();

		tvCharCount.setText(String.valueOf(STATUS_MAX_LENGTH));
		tvCharCount.setTextColor(Color.GRAY);

		etStatus.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {
				int count = STATUS_MAX_LENGTH - s.length();
				tvCharCount.setText(String.valueOf(count));
				if (count > 10) {
					tvCharCount.setTextColor(Color.GRAY);
				} else if (count > 5){
					tvCharCount.setTextColor(Color.rgb(240, 190, 40)); // orange
				} else {
					tvCharCount.setTextColor(Color.RED);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_compose_menu, menu);
		return true;
	}

	void setupViews() {
		etStatus = (EditText) findViewById(R.id.etStatus);
		tvCharCount = (TextView) findViewById(R.id.tvCharCount);
	}

	public void onSend(MenuItem mi) {
		Intent i = new Intent();
		i.putExtra("status", etStatus.getText().toString());
		setResult(RESULT_OK, i);
		finish();
	}
}
