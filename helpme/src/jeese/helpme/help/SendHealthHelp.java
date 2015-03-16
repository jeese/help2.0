package jeese.helpme.help;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import jeese.helpme.R;
import jeese.helpme.activity.MainActivity;
import jeese.helpme.auth.StartActivity;
import jeese.helpme.view.SildingFinishLayout;
import jeese.helpme.view.SildingFinishLayout.OnSildingFinishListener;
import jeese.helpme.view.materialedittext.MaterialEditText;
import jeese.helpme.widget.ArrayWheelAdapter;
import jeese.helpme.widget.WheelView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SendHealthHelp extends ActionBarActivity{
	
	private Toolbar mToolbar;
	private SildingFinishLayout mSildingFinishLayout;
	private MaterialEditText content_edit;
	private String content;
	private Button finishBtn;
	private Button chooseBtn;
	
	//��ʼ��ʱ���ֻἷѹ���ں�������������ӿո񼴿�
	private String[] names = {" ���ಡ  "," �Գ�Ѫ "," �Թ��� "," �ж� "," ��ʶ���� "," �ļ����� "," �Ľ�ʹ "};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_health_help);
		
		init();
	}
	
	private void init(){
		setToolBar();
		
		content_edit=(MaterialEditText) findViewById(R.id.content_edit);
		finishBtn=(Button) findViewById(R.id.finish_button);
		chooseBtn=(Button) findViewById(R.id.choose_button);
		
		chooseBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final AlertDialog dialog = new AlertDialog.Builder(SendHealthHelp.this).create();
				dialog.setTitle("ѡ��ͻ����������");
				final WheelView catalogWheel = new WheelView(SendHealthHelp.this);
				dialog.setButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String str = catalogWheel.getTextItem(catalogWheel.getCurrentItem());
						content_edit.setText(str);
						dialog.dismiss();
						// ʵ����ui��ˢ��
					}
				});
				dialog.setButton2("ȡ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				catalogWheel.setVisibleItems(5);
				catalogWheel.setCyclic(true);
				catalogWheel.setAdapter(new ArrayWheelAdapter<String>(names));
				dialog.setView(catalogWheel);
				dialog.show();
			}
		});
		
		finishBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//���ʷ��������������Ϣ
				sendHealthHelp();
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
		mToolbar.setTitle("��д���������Ϣ");// �������������setSupportActionBar֮ǰ����Ȼ����Ч
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
	
	private void sendHealthHelp(){
		content = content_edit.getText().toString();

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("helpContent", content);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RequestParams params = new RequestParams();
		params.addBodyParameter("fields", jsonObject.toString());

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST,
				"http://120.24.208.130:3333/api/auth/register", params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						System.out.println("���ʷ�����ʧ��");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {

						try {
							JSONObject replyObject = new JSONObject(arg0.result);
							String state = replyObject.getString("state");
							if(state.equals("true")){
								System.out.println("����ɹ�");
								
								// ҳ����ת����ҳ��
								Intent intent = new Intent(SendHealthHelp.this,
										MainActivity.class);
								SendHealthHelp.this.startActivity(intent);
								StartActivity.StartActivity.finish();
								finish();
								
							}else{
								System.out.println("����ʧ��");
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

		});
}

}