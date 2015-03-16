package jeese.helpme.activity;

import io.rong.imkit.RongIM;
import jeese.helpme.R;
import jeese.helpme.application.App;
import jeese.helpme.auth.LoginActivity;
import jeese.helpme.discover.Discover_Fragment;
import jeese.helpme.help.Help_Fragment;
import jeese.helpme.home.Home_Fragment;
import jeese.helpme.me.Me_Fragment;
import jeese.helpme.setting.SettingsActivity;
import jeese.helpme.people.People_Fragment;
import jeese.helpme.service.LocationService;
import jeese.helpme.service.MainService;
import jeese.helpme.util.DummyTabContent;
import jeese.helpme.view.BadgeView;
import jeese.helpme.view.MaterialDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private ActionBar actionBar;
	private Toolbar mToolbar;
	private TabHost tabHost;
	private TabWidget tabWidget;
	private int CURRENT_TAB = 3;
	/**
	 * 自定义初始界面 0->home 1->discover 2->help 3->people 4->me
	 */
	private int CUSTOM_TAB = 2;
	private int firstenter = 0;
	private Home_Fragment homeFragment;
	private Discover_Fragment discoverFragment;
	private Help_Fragment helpFragment;
	private People_Fragment peopleFragment;
	private Me_Fragment meFragment;
	private android.support.v4.app.FragmentTransaction ft;
	private RelativeLayout tabIndicator1, tabIndicator2, tabIndicator3,
			tabIndicator4, tabIndicator5;

	private TextView tvTab1;
	private ImageView ivTab1;
	private TextView tvTab2;
	private ImageView ivTab2;
	private ImageView ivTab3;
	private TextView tvTab4;
	private ImageView ivTab4;
	private TextView tvTab5;
	private ImageView ivTab5;

	private View v1;
	private View v2;
	private View v3;
	private View v4;
	
	private int ONLINE = 1;
	private int OFFLINE = 2;
	private int NETWORK_ERROR = 3;
	private int STATE = 0;
	private MaterialDialog mMaterialDialog_1;
	private MaterialDialog mMaterialDialog_2;
	private Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViews();

		// 开启后台服务
		Intent intent = new Intent(this, MainService.class);
		startService(intent);

		// 定位一次
		Intent intent_1 = new Intent(this, LocationService.class);
		startService(intent_1);

		findTabView();
		tabHost.setup();

		/** 监听 */
		TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {

				/** 碎片管理 */
				android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
				homeFragment = (Home_Fragment) fm.findFragmentByTag("home");
				discoverFragment = (Discover_Fragment) fm
						.findFragmentByTag("discover");
				helpFragment = (Help_Fragment) fm.findFragmentByTag("help");
				peopleFragment = (People_Fragment) fm
						.findFragmentByTag("people");
				meFragment = (Me_Fragment) fm.findFragmentByTag("me");
				ft = fm.beginTransaction();

				/** 如果存在hide */
				if (homeFragment != null)
					ft.hide(homeFragment);

				if (discoverFragment != null)
					ft.hide(discoverFragment);

				if (helpFragment != null)
					ft.hide(helpFragment);

				if (peopleFragment != null)
					ft.hide(peopleFragment);

				if (meFragment != null)
					ft.hide(meFragment);

				/** 如果当前选项卡是home */
				if (tabId.equalsIgnoreCase("home")) {
					if (firstenter < 1) {
						// ft.add(R.id.realtabcontent, new Discover_Fragment(),
						// "discover");
						firstenter++;
					} else {
						isTabHome();
						CURRENT_TAB = 1;
					}

					/** 如果当前选项卡是discover */
				} else if (tabId.equalsIgnoreCase("discover")) {
					isTabDiscover();
					CURRENT_TAB = 2;

					/** 如果当前选项卡是help */
				} else if (tabId.equalsIgnoreCase("help")) {
					isTabHelp();
					CURRENT_TAB = 3;

					/** 如果当前选项卡是people */
				} else if (tabId.equalsIgnoreCase("people")) {
					isTabPeople();
					CURRENT_TAB = 4;

					/** 如果当前选项卡是me */
				} else if (tabId.equalsIgnoreCase("me")) {
					isTabMe();
					CURRENT_TAB = 5;
				} else {
					switch (CURRENT_TAB) {
					case 1:
						isTabHome();
						break;
					case 2:
						isTabDiscover();
						break;
					case 3:
						isTabHelp();
						break;
					case 4:
						isTabPeople();
						break;
					case 5:
						isTabMe();
						break;
					}

				}
				ft.commit();
			}

		};
		// 设置初始选项卡
		tabHost.setOnTabChangedListener(tabChangeListener);
		initTab();
		tabHost.setCurrentTab(CUSTOM_TAB);

		//数字脚标
		setBadgeView();
		
		//网络状态
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				NetworkState();
			}
		}, 3000);
		
	}

	private void initViews() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		// toolbar.setLogo(R.drawable.ic_launcher);
		mToolbar.setTitle("易助");// 标题的文字需在setSupportActionBar之前，不然会无效
		// toolbar.setSubtitle("副标题");
		setSupportActionBar(mToolbar);

		actionBar = getSupportActionBar();

		/* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过下面的两个回调方法来处理 */
		mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.action_message:
					// 启动会话列表
					RongIM.getInstance().startConversationList(
							MainActivity.this);
					return true;
					//启动设置页
				case R.id.action_settings:
					Intent it = new Intent(MainActivity.this, SettingsActivity.class);
					startActivity(it);					
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
//		MenuItem message = menu.findItem(R.id.action_message);
//		message.setIcon(icon)
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);

//		MenuItem message = menu.findItem(R.id.action_message);
//		message.setIcon(icon)
		return super.onPrepareOptionsMenu(menu);
	}

	// 判断当前
	public void isTabHome() {

		if (homeFragment == null) {
			ft.add(R.id.realtabcontent, new Home_Fragment(), "home");
		} else {
			ft.show(homeFragment);
		}

		actionBar.show();

		tvTab1.setTextColor(getResources().getColor(R.color.tabtext2));
		tvTab2.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab4.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab5.setTextColor(getResources().getColor(R.color.tabtext1));
	}

	public void isTabDiscover() {

		if (discoverFragment == null) {
			ft.add(R.id.realtabcontent, new Discover_Fragment(), "discover");
		} else {
			ft.show(discoverFragment);
		}

		actionBar.show();

		tvTab1.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab2.setTextColor(getResources().getColor(R.color.tabtext2));
		tvTab4.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab5.setTextColor(getResources().getColor(R.color.tabtext1));
	}

	public void isTabHelp() {

		if (helpFragment == null) {
			ft.add(R.id.realtabcontent, new Help_Fragment(), "help");
		} else {
			ft.show(helpFragment);
		}

		actionBar.show();

		tvTab1.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab2.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab4.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab5.setTextColor(getResources().getColor(R.color.tabtext1));
	}

	public void isTabPeople() {

		if (peopleFragment == null) {
			ft.add(R.id.realtabcontent, new People_Fragment(), "people");
		} else {
			ft.show(peopleFragment);
		}

		actionBar.show();

		tvTab1.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab2.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab4.setTextColor(getResources().getColor(R.color.tabtext2));
		tvTab5.setTextColor(getResources().getColor(R.color.tabtext1));
	}

	public void isTabMe() {

		if (meFragment == null) {
			ft.add(R.id.realtabcontent, new Me_Fragment(), "me");
		} else {
			ft.show(meFragment);
		}

		actionBar.show();

		tvTab1.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab2.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab4.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab5.setTextColor(getResources().getColor(R.color.tabtext2));
	}

	/**
	 * 找到Tabhost布局
	 */
	public void findTabView() {

		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		LinearLayout layout = (LinearLayout) tabHost.getChildAt(0);
		tabWidget = (TabWidget) layout.getChildAt(3);

		tabIndicator1 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, tabWidget, false);
		tvTab1 = (TextView) tabIndicator1.getChildAt(1);
		ivTab1 = (ImageView) tabIndicator1.getChildAt(0);
		v1 = (View) tabIndicator1.getChildAt(2);
		ivTab1.setBackgroundResource(R.drawable.selector_mood_home);
		tvTab1.setText(R.string.buttom_home);

		tabIndicator2 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, tabWidget, false);
		tvTab2 = (TextView) tabIndicator2.getChildAt(1);
		ivTab2 = (ImageView) tabIndicator2.getChildAt(0);
		v2 = (View) tabIndicator2.getChildAt(2);
		ivTab2.setBackgroundResource(R.drawable.selector_mood_discover);
		tvTab2.setText(R.string.buttom_discover);

		tabIndicator3 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab_indicator_help, tabWidget, false);
		ivTab3 = (ImageView) tabIndicator3.getChildAt(0);
		ivTab3.setBackgroundResource(R.drawable.selector_mood_help);

		tabIndicator4 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, tabWidget, false);
		tvTab4 = (TextView) tabIndicator4.getChildAt(1);
		ivTab4 = (ImageView) tabIndicator4.getChildAt(0);
		v3 = (View) tabIndicator4.getChildAt(2);
		ivTab4.setBackgroundResource(R.drawable.selector_mood_people);
		tvTab4.setText(R.string.buttom_people);

		tabIndicator5 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, tabWidget, false);
		tvTab5 = (TextView) tabIndicator5.getChildAt(1);
		ivTab5 = (ImageView) tabIndicator5.getChildAt(0);
		v4 = (View) tabIndicator5.getChildAt(2);
		ivTab5.setBackgroundResource(R.drawable.selector_mood_me);
		tvTab5.setText(R.string.buttom_me);
	}

	/**
	 * 初始化选项卡
	 * 
	 * */
	public void initTab() {

		TabHost.TabSpec tSpecHome = tabHost.newTabSpec("home");
		tSpecHome.setIndicator(tabIndicator1);
		tSpecHome.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpecHome);

		TabHost.TabSpec tSpecDiscover = tabHost.newTabSpec("discover");
		tSpecDiscover.setIndicator(tabIndicator2);
		tSpecDiscover.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpecDiscover);

		TabHost.TabSpec tSpecHelp = tabHost.newTabSpec("help");
		tSpecHelp.setIndicator(tabIndicator3);
		tSpecHelp.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpecHelp);

		TabHost.TabSpec tSpecPeople = tabHost.newTabSpec("people");
		tSpecPeople.setIndicator(tabIndicator4);
		tSpecPeople.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpecPeople);

		TabHost.TabSpec tSpecMe = tabHost.newTabSpec("me");
		tSpecMe.setIndicator(tabIndicator5);
		tSpecMe.setContent(new DummyTabContent(getBaseContext()));
		tabHost.addTab(tSpecMe);

	}
	
	private void NetworkState(){
		App myApp = ((App) getApplicationContext());
		STATE = myApp.STATE;
		
		mMaterialDialog_1 = new MaterialDialog(this)
	    .setTitle("登录失败")
	    .setMessage("您的登录尝试失败，请检查您的账户及密码信息后重新登录。")
	    .setPositiveButton("确认", new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	mMaterialDialog_1.dismiss();
	            
				// 页面跳转到登录页面
				Intent intent = new Intent(MainActivity.this,
						LoginActivity.class);
				MainActivity.this.startActivity(intent);
	        	finish();
	        }
	    });
		
		mMaterialDialog_2 = new MaterialDialog(this)
	    .setTitle("网络不畅")
	    .setMessage("您的设备网络连接不畅，请检查您的网络。")
	    .setPositiveButton("确认", new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	mMaterialDialog_2.dismiss();

	        }
	    });
		
		if(STATE == OFFLINE){
			mMaterialDialog_1.show();
		}else if(STATE == NETWORK_ERROR){
			mMaterialDialog_2.show();
		}
	}

	private void setBadgeView() {
		BadgeView bv = new BadgeView(this, v1);
		bv.setText("3");
		bv.setTextColor(Color.WHITE);
		bv.setTextSize(12);
		bv.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		bv.show();

//		BadgeView bv1 = new BadgeView(this, v3);
//		bv1.setText("1");
//		bv1.setTextColor(Color.WHITE);
//		bv1.setTextSize(12);
//		bv1.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//		bv1.show();
//
//		BadgeView bv2 = new BadgeView(this, v4);
//		bv2.setText("1");
//		bv2.setTextColor(Color.WHITE);
//		bv2.setTextSize(12);
//		bv2.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//		bv2.show();

	}
}
