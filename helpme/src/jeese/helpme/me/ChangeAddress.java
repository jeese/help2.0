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

public class ChangeAddress extends ActionBarActivity{
	private Toolbar mToolbar;
	private EditText textAddress;
	private Button confirmBtn;
	private int id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_address);
		
		setToolBar();
		init();
	}
	
	public void init(){	
		
		textAddress=(EditText) findViewById(R.id.text_add_address);
		confirmBtn=(Button) findViewById(R.id.finish_change_button);
		Intent it = getIntent();
		String text = it.getStringExtra("value");
		textAddress.setText(text);
		id = it.getIntExtra("id", -1);
		textAddress.setText(text);
		
		// ����Ҫ��ָ������������󽹵㡣Ȼ����������������������̡�
		textAddress.setFocusable(true);
		textAddress.setFocusableInTouchMode(true);
		textAddress.requestFocus();
		textAddress.setSelection(text.length());//set cursor to the end
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager = (InputMethodManager) textAddress
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(textAddress, 0);
			}
		}, 998);
		
		confirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str = textAddress.getText().toString();
				if(str.equals("")){
					Toast.makeText(getApplicationContext(), "��ַ����Ϊ��", Toast.LENGTH_SHORT).show();
				}else{
					Bundle b=new Bundle();
					b.putString("address", str);
					b.putInt("id", id);
					Intent intent = new Intent();
					intent.putExtras(b);
					setResult(android.app.Activity.RESULT_OK, intent);
					finish();
				}
			}
		});
	}
	
	private void setToolBar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// toolbar.setLogo(R.drawable.ic_launcher);
		mToolbar.setTitle("�޸ĵ�ַ");// �������������setSupportActionBar֮ǰ����Ȼ����Ч
		// toolbar.setSubtitle("������");
		setSupportActionBar(mToolbar);// Toolbar����ȡ��ԭ���� actionbar ��
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// �˵��ļ���������toolbar�����ã�Ҳ������ActionBar������ͨ������������ص�����������
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
