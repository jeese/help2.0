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
	 * ListView��adapter
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyAddressAdapter extends BaseAdapter {
		/*
		 * listView�ڿ�ʼ���Ƶ�ʱ��ϵͳ���ȵ���getCount�����������������ķ���ֵ�õ�listView�ĳ���
		 * ����Ҳ��Ϊʲô�ڿ�ʼ�ĵ�һ��ͼ�ر�ı���б���
		 * ����Ȼ�����������ȣ�����getView������һ����ÿһ�С�������getCount��������ֵ��0�Ļ����б�����ʾͬ��return
		 * 1����ֻ��ʾһ�С�
		 */
		/*
		 * ϵͳ��ʾ�б�ʱ������ʵ����һ�������������ｫʵ�����Զ�����������������ֶ��������ʱ�������ֶ�ӳ�����ݣ�����Ҫ��дgetView����������
		 * ϵͳ�ڻ����б��ÿһ�е�ʱ�򽫵��ô˷���
		 * ��getView()������������position��ʾ����ʾ���ǵڼ��У�covertView�ǴӲ����ļ���inflate���Ĳ���
		 * ��������LayoutInflater�ķ���������õ�vlist2
		 * .xml�ļ���ȡ��Viewʵ��������ʾ��Ȼ��xml�ļ��еĸ������ʵ������
		 * �򵥵�findViewById()����������������Խ����ݶ�Ӧ�������������
		 * �����ǰ�ťΪ����Ӧ����¼�����ҪΪ����ӵ�����������������ܲ������¼�������һ���Զ����listView������ˣ�
		 */
		/*
		 * ���������ǻع�ͷ��������������̡�ϵͳҪ����ListView�ˣ������Ȼ��Ҫ���Ƶ�����б�ĳ��ȣ�Ȼ��ʼ���Ƶ�һ�У���ô�����أ�
		 * ����getView
		 * ()����������������������Ȼ��һ��View��ʵ������һ��ViewGroup����Ȼ����ʵ�������ø����������ʾ֮�����ˣ���������һ���ˡ���
		 * �ٻ�����һ�У�ֱ������Ϊֹ��
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

			// �Զ�����ͼ
			ViewHolder holder = null;
			if (convertView == null) {// һ��ʼ����ʾ����Ļ�ڵ�item��û�н���Recyclor�����ԣ�convertView��Ϊ�ϵ�View�ǿյġ�
				convertView = mInflater.inflate(R.layout.item_list_address,
						parent, false);// ������һ��View֮����ЩconvertView�ͻ����Recyclor����Recyclor�Ĵ�С������Ļ����ʾ����Ķ��ٶ�����
				holder = new ViewHolder();
				// ��ȡ�ؼ�����
				holder.name = (TextView) convertView
						.findViewById(R.id.item_name);
				// ���ÿؼ�����convertView,��View�а�һ��tag����Ϊ���ӵ�����
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
		mToolbar.setTitle("�ҵĵ�ַ");// �������������setSupportActionBar֮ǰ����Ȼ����Ч
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
