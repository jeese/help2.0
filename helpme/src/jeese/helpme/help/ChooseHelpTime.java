package jeese.helpme.help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jeese.helpme.R;
import jeese.helpme.view.SildingFinishLayout;
import jeese.helpme.view.SildingFinishLayout.OnSildingFinishListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.ListView;

public class ChooseHelpTime extends ActionBarActivity {
	private Toolbar mToolbar;
	private SildingFinishLayout mSildingFinishLayout;
	private ListView chooseList;
	private List<Map<String, Object>> data=new ArrayList<Map<String, Object>>();
	private ImageView chooseImg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_help_time);
		
		//设置toolbar
		setToolBar();

		chooseList=(ListView) findViewById(R.id.choose_time_list);
		
		getData();
		SimpleAdapter listAdapter = new SimpleAdapter(this, data, R.layout.choose_help_time_item, 
				new String[]{"name", "img"}, new int[]{R.id.choose_text,R.id.choose_image});
		
		//设置listview的适配器和监听函数
		chooseList.setAdapter(listAdapter);
		chooseList.setOnItemClickListener(listListener);
		mSildingFinishLayout = (SildingFinishLayout) findViewById(R.id.sildingFinishLayout);
		mSildingFinishLayout
				.setOnSildingFinishListener(new OnSildingFinishListener() {

					@Override
					public void onSildingFinish() {
						finish();
					}
				});
		// touchView要设置到ListView上面
		mSildingFinishLayout.setTouchView(chooseList);
	}
	/**
	 * 设置toolbar
	 */
	private void setToolBar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// toolbar.setLogo(R.drawable.ic_launcher);
		mToolbar.setTitle("有效期");// 标题的文字需在setSupportActionBar之前，不然会无效
		// toolbar.setSubtitle("副标题");
		setSupportActionBar(mToolbar);//Toolbar即能取代原本的 actionbar 了
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		 //菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过下面的两个回调方法来处理 
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
	/**
	 * 获得数据
	 */
	public void getData(){
		Map<String,Object> map=new HashMap<String, Object>();
		
		map.put("img", R.drawable.orange);
		map.put("name", "2小时");
		data.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.orange);
		map.put("name", "6小时");
		data.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.orange);
		map.put("name", "12小时");
		data.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.orange);
		map.put("name","1天");
		data.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.orange);
		map.put("name","2天");
		data.add(map);
	}
	
	
	/**
	 * 设置listview中item的点击事件监听
	 */
	OnItemClickListener listListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			chooseImg=(ImageView) arg1.findViewById(R.id.choose_image);
			chooseImg.setImageDrawable(getResources().getDrawable(R.drawable.deep_orange));
			//调用setResult函数，回传数据
			Bundle bundle = new Bundle();
			bundle.putString("time", data.get(position).get("name").toString());
			Intent intent = new Intent(ChooseHelpTime.this, SendQuestionActivity.class);
			intent.putExtras(bundle);
			setResult(android.app.Activity.RESULT_OK, intent);
			finish();
		}
	};
}

