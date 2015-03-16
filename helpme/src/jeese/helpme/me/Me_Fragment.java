package jeese.helpme.me;

import jeese.helpme.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Me_Fragment extends Fragment {
	
	private TextView meSet;
	private TextView mName;
	public ImageView mHeadImg;
	public View mView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Inflate()作用就是将xml定义的一个布局找出来，但仅仅是找出来而且隐藏的，没有找到的同时并显示功能。
		//inflate只会把Layout形成一个以view类实现成的对象，有需要时再用setContentView(view)显示出来
		mView = View.inflate(getActivity(), R.layout.me_fragment, null);
		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//getParent()和getCurrentActivity()的返回类型是Activity，所以用完了马上接 .方法 是没法编译的
		ViewGroup parent = (ViewGroup) mView.getParent();
		if (parent != null) {
			parent.removeView(mView);
		}
		return mView;
	}
	
	public void init(){
		
		mName=(TextView) mView.findViewById(R.id.textView_mname);
		mName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(getActivity(), MeProfile.class);
				startActivity(it);
			}
		});

	}
}
