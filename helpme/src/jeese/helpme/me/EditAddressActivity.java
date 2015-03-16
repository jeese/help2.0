package jeese.helpme.me;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jeese.helpme.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EditAddressActivity extends ActionBarActivity{
	private Toolbar mToolbar;
	private Button addAddress;
	private ListView listView;
	private MyAddressAdapter adapter;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private final static int ADD_ADDRESS=0;
	private final static int CHANGE_ADDRESS=1;
	private static int pos=-1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_address);
		getData();
		setToolBar();
		init();
		
	}
	public void init(){
		listView=(ListView) findViewById(R.id.listView_show_address);
		
		adapter=new MyAddressAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				final String value = (String) mData.get(position).get("item_name");
				Intent it = new Intent(EditAddressActivity.this, ChangeAddress.class);
				it.putExtra("id", position);
				it.putExtra("value", value);
				pos = position;
				startActivityForResult(it, CHANGE_ADDRESS);
			}
		});
		
		addAddress=(Button) findViewById(R.id.button_add_new_address);
		addAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(EditAddressActivity.this, AddAddress.class);
				startActivityForResult(it, ADD_ADDRESS);
			}
		});
	}
	private void getData() {
		Map<String, Object> map = new HashMap<String, Object>();
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case ADD_ADDRESS:
			if (resultCode == RESULT_OK) {
				String result = data.getExtras().getString("address")
						.toString();
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("item_name", result);
				mData.add(map);
				adapter.notifyDataSetChanged();
			}
			break;
		case CHANGE_ADDRESS:
			if(resultCode == RESULT_OK){
				String result = data.getExtras().getString("address")
						.toString();
				int pos = data.getExtras().getInt("id");
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("item_name", result);
				if(pos != -1){
					mData.set(pos, map);
					adapter.notifyDataSetChanged();
				}
			}
			break;
		}
	}
	/**
	 * ListView的adapter
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyAddressAdapter extends BaseAdapter {
		/*
		 * listView在开始绘制的时候，系统首先调用getCount（）函数，根据他的返回值得到listView的长度
		 * （这也是为什么在开始的第一张图特别的标出列表长度
		 * ），然后根据这个长度，调用getView（）逐一绘制每一行。如果你的getCount（）返回值是0的话，列表将不显示同样return
		 * 1，就只显示一行。
		 */
		/*
		 * 系统显示列表时，首先实例化一个适配器（这里将实例化自定义的适配器）。当手动完成适配时，必须手动映射数据，这需要重写getView（）方法。
		 * 系统在绘制列表的每一行的时候将调用此方法
		 * 。getView()有三个参数，position表示将显示的是第几行，covertView是从布局文件中inflate来的布局
		 * 。我们用LayoutInflater的方法将定义好的vlist2
		 * .xml文件提取成View实例用来显示。然后将xml文件中的各个组件实例化（
		 * 简单的findViewById()方法）。这样便可以将数据对应到各个组件上了
		 * 。但是按钮为了响应点击事件，需要为它添加点击监听器，这样就能捕获点击事件。至此一个自定义的listView就完成了，
		 */
		/*
		 * 现在让我们回过头从新审视这个过程。系统要绘制ListView了，他首先获得要绘制的这个列表的长度，然后开始绘制第一行，怎么绘制呢？
		 * 调用getView
		 * ()函数。在这个函数里面首先获得一个View（实际上是一个ViewGroup），然后再实例并设置各个组件，显示之。好了，绘制完这一行了。那
		 * 再绘制下一行，直到绘完为止。
		 */
		private LayoutInflater mInflater;

		public MyAddressAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// 自定义视图
			ViewHolder holder = null;
			if (convertView == null) {// 一开始，显示在屏幕内的item都没有进入Recyclor，所以，convertView作为老的View是空的。
				convertView = mInflater.inflate(R.layout.item_list_address,
						parent, false);// 创建了一个View之后，这些convertView就会进入Recyclor，而Recyclor的大小依据屏幕上显示对象的多少而定。
				holder = new ViewHolder();
				// 获取控件对象
				holder.name = (TextView) convertView
						.findViewById(R.id.item_name);
				// 设置控件集到convertView,在View中绑定一个tag，作为附加的数据
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText((String) mData.get(position).get("item_name"));

			holder.name.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(), "click",
							Toast.LENGTH_LONG).show();
				}
			});

			return convertView;
		}

		public final class ViewHolder {
			public TextView name;
		}
	}
	private void setToolBar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// toolbar.setLogo(R.drawable.ic_launcher);
		mToolbar.setTitle("我的地址");// 标题的文字需在setSupportActionBar之前，不然会无效
		// toolbar.setSubtitle("副标题");
		setSupportActionBar(mToolbar);// Toolbar即能取代原本的 actionbar 了
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过下面的两个回调方法来处理
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
