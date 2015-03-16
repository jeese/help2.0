package jeese.helpme.help;

import jeese.helpme.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewLocation extends ActionBarActivity {
	private EditText newAddress;
	private Button confirmBtn;
	private String addr;
	private Toolbar mToolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new_location);
		
		//����toolbar
		setToolBar();
		
		newAddress = (EditText) findViewById(R.id.edit_new_address);
		confirmBtn=(Button) findViewById(R.id.confirm_button);

		confirmBtn.setOnClickListener(new OnClickListener() {

			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String address = newAddress.getText().toString();
				if(address.equals("")){
					Toast.makeText(getApplicationContext(), address, Toast.LENGTH_LONG).show();
					return;
				}
				else{
					Intent it = new Intent(AddNewLocation.this,ChooseLocation.class);
					Bundle b = new Bundle();
					b.putString("newAddress", address);
					it.putExtras(b);
					setResult(android.app.Activity.RESULT_OK, it);
					finish();
				}
			}
		});	
}
	/**
	 * ����toolbar
	 */
	private void setToolBar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// toolbar.setLogo(R.drawable.ic_launcher);
		mToolbar.setTitle("�޸�λ��");// �������������setSupportActionBar֮ǰ����Ȼ����Ч
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
