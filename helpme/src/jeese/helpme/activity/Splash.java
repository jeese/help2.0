package jeese.helpme.activity;

import jeese.helpme.R;
import jeese.helpme.application.App;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 0; // �ӳ�ʱ��

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		// �ж�app״̬
		SharedPreferences preferences = getSharedPreferences("e_help",
				Context.MODE_PRIVATE);
		Boolean first_open = preferences.getBoolean("first_open_0.5.0", true);
		Boolean login_status = preferences.getBoolean("login_status", true);

		// �Ƿ��һ�δ�
		if (first_open) {
			// ��ǰ�汾Ӧ�õ�һ�δ�
			// TODO ��ת��ӭҳ��
		} else {
			// �ǵ�һ�δ�
		}

		// ��¼״̬
		if (login_status) {
			// �ѵ�¼״̬

			App myApp = ((App) getApplicationContext());
			myApp.login();

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {

					Intent mainIntent = new Intent(Splash.this,
							MainActivity.class);
					Splash.this.startActivity(mainIntent);
					Splash.this.finish();

				}

			}, SPLASH_DISPLAY_LENGHT);

		} else {
			// δ��¼״̬
			// TODO ��ת�����ֻ���ҳ��
		}

	}

}
