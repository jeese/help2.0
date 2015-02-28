package jeese.helpme.auth;

import jeese.helpme.R;
import jeese.helpme.activity.MainActivity;
import jeese.helpme.view.SildingFinishLayout;
import jeese.helpme.view.SildingFinishLayout.OnSildingFinishListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class loginActivity extends ActionBarActivity {

	public static Activity loginPhoneActivity;
	private Toolbar mToolbar;
	private SildingFinishLayout mSildingFinishLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		loginPhoneActivity = this;
		init();
	}

	private void init() {
		setToolBar();

		EditText phone_edit = (EditText) findViewById(R.id.password_edit);

		Button phoneButton = (Button) findViewById(R.id.finish_button);
		phoneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ҳ����ת����ҳ��
				Intent intent = new Intent(loginActivity.this,
						MainActivity.class);
				loginActivity.this.startActivity(intent);
				StartActivity.StartActivity.finish();
				finish();
			}
		});

		mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
		mSildingFinishLayout
				.setOnSildingFinishListener(new OnSildingFinishListener() {

					@Override
					public void onSildingFinish() {
						finish();
					}
				});

		// touchViewҪ���õ�ListView����
		mSildingFinishLayout.setTouchView(mSildingFinishLayout);

	}

	private void setToolBar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// toolbar.setLogo(R.drawable.ic_launcher);
		mToolbar.setTitle("ʹ���ֻ��ŵ�½");// �������������setSupportActionBar֮ǰ����Ȼ����Ч
		// toolbar.setSubtitle("������");
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
