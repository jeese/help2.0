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

	private static final int TAKE_PICTURE = 0;// 拍照的请求码
	private static final int RESULT_LOAD_IMAGE = 1;// 从相册选取的请求码
	private static final int CUT_PHOTO_REQUEST_CODE = 2;// 裁剪图片的请求码
	private static final int EDIT_NAME = 3;
	private static final int EDIT_PHONE = 4;
	private static final int EDIT_ID = 5;
	private static final int EDIT_SEX = 6;
	private static final int EDIT_AGE = 7;
	private static final int EDIT_ADDRESS = 8;
	private static final int EDIT_ILLNESS = 9;
	private static final int EDIT_EXPERIENCE = 10;

	private String drr = null;// 存放压缩后的图片的路径
	private Bitmap head = null;// 头像Bitmap
	private Bitmap circleHead = null;// 存放在主界面显示的圆角矩形图片
	private float dp;

	private ListView mProfileList;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private MyProfileAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_profile);

		// 设置toolbar
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
				if (str.equals("姓名")) {
					Intent it = new Intent(MeProfile.this,
							EditNameActivity.class);
					it.putExtra("value", value);
					startActivityForResult(it, EDIT_NAME);
				} else if (str.equals("手机号")) {
					Intent it = new Intent(MeProfile.this,
							EditPhoneActivity.class);
					it.putExtra("value", value);
					startActivityForResult(it, EDIT_PHONE);
				} else if (str.equals("身份证号")) {
					Intent it = new Intent(MeProfile.this,
							EditIDNumberActivity.class);
					it.putExtra("value", value);
					startActivityForResult(it, EDIT_ID);
				} else if (str.equals("性别")) {
					Intent it = new Intent(MeProfile.this,
							EditSexActivity.class);
					it.putExtra("value", value);
					startActivityForResult(it, EDIT_SEX);
				} else if (str.equals("年龄")) {
					Intent it = new Intent(MeProfile.this,
							EditAgeActivity.class);
					it.putExtra("value", value);
					startActivityForResult(it, EDIT_AGE);
				} else if (str.equals("地址")) {
					Intent it = new Intent(MeProfile.this,
							EditAddressActivity.class);
					startActivityForResult(it, EDIT_ADDRESS);
				} else if (str.equals("病史")) {
//					Intent it = new Intent(MeProfile.this,
//							EditIllActivity.class);
//					startActivityForResult(it, EDIT_ILLNESS);
//				} else if (str.equals("经验")) {
//					Intent it = new Intent(MeProfile.this,
//							EditExperActivity.class);
//					startActivityForResult(it, EDIT_EXPERIENCE);
				}
			}

			// if(str.equals("姓名")){
			// Builder b = new AlertDialog.Builder(MeProfile.this);
			// b.setTitle("编辑姓名");
			//
			// LinearLayout l = (LinearLayout) getLayoutInflater().inflate(
			// R.layout.edit_name_dialog, null);
			// final EditText editName = (EditText)
			// l.findViewById(R.id.edit_name_dialog);
			// editName.setText(mData.get(position).get("value").toString());
			//
			// b.setView(l);
			//
			// b.setPositiveButton("更新", new DialogInterface.OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface arg0, int arg1) {
			// ListView lstres = (ListView)findViewById(R.id.listView_profile);
			// LinearLayout ll = (LinearLayout)lstres.getChildAt(0);//
			// 获得Item布局，i就是item的位置
			// TextView text = (TextView) ll.findViewById(R.id.value_profile);//
			// 从子级中获得控件
			// text.setText(editName.getText().toString());
			//
			// Map<String,Object> map=new HashMap<String, Object>();
			// map.put("name", "姓名");
			// map.put("value", editName.getText().toString());
			// map.put("img",R.drawable.android_list_idex);
			// mData.set(0, map);
			// listAdapter.notifyDataSetChanged();
			// }
			// }).setNegativeButton("取消", new DialogInterface.OnClickListener()
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
		// touchView要设置到ListView上面
		mSildingFinishLayout.setTouchView(mProfileList);
	}

	/**
	 * 为了获取Camera返回的图片信息，重写该方法。
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case EDIT_NAME:
			if (resultCode == RESULT_OK) {
				String result = data.getExtras().getString("username")
						.toString();
				Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
/*				ListView lstres = (ListView) findViewById(R.id.listView_profile);
				LinearLayout ll = (LinearLayout) lstres.getChildAt(0);// 获得Item布局，i就是item的位置
				TextView text = (TextView) ll.findViewById(R.id.value_profile);// 从子级中获得控件

				text.setText(result);*/

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", "姓名");
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
				map.put("name", "手机号");
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
				map.put("name", "身份证号");
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
				map.put("name", "性别");
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
				map.put("name", "年龄");
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
				map.put("name", "手机号");
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
				map.put("name", "手机号");
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
				map.put("name", "手机号");
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
//				// 把流转化为Bitmap图片
//				head = Bimp.getLoacalBitmap(drr);
				// 圆形头像
//				circleHead = Bimp.createFramedPhoto(480, 480, head,
//						(int) (dp * 1.6f));
				// BitmapDrawable bd= new BitmapDrawable(getResources(),
				// circleHead); //因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
				mPortrait.setImageBitmap(bimp);
			}
			break;
		case RESULT_LOAD_IMAGE:
			if (resultCode == RESULT_OK && data != null) {// 相册返回
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
	 * 点击camera图标从下面弹出来选择框
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
					// 相册
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
	 * 拍照上传
	 */
	public void photo() {
		try {
			// image的获取可以通过调Android自带的Camera应用来完成
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);

			// 如果我们想要读取或者向SD卡写入，这时就必须先要判断一个SD卡的状态，否则有可能出错。
			String sdcardState = Environment.getExternalStorageState();
			String sdcardPathDir = android.os.Environment
					.getExternalStorageDirectory().getPath() + "/tempImage/";
			File file = null;

			// Environment.MEDIA_MOUNTED表示sd卡正常挂载
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				/* 下面就指定image存储在SDCard上的tempImage文件夹下面 */
				File fileDir = new File(sdcardPathDir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();// 若文件夹已经存在就不需要新建文件夹了
				}
				/* 创建一个文件 */
				file = new File(sdcardPathDir + System.currentTimeMillis()
						+ ".JPEG");
			}
			if (file != null) {
				path = file.getPath();// getpath 得到缩写的路径，根据当前目录位置可以缩写路径。得到相对路径。
				// uri的作用是根据这个URI找到某个资源文件，基本格式如： file:///sdcard/temp.jpg
				photoUri = Uri.fromFile(file);
				/*
				 * 由于Camara返回的是缩略图，我们可以传递给他一个参数EXTRA_OUTPUT,
				 * 来将用Camera获取到的图片存储在一个指定的URI位置处。 这样就将文件的存储方式和uri指定到了Camera应用中
				 */
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
				/*
				 * 由于我们需要调用完Camera后，可以返回Camera获取到的图片，
				 * 所以，我们使用startActivityForResult来启动Camera，之后对于获取的图片进行裁剪
				 */
				startActivityForResult(openCameraIntent, TAKE_PICTURE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getData() {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("name", "姓名");
		map.put("value", "石晓辉");
		map.put("img", R.drawable.android_list_idex);
		mData.add(map);

		map = new HashMap<String, Object>();

		map.put("name", "手机号");
		map.put("value", "135****4260");
		map.put("img", R.drawable.android_list_idex);
		mData.add(map);

		map = new HashMap<String, Object>();

		map.put("name", "身份证号");
		map.put("value", "130430********0169");
		map.put("img", R.drawable.android_list_idex);
		mData.add(map);

		map = new HashMap<String, Object>();

		map.put("name", "性别");
		map.put("value", "女");
		map.put("img", R.drawable.android_list_idex);
		mData.add(map);

		map = new HashMap<String, Object>();

		map.put("name", "年龄");
		map.put("value", "23");
		map.put("img", R.drawable.android_list_idex);
		mData.add(map);

		map = new HashMap<String, Object>();

		map.put("name", "地址");
		map.put("value", "");
		map.put("img", R.drawable.android_list_idex);
		mData.add(map);

		map = new HashMap<String, Object>();

		map.put("name", "病史");
		map.put("value", "");
		map.put("img", R.drawable.android_list_idex);
		mData.add(map);

		map = new HashMap<String, Object>();

		map.put("name", "经验");
		map.put("value", "");
		map.put("img", R.drawable.android_list_idex);
		mData.add(map);
	}

	/**
	 * ListView的adapter
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyProfileAdapter extends BaseAdapter {
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

			// 自定义视图
			ViewHolder holder = null;
			if (convertView == null) {// 一开始，显示在屏幕内的item都没有进入Recyclor，所以，convertView作为老的View是空的。
				convertView = mInflater.inflate(R.layout.item_myprofile,
						parent, false);// 创建了一个View之后，这些convertView就会进入Recyclor，而Recyclor的大小依据屏幕上显示对象的多少而定。
				holder = new ViewHolder();
				// 获取控件对象
				holder.name = (TextView) convertView
						.findViewById(R.id.name_profile);
				holder.value = (TextView) convertView
						.findViewById(R.id.value_profile);
				holder.img = (ImageView) convertView
						.findViewById(R.id.image_arrow);
				// 设置控件集到convertView,在View中绑定一个tag，作为附加的数据
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
		mToolbar.setTitle("个人信息");// 标题的文字需在setSupportActionBar之前，不然会无效
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
