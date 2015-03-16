package jeese.helpme.application;

import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.PreferencesCookieStore;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import cn.jpush.android.api.JPushInterface;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;

public class App extends Application {

	private String sessionId = null;
	private PreferencesCookieStore preferencesCookieStore;
	private Handler handler = new Handler();
	
	private int ONLINE = 1;
	private int OFFLINE = 2;
	private int NETWORK_ERROR = 3;
	public int STATE = 0;

	public String getSessionId() {
		return sessionId;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		preferencesCookieStore = new PreferencesCookieStore(this);

		// JPush的初始化
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		// 初始化。
		RongIM.init(this);

		// RongIM.init(this, "k51hidwq1fspb", R.drawable.ic_launcher);

		// 从您的应用服务器请求，以获取 Token。在本示例中我们直接在下面 hardcode 给 token 赋值。
		// String token = getTokenFromAppServer();

		// 此处直接 hardcode 给 token 赋值，请替换为您自己的 Token。

		// 001
		// 小狗
		// String token =
		// "gmeGPMbpkNpRVFqmRk49jf9JXx3k/zTlRSJ1G8Af9CI3U4e9lF3jnvQKYp2ecC/dBVc7XyuL9skbqunN048Scg==";

		// 002
		// 小牛
		// String token =
		// "yg9B/Wh0bZm9Wab05Ia5Af9JXx3k/zTlRSJ1G8Af9CI3U4e9lF3jnlvB84WBZ50UdgkvtOX7ULs5ATrHbF6P1g==";

		// 003
		// 小蓝孩
		String token = "wjfvk2IpBXF+sB/wrTU7icgXtCAuovQvahXBtnZqSZmQjRdyLfCC8elmntSLtDK7+ykyqIE2k4IxxMHaZ6bvcw==";

		// 连接融云服务器。
		try {
			RongIM.connect(token, new RongIMClient.ConnectCallback() {

				@Override
				public void onSuccess(String s) {
					// 此处处理连接成功。
					Log.d("Connect:", "Login successfully.");
				}

				@Override
				public void onError(ErrorCode errorCode) {
					// 此处处理连接错误。
					Log.d("Connect:", "Login failed.");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void login() {
		SharedPreferences preferences = getSharedPreferences("e_help",
				Context.MODE_PRIVATE);
		sessionId = preferences.getString("sessionId", null);
		BasicClientCookie cookie = new BasicClientCookie("SESSION", sessionId);
		cookie.setDomain("120.24.208.130");
		cookie.setPath("/api/auth");
		preferencesCookieStore.addCookie(cookie);

		String phone = preferences.getString("cellPhone", null);
		String password = preferences.getString("password", null);

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("cellPhone", "110110_11");
			jsonObject.put("password", "helloworld");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RequestParams params = new RequestParams();
		params.addBodyParameter("fields", jsonObject.toString());

		HttpUtils http = new HttpUtils();
		http.configCookieStore(preferencesCookieStore);
		http.send(HttpRequest.HttpMethod.POST,
				"http://120.24.208.130:3333/api/auth/login", params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						System.out.println("连接服务器失败");
						STATE = NETWORK_ERROR;
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {

						try {
							JSONObject replyObject = new JSONObject(arg0.result);
							String state = replyObject.getString("state");
							if (state.equals("true")) {
								JSONObject sessionObject = replyObject
										.getJSONObject("session");
								sessionId = sessionObject
										.getString("sessionId");
								SharedPreferences preferences = getSharedPreferences(
										"e_help", Context.MODE_PRIVATE);
								Editor editor = preferences.edit();
								editor.putString("sessionId", sessionId);
								editor.putBoolean("login_status", true);
								editor.commit();

								System.out.println("登录成功 session：" + sessionId);
								STATE = ONLINE;

								handler.postDelayed(new Runnable() {

									@Override
									public void run() {
										login();
										handler.postDelayed(this, 3000000);
									}
								}, 3000000);
							}else{
								System.out.println("登陆失败");
								STATE = OFFLINE;
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				});
	}

}
