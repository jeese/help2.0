package jeese.helpme.me;

import java.util.Timer;
import java.util.TimerTask;

import jeese.helpme.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditNameActivity extends ActionBarActivity{
	private Toolbar mToolbar;
	private EditText editName;
	private Button finishChangeBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_name);
		
		setToolBar();
		init();
	}
	
	public void init(){
		editName=(EditText) findViewById(R.id.edit_name);
		Intent it = getIntent();
		String text = it.getStringExtra("value");
		editName.setText(text);
		// ����Ҫ��ָ������������󽹵㡣Ȼ����������������������̡�
		editName.setFocusable(true);
		editName.setFocusableInTouchMode(true);
		editName.requestFocus();
		editName.setSelection(text.length());//set cursor to the end  
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) editName
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(editName, 0);
			}
		}, 998);
		finishChangeBtn=(Button) findViewById(R.id.finish_change_button);
		finishChangeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				//����setResult�������ش�����
				String str = editName.getText().toString();
				//Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
				Bundle b =new Bundle();
				b.putString("username", str);
				Intent it=new Intent(EditNameActivity.this, MeProfile.class);
				it.putExtras(b);
				setResult(android.app.Activity.RESULT_OK, it);
				finish();
			}
		});
	}
	
	private void setToolBar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// toolbar.setLogo(R.drawable.ic_launcher);
		mToolbar.setTitle("����");// �������������setSupportActionBar֮ǰ����Ȼ����Ч
		// toolbar.setSubtitle("������");
		setSupportActionBar(mToolbar);//Toolbar����ȡ��ԭ���� actionbar ��
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		 //�˵��ļ���������toolbar�����ã�Ҳ������ActionBar������ͨ������������ص����������� 
		mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case android.R.id.home:
					finish();
					return true;
				default:
					break;
				}
				return true;
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
