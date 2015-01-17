package jeese.helpme.activity;

import jeese.helpme.R;
import jeese.helpme.discover.Discover_Fragment;
import jeese.helpme.help.Help_Fragment;
import jeese.helpme.home.Home_Fragment;
import jeese.helpme.me.Me_Fragment;
import jeese.helpme.people.People_Fragment;
import jeese.helpme.service.LocationService;
import jeese.helpme.service.MainService;
import jeese.helpme.util.DummyTabContent;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	TabHost tabHost;
	TabWidget tabWidget;
	LinearLayout bottom_layout;
	int CURRENT_TAB = 3;
	/**
	 * �Զ����ʼ���� 0->home 1->discover 2->help 3->people 4->me
	 */
	int CUSTOM_TAB = 2;
	int firstenter = 0;
	Home_Fragment homeFragment;
	Discover_Fragment discoverFragment;
	Help_Fragment helpFragment;
	People_Fragment peopleFragment;
	Me_Fragment meFragment;
	android.support.v4.app.FragmentTransaction ft;
	RelativeLayout tabIndicator1, tabIndicator2, tabIndicator3, tabIndicator4,
			tabIndicator5;

	TextView tvTab1;
	ImageView ivTab1;
	TextView tvTab2;
	ImageView ivTab2;
	ImageView ivTab3;
	TextView tvTab4;
	ImageView ivTab4;
	TextView tvTab5;
	ImageView ivTab5;

	ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ͼ������Ƿ���ʾ��������false����ʾ

		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);

		// ������̨����
		Intent intent = new Intent(this, MainService.class);
		startService(intent);

		// ��λһ��
		Intent intent_1 = new Intent(this, LocationService.class);
		startService(intent_1);

		findTabView();
		tabHost.setup();

		/** ���� */
		TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {

				/** ��Ƭ���� */
				android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
				homeFragment = (Home_Fragment) fm.findFragmentByTag("home");
				discoverFragment = (Discover_Fragment) fm
						.findFragmentByTag("discover");
				helpFragment = (Help_Fragment) fm.findFragmentByTag("help");
				peopleFragment = (People_Fragment) fm
						.findFragmentByTag("people");
				meFragment = (Me_Fragment) fm.findFragmentByTag("me");
				ft = fm.beginTransaction();

				/** �������hide */
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

				/** �����ǰѡ���home */
				if (tabId.equalsIgnoreCase("home")) {
					if (firstenter < 1) {
						// ft.add(R.id.realtabcontent, new Discover_Fragment(),
						// "discover");
						firstenter++;
					} else {
						isTabHome();
						CURRENT_TAB = 1;
					}

					/** �����ǰѡ���discover */
				} else if (tabId.equalsIgnoreCase("discover")) {
					isTabDiscover();
					CURRENT_TAB = 2;

					/** �����ǰѡ���help */
				} else if (tabId.equalsIgnoreCase("help")) {
					isTabHelp();
					CURRENT_TAB = 3;

					/** �����ǰѡ���people */
				} else if (tabId.equalsIgnoreCase("people")) {
					isTabPeople();
					CURRENT_TAB = 4;

					/** �����ǰѡ���me */
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
		// ���ó�ʼѡ�
		tabHost.setOnTabChangedListener(tabChangeListener);
		initTab();
		tabHost.setCurrentTab(CUSTOM_TAB);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// �жϵ�ǰ
	public void isTabHome() {

		if (homeFragment == null) {
			actionBar.show();
			ft.add(R.id.realtabcontent, new Home_Fragment(), "home");
		} else {
			actionBar.show();
			ft.show(homeFragment);
		}
		tvTab1.setTextColor(getResources().getColor(R.color.tabtext2));
		tvTab2.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab4.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab5.setTextColor(getResources().getColor(R.color.tabtext1));
	}

	public void isTabDiscover() {

		if (discoverFragment == null) {
			actionBar.hide();
			ft.add(R.id.realtabcontent, new Discover_Fragment(), "discover");
		} else {
			actionBar.hide();
			ft.show(discoverFragment);
		}
		tvTab1.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab2.setTextColor(getResources().getColor(R.color.tabtext2));
		tvTab4.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab5.setTextColor(getResources().getColor(R.color.tabtext1));
	}

	public void isTabHelp() {

		if (helpFragment == null) {
			actionBar.hide();
			ft.add(R.id.realtabcontent, new Help_Fragment(), "help");
		} else {
			actionBar.hide();
			ft.show(helpFragment);
		}
		tvTab1.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab2.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab4.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab5.setTextColor(getResources().getColor(R.color.tabtext1));
	}

	public void isTabPeople() {

		if (peopleFragment == null) {
			actionBar.show();
			ft.add(R.id.realtabcontent, new People_Fragment(), "people");
		} else {
			actionBar.show();
			ft.show(peopleFragment);
		}
		tvTab1.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab2.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab4.setTextColor(getResources().getColor(R.color.tabtext2));
		tvTab5.setTextColor(getResources().getColor(R.color.tabtext1));
	}

	public void isTabMe() {

		if (meFragment == null) {
			actionBar.hide();
			ft.add(R.id.realtabcontent, new Me_Fragment(), "me");
		} else {
			actionBar.hide();
			ft.show(meFragment);
		}
		tvTab1.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab2.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab4.setTextColor(getResources().getColor(R.color.tabtext1));
		tvTab5.setTextColor(getResources().getColor(R.color.tabtext2));
	}

	/**
	 * �ҵ�Tabhost����
	 */
	public void findTabView() {

		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		LinearLayout layout = (LinearLayout) tabHost.getChildAt(0);
		TabWidget tw = (TabWidget) layout.getChildAt(1);

		tabIndicator1 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, tw, false);
		tvTab1 = (TextView) tabIndicator1.getChildAt(2);
		ivTab1 = (ImageView) tabIndicator1.getChildAt(1);
		ivTab1.setBackgroundResource(R.drawable.selector_mood_home);
		tvTab1.setText(R.string.buttom_home);

		tabIndicator2 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, tw, false);
		tvTab2 = (TextView) tabIndicator2.getChildAt(2);
		ivTab2 = (ImageView) tabIndicator2.getChildAt(1);
		ivTab2.setBackgroundResource(R.drawable.selector_mood_discover);
		tvTab2.setText(R.string.buttom_discover);

		tabIndicator3 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab_indicator_help, tw, false);
		ivTab3 = (ImageView) tabIndicator3.getChildAt(1);
		ivTab3.setBackgroundResource(R.drawable.selector_mood_help);

		tabIndicator4 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, tw, false);
		tvTab4 = (TextView) tabIndicator4.getChildAt(2);
		ivTab4 = (ImageView) tabIndicator4.getChildAt(1);
		ivTab4.setBackgroundResource(R.drawable.selector_mood_people);
		tvTab4.setText(R.string.buttom_people);

		tabIndicator5 = (RelativeLayout) LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, tw, false);
		tvTab5 = (TextView) tabIndicator5.getChildAt(2);
		ivTab5 = (ImageView) tabIndicator5.getChildAt(1);
		ivTab5.setBackgroundResource(R.drawable.selector_mood_me);
		tvTab5.setText(R.string.buttom_me);
	}

	/**
	 * ��ʼ��ѡ�
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

}
