package net.julienlecomte.apps.basictwitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class ComposeActivity extends Activity {
	private EditText etStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);

		setupViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_compose_menu, menu);
		return true;
	}

	void setupViews() {
		etStatus = (EditText) findViewById(R.id.etStatus);
	}

	public void onCancel(MenuItem mi) {
		Intent i = new Intent();
		setResult(RESULT_CANCELED, i);
		finish();
	}

	public void onSend(MenuItem mi) {
		Intent i = new Intent();
		i.putExtra("status", etStatus.getText().toString());
		setResult(RESULT_OK, i);
		finish();
	}
}
