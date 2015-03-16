package jeese.helpme.me;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jeese.helpme.R;
import jeese.helpme.view.CircleImageView;
import jeese.helpme.view.SildingFinishLayout;
import jeese.helpme.view.SildingFinishLayout.OnSildingFinishListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MeProfile extends ActionBarActivity {
	private Toolbar mToolbar;
	private SildingFinishLayout mSildingFinishLayout;
	private CircleImageView mPortrait;
	private Uri photoUri;
	private String path = "";

	private static final int TAKE_PICTURE = 0;// ���յ�������
	private static final int RESULT_LOAD_IMAGE = 1;// �����ѡȡ��������
	private static final int CUT_PHOTO_REQUEST_CODE = 2;// �ü�ͼƬ��������
	private static final int EDIT_NAME = 3;
	private static final int EDIT_PHONE = 4;
	private static final int EDIT_ID = 5;
	private static final int EDIT_SEX = 6;
	private static final int EDIT_AGE = 7;
	private static final int EDIT_ADDRESS = 8;
	private static final int EDIT_ILLNESS = 9;
	private static final int EDIT_EXPERIENCE = 10;

	private String drr = null;// ���ѹ�����ͼƬ��·��
	private Bitmap head = null;// ͷ��Bitmap
	private Bitmap circleHead = null;// �������������ʾ��Բ�Ǿ���ͼƬ
	private float dp;

	private ListView mProfileList;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private MyProfileAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_profile);

		// ����toolbar
		setToolBar();
		init();
	}

	public void init() {
		dp = getResources().getDimension(R.dimen.dp);

		mPortrait = (CircleImageView) findViewById(R.id.image_portrait);
		mPortrait.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new PopupWindows(MeProfile.this, mPortrait);
			}
		});

		getData();
		listAdapter = new MyProfileAdapter(this);
		mProfileList = (ListView) findViewById(R.id.listView_profile);
		mProfileList.setAdapter(listAdapter);
		mProfileList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				Toast.makeText(getApplicationContext(),
						(String) mData.get(position).get("name"),
						Toast.LENGTH_LONG).show();
				final String str = (String) mData.get(position).get("name");
				final String value = (String) mData.get(position).get("value");
				if (str.equals("����")) {
					Intent it = new Intent(MeProfile.this,
							EditNameActivity.class);
					it.putExtra("value", value);
					startActivityForResult(it, EDIT_NAME);
				} else if (str.equals("�ֻ���")) {
					Intent it = new Intent(MeProfile.this,
							EditPhoneActivity.class);
					it.putExtra("value", value);
					startActivityForResult(it, EDIT_PHONE);
				} else if (str.equals("���֤��")) {
					Intent it = new Intent(MeProfile.this,
							EditIDNumberActivity.class);
					it.putExtra("value", value);
					startActivityForResult(it, EDIT_ID);
				} else if (str.equals("�Ա�")) {
					Intent it = new Intent(MeProfile.this,
							EditSexActivity.class);
					it.putExtra("value", value);
					startActivityForResult(it, EDIT_SEX);
				} else if (str.equals("����")) {
					Intent it = new Intent(MeProfile.this,
							EditAgeActivity.class);
					it.putExtra("value", value);
					startActivityForResult(it, EDIT_AGE);
				} else if (str.equals("��ַ")) {
					Intent it = new Intent(MeProfile.this,
							EditAddressActivity.class);
					startActivityForResult(it, EDIT_ADDRESS);
				} else if (str.equals("��ʷ")) {
//					Intent it = new Intent(MeProfile.this,
//							EditIllActivity.class);
//					startActivityForResult(it, EDIT_ILLNESS);
//				} else if (str.equals("����")) {
//					Intent it = new Intent(MeProfile.this,
//							EditExperActivity.class);
//					startActivityForResult(it, EDIT_EXPERIENCE);
				}
			}

			// if(str.equals("����")){
			// Builder b = new AlertDialog.Builder(MeProfile.this);
			// b.setTitle("�༭����");
			//
			// LinearLayout l = (LinearLayout) getLayoutInflater().inflate(
			// R.layout.edit_name_dialog, null);
			// final EditText editName = (EditText)
			// l.findViewById(R.id.edit_name_dialog);
			// editName.setText(mData.get(position).get("value").toString());
			//
			// b.setView(l);
			//
			// b.setPositiveButton("����", new DialogInterface.OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface arg0, int arg1) {
			// ListView lstres = (ListView)findViewById(R.id.listView_profile);
			// LinearLayout ll = (LinearLayout)lstres.getChildAt(0);//
			// ���Item���֣�i����item��λ��
			// TextView text = (TextView) ll.findViewById(R.id.value_profile);//
			// ���Ӽ��л�ÿؼ�
			// text.setText(editName.getText().toString());
			//
			// Map<String,Object> map=new HashMap<String, Object>();
			// map.put("name", "����");
			// map.put("value", editName.getText().toString());
			// map.put("img",R.drawable.android_list_idex);
			// mData.set(0, map);
			// listAdapter.notifyDataSetChanged();
			// }
			// }).setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
			// {
			//
			// @Override
			// public void onClick(DialogInterface arg0, int arg1) {
			// arg0.cancel();
			// }
			// }).create().show();
			// }
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
		mSildingFinishLayout.setTouchView(mProfileList);
	}

	/**
	 * Ϊ�˻�ȡCamera���ص�ͼƬ��Ϣ����д�÷�����
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case EDIT_NAME:
			if (resultCode == RESULT_OK) {
				String result = data.getExtras().getString("username")
						.toString();
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
/*				ListView lstres = (ListView) findViewById(R.id.listView_profile);
				LinearLayout ll = (LinearLayout) lstres.getChildAt(0);// ���Item���֣�i����item��λ��
				TextView text = (TextView) ll.findViewById(R.id.value_profile);// ���Ӽ��л�ÿؼ�

				text.setText(result);*/

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", "����");
				map.put("value", result);
				map.put("img", R.drawable.android_list_idex);
				mData.set(0, map);
				listAdapter.notifyDataSetChanged();
			}
			break;
		case EDIT_PHONE:
			if (resultCode == RESULT_OK) {
				String result = data.getExtras().getString("phone")
						.toString();
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", "�ֻ���");
				map.put("value", result);
				map.put("img", R.drawable.android_list_idex);
				mData.set(1, map);
				listAdapter.notifyDataSetChanged();
			}
			break;
		case EDIT_ID:
			if (resultCode == RESULT_OK) {
				String result = data.getExtras().getString("idnumber")
						.toString();
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", "���֤��");
				map.put("value", result);
				map.put("img", R.drawable.android_list_idex);
				mData.set(2, map);
				listAdapter.notifyDataSetChanged();
			}
			break;
		case EDIT_SEX:
			if (resultCode == RESULT_OK) {
				String result = data.getExtras().getString("sex")
						.toString();
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", "�Ա�");
				map.put("value", result);
				map.put("img", R.drawable.android_list_idex);
				mData.set(3, map);
				listAdapter.notifyDataSetChanged();
			}
			break;
		case EDIT_AGE:
			if (resultCode == RESULT_OK) {
				String result = data.getExtras().getString("age")
						.toString();
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", "����");
				map.put("value", result);
				map.put("img", R.drawable.android_list_idex);
				mData.set(4, map);
				listAdapter.notifyDataSetChanged();
			}
			break;
		case EDIT_ADDRESS:
			if (resultCode == RESULT_OK) {
				String result = data.getExtras().getString("phone")
						.toString();
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", "�ֻ���");
				map.put("value", result);
				map.put("img", R.drawable.android_list_idex);
				mData.set(1, map);
				listAdapter.notifyDataSetChanged();
			}
			break;
		case EDIT_ILLNESS:
			if (resultCode == RESULT_OK) {
				String result = data.getExtras().getString("phone")
						.toString();
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", "�ֻ���");
				map.put("value", result);
				map.put("img", R.drawable.android_list_idex);
				mData.set(1, map);
				listAdapter.notifyDataSetChanged();
			}
			break;
		case EDIT_EXPERIENCE:
			if (resultCode == RESULT_OK) {
				String result = data.getExtras().getString("phone")
						.toString();
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", "�ֻ���");
				map.put("value", result);
				map.put("img", R.drawable.android_list_idex);
				mData.set(1, map);
				listAdapter.notifyDataSetChanged();
			}
			break;
		case TAKE_PICTURE:
			if (resultCode == RESULT_OK) {
				startPhotoZoom(photoUri);
			}
			break;
		case CUT_PHOTO_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				byte[] bitmapByte=data.getByteArrayExtra("bitmap");
				Bitmap bimp=BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
//				// ����ת��ΪBitmapͼƬ
//				head = Bimp.getLoacalBitmap(drr);
				// Բ��ͷ��
//				circleHead = Bimp.createFramedPhoto(480, 480, head,
//						(int) (dp * 1.6f));
				// BitmapDrawable bd= new BitmapDrawable(getResources(),
				// circleHead); //��ΪBtimapDrawable��Drawable�����࣬����ֱ��ʹ��bd���󼴿ɡ�
				mPortrait.setImageBitmap(bimp);
			}
			break;
		case RESULT_LOAD_IMAGE:
			if (resultCode == RESULT_OK && data != null) {// ��᷵��
				photoUri = data.getData();
				if (photoUri != null) {
					startPhotoZoom(photoUri);
				}
			}
			break;
		}
	}

	private void startPhotoZoom(Uri uri) {
		// TODO Auto-generated method stub
		Intent it = new Intent(MeProfile.this,StartPhotoZoom.class);
		it.setData(uri);
		it.putExtra("url", uri);
		startActivityForResult(it, CUT_PHOTO_REQUEST_CODE);
	}

	/**
	 * ���cameraͼ������浯����ѡ���
	 * */
	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {
			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// ���
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, RESULT_LOAD_IMAGE);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});
		}
	}

	/**
	 * �����ϴ�
	 */
	public void photo() {
		try {
			// image�Ļ�ȡ����ͨ����Android�Դ���CameraӦ�������
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);

			// ���������Ҫ��ȡ������SD��д�룬��ʱ�ͱ�����Ҫ�ж�һ��SD����״̬�������п��ܳ���
			String sdcardState = Environment.getExternalStorageState();
			String sdcardPathDir = android.os.Environment
					.getExternalStorageDirectory().getPath() + "/tempImage/";
			File file = null;

			// Environment.MEDIA_MOUNTED��ʾsd����������
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				/* �����ָ��image�洢��SDCard�ϵ�tempImage�ļ������� */
				File fileDir = new File(sdcardPathDir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();// ���ļ����Ѿ����ھͲ���Ҫ�½��ļ�����
				}
				/* ����һ���ļ� */
				file = new File(sdcardPathDir + System.currentTimeMillis()
						+ ".JPEG");
			}
			if (file != null) {
				path = file.getPath();// getpath �õ���д��·�������ݵ�ǰĿ¼λ�ÿ�����д·�����õ����·����
				// uri�������Ǹ������URI�ҵ�ĳ����Դ�ļ���������ʽ�磺 file:///sdcard/temp.jpg
				photoUri = Uri.fromFile(file);
				/*
				 * ����Camara���ص�������ͼ�����ǿ��Դ��ݸ���һ������EXTRA_OUTPUT,
				 * ������Camera��ȡ����ͼƬ�洢��һ��ָ����URIλ�ô��� �����ͽ��ļ��Ĵ洢��ʽ��uriָ������CameraӦ����
				 */
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				/*
				 * ����������Ҫ������Camera�󣬿��Է���Camera��ȡ����ͼƬ��
				 * ���ԣ�����ʹ��startActivityForResult������Camera��֮����ڻ�ȡ��ͼƬ���вü�
				 */
				startActivityForResult(openCameraIntent, TAKE_PICTURE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getData() {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("name", "����");
		map.put("value", "ʯ����");
		map.put("img", R.drawable.android_list_idex);
		mData.add(map);

		map = new HashMap<String, Object>();

		map.put("name", "�ֻ���");
		map.put("value", "135****4260");
		map.put("img", R.drawable.android_list_idex);
		mData.add(map);

		map = new HashMap<String, Object>();

		map.put("name", "���֤��");
		map.put("value", "130430********0169");
		map.put("img", R.drawable.android_list_idex);
		mData.add(map);

		map = new HashMap<String, Object>();

		map.put("name", "�Ա�");
		map.put("value", "Ů");
		map.put("img", R.drawable.android_list_idex);
		mData.add(map);

		map = new HashMap<String, Object>();

		map.put("name", "����");
		map.put("value", "23");
		map.put("img", R.drawable.android_list_idex);
		mData.add(map);

		map = new HashMap<String, Object>();

		map.put("name", "��ַ");
		map.put("value", "");
		map.put("img", R.drawable.android_list_idex);
		mData.add(map);

		map = new HashMap<String, Object>();

		map.put("name", "��ʷ");
		map.put("value", "");
		map.put("img", R.drawable.android_list_idex);
		mData.add(map);

		map = new HashMap<String, Object>();

		map.put("name", "����");
		map.put("value", "");
		map.put("img", R.drawable.android_list_idex);
		mData.add(map);
	}

	/**
	 * ListView��adapter
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyProfileAdapter extends BaseAdapter {
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

		public MyProfileAdapter(Context context) {
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
				convertView = mInflater.inflate(R.layout.item_myprofile,
						parent, false);// ������һ��View֮����ЩconvertView�ͻ����Recyclor����Recyclor�Ĵ�С������Ļ����ʾ����Ķ��ٶ�����
				holder = new ViewHolder();
				// ��ȡ�ؼ�����
				holder.name = (TextView) convertView
						.findViewById(R.id.name_profile);
				holder.value = (TextView) convertView
						.findViewById(R.id.value_profile);
				holder.img = (ImageView) convertView
						.findViewById(R.id.image_arrow);
				// ���ÿؼ�����convertView,��View�а�һ��tag����Ϊ���ӵ�����
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.img.setBackgroundResource((Integer) mData.get(position).get(
					"img"));
			holder.name.setText((String) mData.get(position).get("name"));
			holder.value.setText((String) mData.get(position).get("value"));

			holder.value.setOnClickListener(new View.OnClickListener() {

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
			public TextView value;
			public ImageView img;
		}
	}
	

	private void setToolBar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// toolbar.setLogo(R.drawable.ic_launcher);
		mToolbar.setTitle("������Ϣ");// �������������setSupportActionBar֮ǰ����Ȼ����Ч
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
