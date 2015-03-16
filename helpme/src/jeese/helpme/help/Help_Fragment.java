package jeese.helpme.help;

import java.util.ArrayList;
import java.util.List;

import jeese.helpme.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Help_Fragment extends Fragment implements OnClickListener {

	private View mView;
	private MyPagerAdapter mPagerAdapter;
	private ViewPager mViewPager;
	private List<View> list;
	private View view1;
	private View view2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mView = View.inflate(getActivity(), R.layout.help_fragment, null);
		init();
		setView1();
		setView2();
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

	private void init() {

		mViewPager = (ViewPager) mView.findViewById(R.id.help_fragment_pager);

		view1 = LayoutInflater.from(getActivity()).inflate(R.layout.help_tab1,
				null);
		view2 = LayoutInflater.from(getActivity()).inflate(R.layout.help_tab2,
				null);

		list = new ArrayList<View>();
		list.add(view1);
		list.add(view2);

		mPagerAdapter = new MyPagerAdapter();
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void setView1() {
		ImageButton help_tab1_button1 = (ImageButton) view1
				.findViewById(R.id.help_tab1_helpbutton_1);
		help_tab1_button1.setOnClickListener(this);
		ImageButton help_tab1_button2 = (ImageButton) view1
				.findViewById(R.id.help_tab1_helpbutton_2);
		help_tab1_button2.setOnClickListener(this);
		ImageButton help_tab1_button3 = (ImageButton) view1
				.findViewById(R.id.help_tab1_helpbutton_3);
		help_tab1_button3.setOnClickListener(this);
	}

	private void setView2() {
		ImageButton help_1 = (ImageButton) view2
				.findViewById(R.id.help_fragment_helpbutton_1);
		help_1.setOnClickListener(this);
		ImageButton help_2 = (ImageButton) view2
				.findViewById(R.id.help_fragment_helpbutton_2);
		help_2.setOnClickListener(this);
		ImageButton help_3 = (ImageButton) view2
				.findViewById(R.id.help_fragment_helpbutton_3);
		help_3.setOnClickListener(this);
		ImageButton help_4 = (ImageButton) view2
				.findViewById(R.id.help_fragment_helpbutton_4);
		help_4.setOnClickListener(this);
		ImageButton help_5 = (ImageButton) view2
				.findViewById(R.id.help_fragment_helpbutton_5);
		help_5.setOnClickListener(this);
		ImageButton help_6 = (ImageButton) view2
				.findViewById(R.id.help_fragment_helpbutton_6);
		help_6.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.help_tab1_helpbutton_1:
			Intent intent_1 = new Intent(getActivity(),
					SendCountDownActivity.class);
			startActivity(intent_1);
			break;

		case R.id.help_tab1_helpbutton_2:
			Intent intent_2 = new Intent(getActivity(),
					SendLifeHelpActivity.class);
			startActivity(intent_2);
			break;

		case R.id.help_tab1_helpbutton_3:
			Intent intent_3 = new Intent(getActivity(),
					SendQuestionActivity.class);
			startActivity(intent_3);
			break;

		case R.id.help_fragment_helpbutton_1:
			Intent gpsIntent = new Intent(getActivity(),
					SendLifeHelpActivity.class);
			startActivity(gpsIntent);
			break;

		case R.id.help_fragment_helpbutton_2:
			Intent intent = new Intent(getActivity(),
					SendQuestionActivity.class);
			startActivity(intent);
			break;

		case R.id.help_fragment_helpbutton_3:

			break;

		case R.id.help_fragment_helpbutton_4:

			break;

		case R.id.help_fragment_helpbutton_5:

			break;

		case R.id.help_fragment_helpbutton_6:

			break;
		}
	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(list.get(position));
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Tab" + (position);
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

}
