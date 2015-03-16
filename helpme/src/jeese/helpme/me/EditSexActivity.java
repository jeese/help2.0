package jeese.helpme.me;

import jeese.helpme.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class EditSexActivity extends ActionBarActivity{
	private Toolbar mToolbar;
	private Button finishChangeBtn;
	private RadioGroup radioGroup;
	private RadioButton radioButton;
	private RadioButton btnFemale;
	private RadioButton btnMale;
	private String str = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_sex);
		
		setToolBar();
		init();
	}
	
	public void init(){
		Intent it = getIntent();
		final String text = it.getStringExtra("value").toString();
		if(text.equals("Ů")){
			btnFemale=(RadioButton) findViewById(R.id.radioButton_female);
			btnFemale.setChecked(true);
		}else if(text.equals("��")){
			btnMale=(RadioButton) findViewById(R.id.radioButton_male);
			btnMale.setChecked(true);
		}
		
		 //����ID�ҵ�RadioGroupʵ��
		radioGroup = (RadioGroup)this.findViewById(R.id.radioGroup);
		//��һ������������
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int radioButtonId = group.getCheckedRadioButtonId();
				radioButton = (RadioButton) findViewById(radioButtonId);
				str = radioButton.getText().toString();
			}
		});
		
		finishChangeBtn = (Button) findViewById(R.id.finish_change_button);
		finishChangeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����setResult�������ش�����
				Bundle b = new Bundle();
				if(str != null){
					b.putString("sex", str);
					Intent it = new Intent(EditSexActivity.this,MeProfile.class);
					it.putExtras(b);
					setResult(android.app.Activity.RESULT_OK, it);
					finish();
					}else{
						b.putString("sex", text);
						Intent it = new Intent(EditSexActivity.this,MeProfile.class);
						it.putExtras(b);
						setResult(android.app.Activity.RESULT_OK, it);
						finish();
					}
			}
		});
	}
	private void setToolBar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// toolbar.setLogo(R.drawable.ic_launcher);
		mToolbar.setTitle("�Ա�");// �������������setSupportActionBar֮ǰ����Ȼ����Ч
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
