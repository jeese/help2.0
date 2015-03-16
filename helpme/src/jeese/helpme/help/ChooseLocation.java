package jeese.helpme.help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jeese.helpme.R;
import jeese.helpme.service.LocationService;
import jeese.helpme.view.SildingFinishLayout;
import jeese.helpme.view.SildingFinishLayout.OnSildingFinishListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.ListView;

public class ChooseLocation extends ActionBarActivity {
	private Toolbar mToolbar;
	private SildingFinishLayout mSildingFinishLayout;
	private ListView chooseList;
	private List<Map<String, Object>> data=new ArrayList<Map<String, Object>>();
	private ImageView chooseImg;
	private Button cancelChooseBtn;
	private String city;
	private String address;
	private static final int REQUESTCODE = 123;
	private SimpleAdapter listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_location);
		
		city = LocationService.getCityCode();
		address = LocationService.getAddress();
		System.out.println("��ַ�ǣ�"+address);	
		
		init();
	}
	
	public void init(){
		//����toolbar
		setToolBar();
		
		chooseList = (ListView) findViewById(R.id.choose_location_list);
		getData();
		
		listAdapter = new SimpleAdapter(this, data, R.layout.item_choose_location,
				new String[]{"name","img"}, new int[]{R.id.choose_text,R.id.choose_image});
		mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
		mSildingFinishLayout
				.setOnSildingFinishListener(new OnSildingFinishListener() {

					@Override
					public void onSildingFinish() {
						finish();
					}
				});
		// touchViewҪ���õ�ListView����
		mSildingFinishLayout.setTouchView(chooseList);

		//����listview���������ͼ�������
		chooseList.setAdapter(listAdapter);
		chooseList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
//				chooseImg=(ImageView) arg1.findViewById(R.id.choose_image);
//				chooseImg.setImageDrawable(getResources().getDrawable(R.drawable.choose_checkmark));
				
				if(data.get(position).get("name").toString().equals("�޸�λ��")){
					Intent it = new Intent(ChooseLocation.this, AddNewLocation.class);
					startActivityForResult(it, REQUESTCODE);
				}
				else{
				//����setResult�������ش�����
				Bundle bundle = new Bundle();
				bundle.putString("location", data.get(position).get("name").toString());
				Intent intent = new Intent(ChooseLocation.this, SendLifeHelpActivity.class);
				intent.putExtras(bundle);
				setResult(android.app.Activity.RESULT_OK, intent);
				finish();
				}
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(requestCode == REQUESTCODE){
			if(resultCode == Activity.RESULT_OK){
				String result=intent.getExtras().getString("newAddress");
				 Map<String,Object> map=new HashMap<String, Object>();
				 map.put("name", result);
				 data.set(0, map);
				 listAdapter.notifyDataSetChanged();
			}
		}
	};
	/**
	 * ����toolbar
	 */
	private void setToolBar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// toolbar.setLogo(R.drawable.ic_launcher);
		mToolbar.setTitle("λ����Ϣ");// �������������setSupportActionBar֮ǰ����Ȼ����Ч
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
	
	//�������
	public void getData(){
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = new HashMap<String, Object>();
		map.put("name", address);
		data.add(map);
		
		map = new HashMap<String, Object>();
		map.put("name","�޸�λ��");
		data.add(map);
		}
}