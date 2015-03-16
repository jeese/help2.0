package jeese.helpme.help;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import jeese.helpme.R;
import jeese.helpme.photoUtil.Bimp;
import jeese.helpme.photoUtil.ChoosePicActivity;
import jeese.helpme.photoUtil.FileUtils;
import jeese.helpme.photoUtil.PhotoActivity;
import jeese.helpme.service.LocationService;
import jeese.helpme.view.MyGridView;
import jeese.helpme.view.SildingFinishLayout;
import jeese.helpme.view.SildingFinishLayout.OnSildingFinishListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/*==================一次性上传多张图片=====================*/
public class SendQuestionActivity extends ActionBarActivity {

	private Toolbar mToolbar;
	private SildingFinishLayout mSildingFinishLayout;
	private EditText editHelpTitle;
	private EditText editHelpContent;
	private TextView tv_num;// 用来显示剩余字数
	private int numTitle = 30;
	private int numContent = 300;// 限制的最大字数 <br>
	private Button sendLifeHelpBtn;

	private TextView chooseLocation;
	private TextView showLocation;

	private TextView chooseRange;
	private TextView showRange;

	private TextView chooseTime;
	private TextView showTime;
	// private ImageView camera;

	private MyGridView noScrollgridview;;
	private GridAdapter adapter;

	private GridView headgridview;
	private List<ImageAndText> headNameList;

	private static final String SERVER_URL = "http://120.24.208.130:8080/api/";

	// requestCode
	private static final int TAKE_PICTURE = 0;// 拍照的请求码
	private static final int CHOOSE_TIME = 3;// 选择求助范围的请求码
	private static final int CHOOSE_FRIEND = 4;// 选择求助范围的请求码
	private static final int CHOOSE_LOCATION = 5;// 选择位置的请求码

	/**
	 * Activity创建时被调用
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_question);

		init();
	}

	public void init() {
		//设置toolbar
		setToolBar();
		editHelpTitle = (EditText) findViewById(R.id.edit_question_title);
		editHelpContent = (EditText) findViewById(R.id.edit_question_text);
		tv_num = (TextView) findViewById(R.id.textView_shownumber);
		tv_num.setText("300");
		sendLifeHelpBtn = (Button) findViewById(R.id.button_send_question);
		chooseLocation = (TextView) findViewById(R.id.choose_address);
		showLocation = (TextView) findViewById(R.id.show_address);
		chooseRange = (TextView) findViewById(R.id.choose_range);
		showRange = (TextView) findViewById(R.id.show_range);
		showRange.setText("公开");
		chooseTime = (TextView) findViewById(R.id.choose_time);
		showTime = (TextView) findViewById(R.id.show_time);
		showTime.setText("1天");
		
		headNameList = new ArrayList<ImageAndText>();
		headgridview = (GridView) findViewById(R.id.headnamegridview);
		headNameGridInit();
		
		// 初始化gridview
		noScrollgridview = (MyGridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position == Bimp.bmp.size()) {
					new PopupWindows(SendQuestionActivity.this,
							noScrollgridview);
				} else {
					Intent intent = new Intent(SendQuestionActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", position);
					startActivity(intent);
				}
			}
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
		mSildingFinishLayout.setTouchView(mSildingFinishLayout);
		
		/**
		 * 选择当前所在位置
		 */
		chooseLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SendQuestionActivity.this,
						ChooseLocation.class);
				startActivityForResult(intent, CHOOSE_LOCATION);
			}
		});

		/**
		 * 选择对谁公开求助内容
		 */
		chooseRange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SendQuestionActivity.this,
						ChooseHelpRange.class);
				startActivityForResult(intent, CHOOSE_FRIEND);
			}
		});
		
		/**
		 * 选择提问有效期
		 */
		chooseTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SendQuestionActivity.this,
						ChooseHelpTime.class);
				startActivityForResult(intent, CHOOSE_TIME);
			}
		});
		editHelpTitle.addTextChangedListener(new TextWatcher() {
			
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;
            
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				  temp = s;
				  System.out.println("s="+s);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				int number = numTitle - s.length();
//				tv_num.setText("" + number);
//				if(number < 10){
//					tv_num.setTextColor(R.color.chenghuang);
//				}
				selectionStart = editHelpTitle.getSelectionStart();
				selectionEnd = editHelpTitle.getSelectionEnd();
				
				if (temp.length() > numTitle){
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionStart;
					editHelpTitle.setText(s);
                    editHelpTitle.setSelection(tempSelection);//设置光标在最后
				}
			}
		});
		editHelpContent.addTextChangedListener(new TextWatcher() { 
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;
            
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				  temp = s;
				  System.out.println("s="+s);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				int number = numContent - s.length();
				tv_num.setText("" + number);
				if(number < 10){
					tv_num.setTextColor(R.color.chenghuang);
				}
				selectionStart = editHelpContent.getSelectionStart();
				selectionEnd = editHelpContent.getSelectionEnd();
				
				if (temp.length() > numContent){
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionStart;
					editHelpContent.setText(s);
                    editHelpContent.setSelection(tempSelection);//设置光标在最后
				}
			}
		});

		/**
		 * 点击camera图标，弹出选择框进行拍照上传或者从相册选择照片上传或者取消
		 */
/*		camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(SendQuestionActivity.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				new PopupWindows(SendQuestionActivity.this, camera);
			}
		});*/

		/**
		 * 点击发送按钮，把数据发送出去
		 */
		sendLifeHelpBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (editHelpContent.getText().toString().equals("")) {
					Toast.makeText(getApplicationContext(), "发布的内容不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				//设置HttpParams参数，然后创建httpClient对象
				RequestParams params = new RequestParams();
				
				// 获取图片路径
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < Bimp.drr.size(); i++) {
					String Str = Bimp.drr.get(i).substring(
							Bimp.drr.get(i).lastIndexOf("/") + 1,
							Bimp.drr.get(i).lastIndexOf("."));
					list.add(FileUtils.SDPATH + Str + ".JPEG");
				}
				// 高清的压缩图片全部就在 list 路径里面了
				// 高清的压缩过的 bmp 对象 都在 Bimp.bmp里面
				for(int i =0; i < Bimp.drr.size(); i++){
					//图片
					params.addBodyParameter("picfile" + i, new File(list.get(i)));
				}
				//标题
				params.addQueryStringParameter("title", editHelpTitle.getText().toString());
				//内容
				params.addQueryStringParameter("content", editHelpContent.getText().toString());
				//经度
				params.addQueryStringParameter("longitude", String.valueOf(LocationService.getGeoLng()));
				//纬度
				params.addQueryStringParameter("latitude", String.valueOf(LocationService.getGeoLat()));
				//省
				params.addQueryStringParameter("provice", LocationService.getProvince());
				//市
				params.addQueryStringParameter("city", LocationService.getCity());
				//地址
				params.addQueryStringParameter("address", showLocation.getText().toString());
				//提问范围
				params.addQueryStringParameter("range", showRange.getText().toString());
				//提问有效期
				params.addQueryStringParameter("time", showTime.getText().toString());
				
				HttpUtils http = new HttpUtils();
				http.send(HttpMethod.POST, SERVER_URL, params, new RequestCallBack<String>(){
					
			        @Override
			        public void onStart() {
			            //testTextView.setText("conn...");
			        }

			        @Override
			        public void onLoading(long total, long current, boolean isUploading) {
			            if (isUploading) {
			               // testTextView.setText("upload: " + current + "/" + total);
			            } else {
			               // testTextView.setText("reply: " + current + "/" + total);
			            }
			        }

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						
					}
					
				});
			}
		});
	}

	private void headNameGridInit() {
		// TODO Auto-generated method stub
		headNameList
				.add(new ImageAndText(
						"http://www.qqtheme.com/touxiang/UploadPic/2009-11/2009119101320367.gif",
						"灰太狼"));
		headNameList
				.add(new ImageAndText(
						"http://www.qqtai.com/qqhead/UploadFiles_3178/200809/2008092004121344.jpg",
						"小米"));
		headNameList
				.add(new ImageAndText(
						"http://i2.itc.cn/20111107/965_cbc1dac4_dcb2_1638_c3b1_d3d57d816c78_1.jpg",
						"谷歌"));
		headNameList.add(new ImageAndText(
				"http://www.baidu.com/img/baidu_sylogo1.gif", "百度"));
		headNameList
				.add(new ImageAndText(
						"http://i1.itc.cn/20111107/70b_3bbc312b_a7d6_24bf_5e85_f8f181f25d0a_1.jpg",
						"灰太狼"));
		headNameList
				.add(new ImageAndText(
						"http://i1.itc.cn/20111107/2b7c_ca94ecbd_c09e_1d97_1de1_f14109b686e1_1.jpg",
						"小米"));
		headNameList
				.add(new ImageAndText(
						"http://i3.itc.cn/20111107/3ab_8ac7b5d8_442b_d8fe_a4fd_62df63bd0a54_1.jpg",
						"灰太狼"));
		headNameList
				.add(new ImageAndText(
						"http://i3.itc.cn/20111107/29e_9251f52f_9701_341e_672c_5caa426c8501_1.jpg",
						"小米"));
		headNameList
				.add(new ImageAndText(
						"http://i2.itc.cn/20111107/focus_5a_c1ecc3bf_05db_49ff_b005_37a10bc87e86_0.jpg",
						"灰太狼"));
		headNameList
				.add(new ImageAndText(
						"http://i2.itc.cn/20111107/616_fd2be256_04f2_02d5_cf93_31c9028f65b2_1.jpg",
						"小米"));
		headNameList
				.add(new ImageAndText(
						"http://www.qqtheme.com/touxiang/UploadPic/2009-11/2009119101320367.gif",
						"灰太狼"));
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int allWidth = (int) (110 * headNameList.size() * density);
		int itemWidth = (int) (100 * density);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				allWidth, LinearLayout.LayoutParams.FILL_PARENT);
		headgridview.setLayoutParams(params);
		headgridview.setColumnWidth(90);
		headgridview.setHorizontalSpacing(10);
		headgridview.setStretchMode(GridView.NO_STRETCH);
		headgridview.setNumColumns(headNameList.size());
		headgridview.setAdapter(new ImageAndTextListAdapter(this, headNameList,
				headgridview));
	}

	private void setToolBar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// toolbar.setLogo(R.drawable.ic_launcher);
		mToolbar.setTitle("我要提问");// 标题的文字需在setSupportActionBar之前，不然会无效
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

	private Uri photoUri;
	private String path = "";

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

	/**
	 * 为了获取Camera返回的图片信息，重写该方法。
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 9 && resultCode == -1) {
				Bimp.drr.add(path);
				// startPhotoZoom(photoUri);
			}
			break;
		case CHOOSE_TIME:
			if (resultCode == RESULT_OK) {
				String result = data.getExtras().getString("time").toString();
				showTime.setText(result);
			}
			break;
		case CHOOSE_FRIEND:
			if (resultCode == RESULT_OK) {
				String result = data.getExtras().getString("group").toString();
				showRange.setText(result);
			}
			break;
		case CHOOSE_LOCATION:
			if (resultCode == RESULT_OK) {
				String result = data.getExtras().getString("location")
						.toString();
				showLocation.setText(result);
			}
		}
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
					/*
					 * Intent i = new
					 * Intent(Intent.ACTION_PICK,android.provider.
					 * MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					 * startActivityForResult(i, RESULT_LOAD_IMAGE);
					 */
					Intent intent = new Intent(SendQuestionActivity.this,
							ChoosePicActivity.class);
					startActivity(intent);
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
	 * 一个继承BaseAdapter的自定义适配器
	 * 
	 * @author Administrator
	 * 
	 */

	public class GridAdapter extends BaseAdapter {
		/*
		 * 事先写好的布局文件往往不能满足我们的需求，有时会根据情况在代码中自定义控件，这就需要用到LayoutInflater。
		 * LayoutInflater在Android中是“扩展”的意思，作用类似于findViewById().
		 * 不同的是LayoutInflater是用来获得布局文件对象的，而findViewById()是用来获得具体控件的。
		 * LayoutInflater经常在BaseAdapter的getView方法中用到，用来获取整个View并返回。
		 */
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			// return (Bimp.bmp.size() + 1);
			if (Bimp.bmp.size() < 9) {
				return Bimp.bmp.size() + 1;
			} else {
				return Bimp.bmp.size();
			}
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int sign = position;
			// 自定义视图
			ViewHolder holder = null;
			if (convertView == null) {// 一开始，显示在屏幕内的item都没有进入Recyclor，所以，convertView作为老的View是空的。
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);// 创建了一个View之后，这些convertView就会进入Recyclor，而Recyclor的大小依据屏幕上显示对象的多少而定。
				holder = new ViewHolder();
				// 获取控件对象:image
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				// 设置控件集到convertView,在View中绑定一个tag，作为附加的数据
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));// 没有添加图片的时候不显示加号图片
				if (position == 9) {
					holder.image.setVisibility(View.GONE);// 数量达到9，不显示加号图片，不能再添加图片
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));// 显示添加的图片
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								System.out.println(path);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
								Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

	/**
	 *** Activity从后台重新回到前台时被调用
	 * ->当前Activity转到新的Activity界面或按Home键回到主屏，自身退居后台：系统会先调用onPause方法
	 * ，然后调用onStop方法，进入停滞状态。
	 * ->用户后退回到此Activity：系统会先调用onRestart方法，然后调用onStart方法，最后调用onResume方法
	 * ，再次进入运行状态。
	 */
	protected void onRestart() {
		adapter.update();
		System.out.println("SendQuestionActivity is onRestart!!!!!!!!!!!");
		super.onRestart();
	}

	/**
	 * 退出当前Activity时被调用,调用之后Activity就结束了
	 */
	protected void onDestroy() {

		FileUtils.deleteDir();
		// 清理图片缓存
		for (int i = 0; i < Bimp.bmp.size(); i++) {
			Bimp.bmp.get(i).recycle();
		}
		for (int i = 0; i < Bimp.bmp.size(); i++) {
			Bimp.bmp.get(i).recycle();
		}
		Bimp.bmp.clear();
		Bimp.max = 0;
		Bimp.act_bool = true;
		Bimp.drr.clear();
		System.out.println("SendQuestionActivity is onDestory!!!!!!!!!!!");
		super.onDestroy();
	}
}

/*
 * package jeese.helpme.help;
 * 
 * import android.app.Activity; import java.io.File; import
 * java.io.FileNotFoundException; import java.io.IOException; import
 * java.text.SimpleDateFormat; import java.util.ArrayList; import
 * java.util.List;
 * 
 * import net.tsz.afinal.FinalHttp; import net.tsz.afinal.http.AjaxCallBack;
 * import net.tsz.afinal.http.AjaxParams; import jeese.helpme.R; import
 * jeese.helpme.photoUtil.Bimp; import jeese.helpme.photoUtil.FileUtils; import
 * jeese.helpme.photoUtil.PhotoActivity; import
 * jeese.helpme.help.ChooseHelpRange; import
 * jeese.helpme.service.LocationService; import android.content.Context; import
 * android.content.Intent; import android.graphics.Bitmap; import
 * android.graphics.BitmapFactory; import android.graphics.Color; import
 * android.graphics.drawable.BitmapDrawable; import
 * android.graphics.drawable.ColorDrawable; import android.net.Uri; import
 * android.os.Bundle; import android.os.Environment; import
 * android.provider.MediaStore; import android.text.Editable; import
 * android.text.TextWatcher; import android.view.Gravity; import
 * android.view.LayoutInflater; import android.view.View; import
 * android.view.View.OnClickListener; import android.view.ViewGroup; import
 * android.view.ViewGroup.LayoutParams; import
 * android.view.animation.AnimationUtils; import
 * android.view.inputmethod.InputMethodManager; import
 * android.widget.AdapterView; import
 * android.widget.AdapterView.OnItemClickListener; import
 * android.widget.BaseAdapter; import android.widget.Button; import
 * android.widget.EditText; import android.widget.GridView; import
 * android.widget.ImageView; import android.widget.PopupWindow; import
 * android.widget.TextView; import android.widget.Toast;
 * 
 * public class SendQuestionActivity extends Activity implements
 * OnItemClickListener {
 * 
 * private TextView chooseFriend; private Button sendQuestionBtn; private
 * EditText editQuestionContent; private Button sendCancelBtn; private ImageView
 * camera; private GridView gridview; private GridAdapter adapter; private
 * static final String SERVER_URL="http://120.24.208.130:8080/api/";
 * 
 * public List<String> drr = new ArrayList<String>();//存放压缩后的图片的路径 public
 * List<Bitmap> bmp = new ArrayList<Bitmap>();//存放在主界面显示的圆角矩形图片
 * 
 * private float dp; //requestCode private static final int TAKE_PICTURE =
 * 0;//拍照的请求码 private static final int RESULT_LOAD_IMAGE = 1;//从相册选取的请求码 private
 * static final int CUT_PHOTO_REQUEST_CODE = 2;//裁剪图片的请求码 private static final
 * int CHOOSE_FRIEND = 4;//选择求助范围的请求码
 * 
 * @Override protected void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState); setContentView(R.layout.send_question);
 * 
 * init(); }
 * 
 * public void init(){ // dp = getResources().getDimension(R.dimen.dp);//边框大小
 * 
 * chooseFriend=(TextView) findViewById(R.id.choose_friend);
 * editQuestionContent=(EditText) findViewById(R.id.edit_ask_question_text);
 * sendQuestionBtn=(Button) findViewById(R.id.send_btn); sendCancelBtn=(Button)
 * findViewById(R.id.cancel_send_btn); camera=(ImageView)
 * findViewById(R.id.send_cose_camera); //初始化gridview //
 * selectimg_horizontalScrollView = (HorizontalScrollView)
 * findViewById(R.id.selectimg_horizontalScrollView); gridview=(GridView)
 * findViewById(R.id.noScrollgridview); gridview.setSelector(new
 * ColorDrawable(Color.TRANSPARENT)); gridviewInit();
 * 
 * // editHelpContent.setFocusable(true); //
 * editHelpContent.setFocusableInTouchMode(true);
 *//**
 * 点击右下角设置对谁公开求助内容
 */
/*
 * chooseFriend.setOnClickListener(new OnClickListener() {
 * 
 * @Override public void onClick(View arg0) { // TODO Auto-generated method stub
 * Intent intent = new Intent(SendQuestionActivity.this, ChooseHelpRange.class);
 * startActivityForResult(intent, CHOOSE_FRIEND); } });
 *//**
 * 点击camera图标，弹出选择框进行拍照上传或者从相册选择照片上传或者取消
 */
/*
 * camera.setOnClickListener(new OnClickListener() {
 * 
 * @Override public void onClick(View arg0) { new
 * PopupWindows(SendQuestionActivity.this, camera); } });
 *//**
 * 根据输入文本的变化更改确认按钮的字体颜色
 */
/*
 * editQuestionContent.addTextChangedListener(new TextWatcher() {
 * 
 * @Override public void onTextChanged(CharSequence arg0, int arg1, int arg2,
 * int arg3) { // 根据输入文本的变化更改确认按钮的字体颜色 String str =
 * editQuestionContent.getText().toString(); if(str.equals("")){
 * sendQuestionBtn.setTextColor(getResources().getColor(R.color.gray)); }else{
 * sendQuestionBtn.setTextColor(getResources().getColor(R.color.dark_gray)); } }
 * 
 * @Override public void beforeTextChanged(CharSequence arg0, int arg1, int
 * arg2, int arg3) { }
 * 
 * @Override public void afterTextChanged(Editable arg0) { } });
 *//**
 * 点击取消按钮关闭当前界面
 */
/*
 * sendCancelBtn.setOnClickListener(new OnClickListener() {
 * 
 * @Override public void onClick(View v) { SendQuestionActivity.this.finish(); }
 * });
 *//**
 * 点击发送按钮，把数据发送出去(未实现！！！)
 */
/*
 * sendQuestionBtn.setOnClickListener(new OnClickListener() {
 * 
 * @Override public void onClick(View arg0) { // TODO Auto-generated method stub
 * String content = editQuestionContent.getText().toString(); if
 * (content.equals("")) { Toast.makeText(getApplicationContext(), "发布的内容不能为空",
 * Toast.LENGTH_SHORT).show(); return; } Double loaitude =
 * LocationService.getGeoLat(); Double longitude = LocationService.getGeoLng();
 * String city = LocationService.getCityCode(); String address =
 * LocationService.getAddress();
 * 
 * AjaxParams params=new AjaxParams(); params.put("username", "xiaoming");
 * params.put("datetime", "2014-12-16"); params.put("type", "1");
 * params.put("content", content); params.put("longitude",
 * longitude.toString()); params.put("latitude", loaitude.toString());
 * params.put("city", city); params.put("address", address);
 * 
 * // 上传图片文件 for(int i=0; i < drr.size(); i++){ String path = drr.get(i); try {
 * params.put("profile_picture", new File(path)); } catch (FileNotFoundException
 * e) { // TODO Auto-generated catch block e.printStackTrace(); } }
 * 
 * FinalHttp finalHttp = new FinalHttp(); finalHttp.post(SERVER_URL, params, new
 * AjaxCallBack<Object>(){
 * 
 * @Override public void onLoading(long count, long current) {
 * super.onLoading(count, current); }
 * 
 * @Override public void onSuccess(Object t) { System.out.println("返回信息是：" +
 * t.toString()); } }); } }); }
 * 
 * private Uri photoUri;
 *//**
 * 拍照上传
 */
/*
 * public void photo() { try { //image的获取可以通过调Android自带的Camera应用来完成 Intent
 * openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
 * 
 * //如果我们想要读取或者向SD卡写入，这时就必须先要判断一个SD卡的状态，否则有可能出错。 String sdcardState =
 * Environment.getExternalStorageState(); String sdcardPathDir =
 * android.os.Environment.getExternalStorageDirectory().getPath() +
 * "/tempImage/"; File file = null;
 * 
 * //Environment.MEDIA_MOUNTED表示sd卡正常挂载 if
 * (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
 * 下面就指定image存储在SDCard上的tempImage文件夹下面 File fileDir = new File(sdcardPathDir);
 * if (!fileDir.exists()) { fileDir.mkdirs();//若文件夹已经存在就不需要新建文件夹了 } 创建一个文件 file
 * = new File(sdcardPathDir + System.currentTimeMillis() + ".JPEG"); } if (file
 * != null) { // path = file.getPath();//getpath 得到缩写的路径，根据当前目录位置可以缩写路径。得到相对路径。
 * //uri的作用是根据这个URI找到某个资源文件，基本格式如： file:///sdcard/temp.jpg photoUri =
 * Uri.fromFile(file);
 * 
 * 由于Camara返回的是缩略图，我们可以传递给他一个参数EXTRA_OUTPUT,来将用Camera获取到的图片存储在一个指定的URI位置处。
 * 这样就将文件的存储方式和uri指定到了Camera应用中
 * 
 * openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
 * 
 * 由于我们需要调用完Camera后，可以返回Camera获取到的图片，
 * 所以，我们使用startActivityForResult来启动Camera，之后对于获取的图片进行裁剪
 * 
 * startActivityForResult(openCameraIntent, TAKE_PICTURE); } } catch (Exception
 * e) { e.printStackTrace(); } }
 *//**
 * 缩放图片
 * */
/*
 * private void startPhotoZoom(Uri uri) { try { //SimpleDateFormat
 * 使得可以选择任何用户定义的日期-时间格式的模式。 SimpleDateFormat sDateFormat = new
 * SimpleDateFormat("yyyyMMddhhmmss");
 * //SimpleDateFormat中的format方法可以把Date型的字符串转换成特定格式的String类型 String address =
 * sDateFormat.format(new java.util.Date());
 * 
 * if (!FileUtils.isFileExist("")) { FileUtils.createSDDir(""); }
 * 在数组drr中增加一个新的图片路径 drr.add(FileUtils.SDPATH + address + ".JPEG");
 * //这是用的隐式意图激活，就是没有指定具体的组件名称，而是通过intent在系统中找到一个最合适的Activity，
 * //通过这个URI可以访问一个本地的资源，即图片资源 Uri imageUri = Uri.parse("file:///sdcard/formats/"
 * + address + ".JPEG");
 * 
 * final Intent intent = new Intent("com.android.camera.action.CROP");
 * intent.setDataAndType(uri, "image/*");
 * 
 * intent.putExtra("crop", "true"); intent.putExtra("aspectX", 1);
 * intent.putExtra("aspectY", 1); intent.putExtra("outputX", 480);
 * intent.putExtra("outputY", 480); intent.putExtra(MediaStore.EXTRA_OUTPUT,
 * imageUri);
 * intent.putExtra("outputFormat",Bitmap.CompressFormat.JPEG.toString());
 * intent.putExtra("noFaceDetection", false); intent.putExtra("return-data",
 * false); startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE); } catch
 * (IOException e) { e.printStackTrace(); } }
 *//**
 * 为了获取Camera返回的图片信息，重写该方法。
 */
/*
 * protected void onActivityResult(int requestCode, int resultCode, Intent data)
 * { switch (requestCode) { case TAKE_PICTURE: if (drr.size() < 9 && resultCode
 * == -1) { startPhotoZoom(photoUri); } break; case RESULT_LOAD_IMAGE: if
 * (drr.size() < 9 && resultCode == RESULT_OK && null != data) { Uri uri =
 * data.getData();//得到图片的路径 if (uri != null) { startPhotoZoom(uri); } } break;
 * case CUT_PHOTO_REQUEST_CODE: if (resultCode == RESULT_OK && null != data) {
 * //获得当前的bitmap Bitmap map = Bimp.getLoacalBitmap(drr.get(drr.size() - 1));
 * //在photoactivity中的List<Bitmap> bitmap中添加一个bitmap //
 * PhotoActivity.bitmap.add(map); //得到圆角的小矩形图片 map = Bimp.createFramedPhoto(480,
 * 480, map,(int) (dp * 1.6f)); bmp.add(map); gridviewInit();//更新主界面gridview }
 * break; case CHOOSE_FRIEND: if(resultCode == RESULT_OK){ String result =
 * data.getExtras().getString("group").toString(); chooseFriend.setText(result);
 * } break; } }
 *//**
 * 点击camera图标从下面弹出来选择框
 * */
/*
 * public class PopupWindows extends PopupWindow {
 * 
 * public PopupWindows(Context mContext, View parent) {
 * 
 * View view = View .inflate(mContext, R.layout.item_popupwindows, null);
 * view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
 * // LinearLayout ll_popup = (LinearLayout) view //
 * .findViewById(R.id.ll_popup); setWidth(LayoutParams.FILL_PARENT);
 * setHeight(LayoutParams.FILL_PARENT); setBackgroundDrawable(new
 * BitmapDrawable()); setFocusable(true); setOutsideTouchable(true);
 * setContentView(view); showAtLocation(parent, Gravity.BOTTOM, 0, 0); update();
 * 
 * Button bt1 = (Button) view .findViewById(R.id.item_popupwindows_camera);
 * Button bt2 = (Button) view .findViewById(R.id.item_popupwindows_photo);
 * Button bt3 = (Button) view .findViewById(R.id.item_popupwindows_cancel);
 * bt1.setOnClickListener(new OnClickListener() { public void onClick(View v) {
 * photo(); dismiss(); } }); bt2.setOnClickListener(new OnClickListener() {
 * public void onClick(View v) { Intent i = new
 * Intent(Intent.ACTION_PICK,android
 * .provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
 * startActivityForResult(i, RESULT_LOAD_IMAGE); dismiss(); } });
 * bt3.setOnClickListener(new OnClickListener() { public void onClick(View v) {
 * dismiss(); } }); } }
 *//**
 * 一个继承BaseAdapter的自定义适配器
 * 
 * @author Administrator
 * 
 */
/*
 * public class GridAdapter extends BaseAdapter {
 * 事先写好的布局文件往往不能满足我们的需求，有时会根据情况在代码中自定义控件，这就需要用到LayoutInflater。
 * LayoutInflater在Android中是“扩展”的意思，作用类似于findViewById().
 * 不同的是LayoutInflater是用来获得布局文件对象的，而findViewById()是用来获得具体控件的。
 * LayoutInflater经常在BaseAdapter的getView方法中用到，用来获取整个View并返回。 private
 * LayoutInflater listContainer; private int selectedPosition = -1; private
 * boolean shape;
 * 
 * public boolean isShape() { return shape; }
 * 
 * public void setShape(boolean shape) { this.shape = shape; }
 * 
 * public class ViewHolder { public ImageView image; public Button bt; }
 * 
 * public GridAdapter(Context context) { listContainer =
 * LayoutInflater.from(context); } public GridAdapter(Context
 * context,ArrayList<Bitmap>bmpList){ listContainer =
 * LayoutInflater.from(context); } public int getCount() { if (bmp.size() < 9) {
 * return bmp.size() + 1; } else { return bmp.size(); } }
 * 
 * public Object getItem(int arg0) { return null; }
 * 
 * public long getItemId(int arg0) { return 0; }
 * 
 * public void setSelectedPosition(int position) { selectedPosition = position;
 * }
 * 
 * public int getSelectedPosition() { return selectedPosition; }
 *//**
 * ListView Item设置
 */
/*
 * public View getView(int position, View convertView, ViewGroup parent) { final
 * int sign = position; // 自定义视图 ViewHolder holder = null; if (convertView ==
 * null) { holder = new ViewHolder(); // 获取list_item布局文件的视图 convertView =
 * listContainer.inflate( R.layout.item_gridview, null);
 * 
 * // 获取控件对象:image和右上角的小圆角删除按钮 holder.image = (ImageView) convertView
 * .findViewById(R.id.item_grida_image); holder.bt = (Button) convertView
 * .findViewById(R.id.item_grida_bt); // 设置控件集到convertView
 * convertView.setTag(holder); } else { holder = (ViewHolder)
 * convertView.getTag(); }
 * 
 * if (position == bmp.size()) {
 * holder.image.setImageBitmap(BitmapFactory.decodeResource( getResources(),
 * R.drawable.icon_addpic_unfocused)); // holder.image.setLayoutParams(new
 * LinearLayout.LayoutParams(screenW, screenH));
 * holder.bt.setVisibility(View.GONE);//隐藏右上角红色删除小按钮 if(position == 0){
 * holder.image.setVisibility(View.GONE);
 * holder.bt.setVisibility(View.GONE);//隐藏右上角红色删除小按钮 } if (position == 9) {
 * holder.image.setVisibility(View.GONE);//数量达到9，不能再添加图片 } } else {
 * holder.image.setImageBitmap(bmp.get(position)); //点击右上角按钮，删除当前图片
 * holder.bt.setOnClickListener(new OnClickListener() {
 * 
 * public void onClick(View v) { // PhotoActivity.bitmap.remove(sign);
 * bmp.get(sign).recycle(); bmp.remove(sign); drr.remove(sign);
 * 
 * gridviewInit(); } }); } return convertView; } }
 *//**
 * 初始化和更新主界面的gridview
 */
/*
 * public void gridviewInit() { adapter = new GridAdapter(this);
 * adapter.setSelectedPosition(0);
 * 
 * //gridview的size最大是9 int size = 0; if (bmp.size() < 9) { size = bmp.size() +
 * 1;//选中的图片的数量和一个用来添加图片的图片 } else { size = bmp.size(); }
 * //利用getLayoutParams()方法，获取控件的LayoutParams // LayoutParams params =
 * gridview.getLayoutParams(); // final int width = size * (int) (dp * 9.4f); //
 * params.width = width; // gridview.setLayoutParams(params); //
 * gridview.setColumnWidth((int) (dp * 9.4f)); //
 * gridview.setStretchMode(GridView.NO_STRETCH); //
 * gridview.setNumColumns(size); gridview.setAdapter(adapter);
 * gridview.setOnItemClickListener(this);
 * 
 * //view 布局完成时调用，每次view改变时都会调用
 * selectimg_horizontalScrollView.getViewTreeObserver() .addOnPreDrawListener(//
 * 绘制完毕 new OnPreDrawListener() { public boolean onPreDraw() {
 * selectimg_horizontalScrollView.scrollTo(width, 0);
 * selectimg_horizontalScrollView .getViewTreeObserver()
 * .removeOnPreDrawListener(this); return false; } }); }
 *//**
 * 点击item继续添加图片
 */
/*
 * public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
 * { ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
 * .hideSoftInputFromWindow(SendQuestionActivity.this
 * .getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); if
 * (arg2 == bmp.size()) { String sdcardState =
 * Environment.getExternalStorageState(); if
 * (Environment.MEDIA_MOUNTED.equals(sdcardState)) { new
 * PopupWindows(SendQuestionActivity.this, gridview); } else {
 * Toast.makeText(getApplicationContext(), "sdcard已拔出，不能选择照片",
 * Toast.LENGTH_SHORT).show(); } } else { Intent intent = new
 * Intent(SendQuestionActivity.this, PhotoActivity.class);
 * 
 * intent.putExtra("ID", arg2); startActivity(intent); } } }
 */

