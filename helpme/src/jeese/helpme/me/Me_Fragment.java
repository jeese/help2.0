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
		//Inflate()���þ��ǽ�xml�����һ�������ҳ��������������ҳ����������صģ�û���ҵ���ͬʱ����ʾ���ܡ�
		//inflateֻ���Layout�γ�һ����view��ʵ�ֳɵĶ�������Ҫʱ����setContentView(view)��ʾ����
		mView = View.inflate(getActivity(), R.layout.me_fragment, null);
		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//getParent()��getCurrentActivity()�ķ���������Activity���������������Ͻ� .���� ��û�������
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
				Intent it = new Intent(getActivity(), MyProfile.class);
				startActivity(it);
			}
		});
		
		meSet = (TextView) mView.findViewById(R.id.textView6_me_set);
		meSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(getActivity(), MeSetting.class);
				startActivity(it);
			}
		});	
	}
}
