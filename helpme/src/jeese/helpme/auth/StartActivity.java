package jeese.helpme.auth;

import jeese.helpme.R;
import jeese.helpme.R.id;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends ActionBarActivity {
	
	public static Activity StartActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_activity);
		StartActivity = this;
		init();
	}

	private void init() {

		Button loginButton = (Button) findViewById(id.login);
		Button registerButton = (Button) findViewById(id.register);

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 页面跳转到登录页面
				Intent intent = new Intent(StartActivity.this,
						LoginActivity.class);
				StartActivity.this.startActivity(intent);
			}
		});

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 页面跳转到注册手机号页面
				Intent intent = new Intent(StartActivity.this,
						RegisterPhoneActivity.class);
				StartActivity.this.startActivity(intent);
			}
		});

	}

}
