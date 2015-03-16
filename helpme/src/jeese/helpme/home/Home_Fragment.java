package jeese.helpme.home;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import io.rong.imkit.RongIM;

import java.util.ArrayList;
import java.util.List;

import jeese.helpme.R;
import jeese.helpme.adapter.Home_ListView_Adapter;
import jeese.helpme.people.ChooseFriendActivity;
import jeese.helpme.util.Dp2Px;
import jeese.helpme.view.PagerSlidingTabStrip;
import jeese.helpme.view.observablescrollview.ObservableListView;
import jeese.helpme.view.observablescrollview.ObservableScrollViewCallbacks;
import jeese.helpme.view.observablescrollview.ScrollState;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemClickListener;

public class Home_Fragment extends Fragment implements
		ObservableScrollViewCallbacks {

	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private MyPagerAdapter mPagerAdapter;
	private ViewPager mViewPager;
	private List<View> list;
	private View view1;
	private View view2;
	private View view3;

	private View mView;

	private int pageid = 0;// 当前page的id

	private PtrFrameLayout ptr1;
	private ObservableListView mListView1;
	private Home_ListView_Adapter mHomeListViewAdapter1;
	private PtrFrameLayout ptr2;
	private ObservableListView mListView2;
	private Home_ListView_Adapter mHomeListViewAdapter2;
	private ObservableListView mListView3;
	private Home_ListView_Adapter mHomeListViewAdapter3;

	private Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mView = View.inflate(getActivity(), R.layout.home_fragment, null);
		init();
		setView1();
		setView2();
		setView3();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup p = (ViewGroup) mView.getParent();
		if (p != null) {
			p.removeAllViewsInLayout();
		}
		return mView;
	}

	@SuppressLint("InflateParams")
	private void init() {

		mHandler = new Handler();

		mViewPager = (ViewPager) mView.findViewById(R.id.home_fragment_pager);

		view1 = LayoutInflater.from(getActivity()).inflate(R.layout.home_tab1,
				null);
		view2 = LayoutInflater.from(getActivity()).inflate(R.layout.home_tab2,
				null);
		view3 = LayoutInflater.from(getActivity()).inflate(R.layout.home_tab3,
				null);

		list = new ArrayList<View>();
		list.add(view1);
		list.add(view2);
		list.add(view3);

		mPagerAdapter = new MyPagerAdapter();
		mViewPager.setAdapter(mPagerAdapter);

		mPagerSlidingTabStrip = (PagerSlidingTabStrip) mView
				.findViewById(R.id.tabs);
		mPagerSlidingTabStrip.setViewPager(mViewPager);
		mPagerSlidingTabStrip
				.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						pageid = arg0;

					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}
				});
		initTabsValue();

	}

	/**
	 * mPagerSlidingTabStrip默认值配置
	 * 
	 */
	private void initTabsValue() {
		// 底部游标颜色
		mPagerSlidingTabStrip.setIndicatorColor(getResources().getColor(
				R.color.colorPrimary));
		// tab的分割线颜色
		mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
		// tab背景
		mPagerSlidingTabStrip.setBackgroundColor(Color.parseColor("#FFFFFF"));
		mPagerSlidingTabStrip.setUnderlineColor(getResources().getColor(
				R.color.colorPrimary));
		// tab底线高度
		mPagerSlidingTabStrip.setUnderlineHeight(1);
		// 游标高度
		mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue
				.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources()
						.getDisplayMetrics()));
		// 选中的文字颜色
		mPagerSlidingTabStrip.setSelectedTextColor(Color.parseColor("#252525"));
		// 正常文字颜色
		mPagerSlidingTabStrip.setTextColor(Color.parseColor("#999999"));
		// 自动填充屏幕
		mPagerSlidingTabStrip.setShouldExpand(true);

	}

	class MyPagerAdapter extends PagerAdapter {

		private final String[] TITLES = { "重要", "附近", "历史" };

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(list.get(position));
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(list.get(position));
			return list.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	@SuppressLint("ResourceAsColor")
	private void setView1() {

		setPtr1();

		mListView1 = (ObservableListView) view1
				.findViewById(R.id.home_tab1_listview);

		mHomeListViewAdapter1 = new Home_ListView_Adapter(getActivity(), null,
				new ListItemButtonClickListener());

		// AnimationAdapter animAdapter = new SwingBottomInAnimationAdapter(
		// mHomeListViewAdapter1);
		// animAdapter.setAbsListView(mListView1);
		mListView1.setAdapter(mHomeListViewAdapter1);

		mListView1.setScrollViewCallbacks(this);
		mListView1.setOnItemClickListener(new ListItemClickListener());
		mListView1.setOnItemLongClickListener(new ListItemLongClickListener());

		mHomeListViewAdapter1.addAll(getItems());

	}

	@SuppressLint("ResourceAsColor")
	private void setView2() {

		setPtr2();

		mListView2 = (ObservableListView) view2
				.findViewById(R.id.home_tab2_listview);

		mHomeListViewAdapter2 = new Home_ListView_Adapter(getActivity(), null,
				new ListItemButtonClickListener());

		// AnimationAdapter animAdapter = new SwingBottomInAnimationAdapter(
		// mHomeListViewAdapter2);
		// animAdapter.setAbsListView(mListView2);
		mListView2.setAdapter(mHomeListViewAdapter2);

		mListView2.setScrollViewCallbacks(this);
		mListView2.setOnItemClickListener(new ListItemClickListener());
		mListView2.setOnItemLongClickListener(new ListItemLongClickListener());

		mHomeListViewAdapter2.addAll(getItems());

	}

	@SuppressLint("ResourceAsColor")
	private void setView3() {
		mListView3 = (ObservableListView) view3
				.findViewById(R.id.home_tab3_listview);

		mHomeListViewAdapter3 = new Home_ListView_Adapter(getActivity(), null,
				new ListItemButtonClickListener());

		// AnimationAdapter animAdapter = new SwingBottomInAnimationAdapter(
		// mHomeListViewAdapter3);
		// animAdapter.setAbsListView(mListView3);
		mListView3.setAdapter(mHomeListViewAdapter3);

		mListView3.setScrollViewCallbacks(this);
		mListView3.setOnItemClickListener(new ListItemClickListener());
		mListView3.setOnItemLongClickListener(new ListItemLongClickListener());

		mHomeListViewAdapter3.addAll(getItems());

	}

	public ArrayList<Integer> getItems() {
		ArrayList<Integer> items = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			items.add(i);
		}
		return items;
	}

	public ArrayList<Integer> getnewItems() {
		ArrayList<Integer> items = new ArrayList<Integer>();
		for (int i = 0; i < 3; i++) {
			items.add(i);
		}
		return items;
	}

	private void setPtr1() {
		ptr1 = (PtrFrameLayout) view1.findViewById(R.id.home_tab1_ptr_frame);

		// header
		StoreHouseHeader header = new StoreHouseHeader(getActivity());
		header.setPadding(0, Dp2Px.dp2px(getActivity(), 20), 0,
				Dp2Px.dp2px(getActivity(), 20));
		header.initWithString("TO BE OR NOT TO BE");

		ptr1.setDurationToCloseHeader(1500);
		ptr1.setHeaderView(header);
		ptr1.addPtrUIHandler(header);
		// for changing string
		ptr1.addPtrUIHandler(new PtrUIHandler() {

			@Override
			public void onUIReset(PtrFrameLayout frame) {

			}

			@Override
			public void onUIRefreshPrepare(PtrFrameLayout frame) {

			}

			@Override
			public void onUIRefreshBegin(PtrFrameLayout frame) {

			}

			@Override
			public void onUIRefreshComplete(PtrFrameLayout frame) {

			}

			@Override
			public void onUIPositionChange(PtrFrameLayout frame,
					boolean isUnderTouch, byte status, int oldPosition,
					int currentPosition, float oldPercent, float currentPercent) {

			}
		});

		ptr1.setPtrHandler(new PtrHandler() {
			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						content, header);
			}

			@Override
			public void onRefreshBegin(final PtrFrameLayout ptr) {
				ptr.postDelayed(new Runnable() {
					@Override
					public void run() {
						mHomeListViewAdapter1.addAll(0, getnewItems());
						mHomeListViewAdapter1.notifyDataSetChanged();
						ptr.refreshComplete();
					}
				}, 1500);
			}
		});
	}

	private void setPtr2() {
		ptr2 = (PtrFrameLayout) view2.findViewById(R.id.home_tab2_ptr_frame);

		// header
		StoreHouseHeader header = new StoreHouseHeader(getActivity());
		header.setPadding(0, Dp2Px.dp2px(getActivity(), 20), 0,
				Dp2Px.dp2px(getActivity(), 20));
		header.initWithString("THAT IS THE QUESTION");

		ptr2.setDurationToCloseHeader(1500);
		ptr2.setHeaderView(header);
		ptr2.addPtrUIHandler(header);
		// for changing string
		ptr2.addPtrUIHandler(new PtrUIHandler() {

			@Override
			public void onUIReset(PtrFrameLayout frame) {

			}

			@Override
			public void onUIRefreshPrepare(PtrFrameLayout frame) {

			}

			@Override
			public void onUIRefreshBegin(PtrFrameLayout frame) {

			}

			@Override
			public void onUIRefreshComplete(PtrFrameLayout frame) {

			}

			@Override
			public void onUIPositionChange(PtrFrameLayout frame,
					boolean isUnderTouch, byte status, int oldPosition,
					int currentPosition, float oldPercent, float currentPercent) {

			}
		});

		ptr2.setPtrHandler(new PtrHandler() {
			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						content, header);
			}

			@Override
			public void onRefreshBegin(final PtrFrameLayout ptr) {
				ptr.postDelayed(new Runnable() {
					@Override
					public void run() {
						mHomeListViewAdapter2.addAll(0, getnewItems());
						mHomeListViewAdapter2.notifyDataSetChanged();
						ptr.refreshComplete();
					}
				}, 1500);
			}
		});
	}

	private final class ListItemButtonClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (pageid == 0) {
				for (int i = mListView1.getFirstVisiblePosition(); i <= mListView1
						.getLastVisiblePosition(); i++) {
					if (v == mListView1.getChildAt(
							i - mListView1.getFirstVisiblePosition())
							.findViewById(R.id.help_card_1_messagebutton)) {

						mHandler.post(new Runnable() {
							@Override
							public void run() {
								String userId = "002";
								String title = "对方的昵称";

								RongIM.getInstance().startPrivateChat(
										getActivity(), userId, title);
							}
						});

					} else if (v == mListView1.getChildAt(
							i - mListView1.getFirstVisiblePosition())
							.findViewById(R.id.ask_card_1_messagebutton)) {

						mHandler.post(new Runnable() {
							@Override
							public void run() {
								String userId = "002";
								String title = "对方的昵称";

								RongIM.getInstance().startPrivateChat(
										getActivity(), userId, title);
							}
						});

					}
				}
			}

		}
	}

	private final class ListItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (pageid == 0) {
				Intent Intent = new Intent(getActivity(),
						HelpDetailsVisitorActivity.class);
				startActivity(Intent);

			} else if (pageid == 1) {
				Intent Intent = new Intent(getActivity(),
						AskDeatilsVisitorActivity.class);
				startActivity(Intent);
			} else {
				Intent Intent = new Intent(getActivity(),
						AskDeatilsOwnerActivity.class);
				startActivity(Intent);
			}

		}
	}

	private final class ListItemLongClickListener implements
			OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			Intent Intent = new Intent(getActivity(),
					ChooseFriendActivity.class);
			startActivity(Intent);
			return true;
		}

	}

	@Override
	public void onScrollChanged(int scrollY, boolean firstScroll,
			boolean dragging) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDownMotionEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpOrCancelMotionEvent(ScrollState scrollState) {
		ActionBar actionBar = ((ActionBarActivity) getActivity())
				.getSupportActionBar();
		if (scrollState == ScrollState.UP) {
			if (actionBar.isShowing()) {
				actionBar.hide();
			}
		} else if (scrollState == ScrollState.DOWN) {
			if (!actionBar.isShowing()) {
				actionBar.show();
			}
		}

	}

}
