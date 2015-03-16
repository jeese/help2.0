package jeese.helpme.me;

import java.io.ByteArrayOutputStream;
import jeese.helpme.R;
import jeese.helpme.view.ClipImageView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartPhotoZoom extends Activity {

	private ClipImageView imageView;
	private Button cancelCut;
	private Button confirmCut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cut);
		
		init();
	}
	
	public void init(){
		imageView = (ClipImageView) findViewById(R.id.src_pic);
		// 设置需要裁剪的图片
		Intent it = getIntent();
		Uri url = it.getData();
//		Bitmap bitmap = null;
		imageView.setImageURI(url);
//		try {
//			bitmap = MediaStore.Images.Media.getBitmap(
//					this.getContentResolver(), url);
//		} catch (Exception e) {
//			Log.e("[Android]", e.getMessage());
//			Log.e("[Android]", "目录为：" + url);
//			e.printStackTrace();
//		}
//		if (bitmap != null) {
//			imageView.setImageBitmap(bitmap);
//		}
		
		cancelCut=(Button) findViewById(R.id.cut_cancel);
		cancelCut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it =new Intent(StartPhotoZoom.this,MeProfile.class);
				startActivity(it);
				finish();
			}
		});
		confirmCut=(Button) findViewById(R.id.cut_save);
		confirmCut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				// 此处获取剪裁后的bitmap
				Bitmap bitmap = imageView.clip();
//				// 由于Intent传递bitmap不能超过40k,此处使用二进制数组传递
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] bitmapByte = baos.toByteArray();
				Intent intent = new Intent(StartPhotoZoom.this, MeProfile.class);
				intent.putExtra("bitmap", bitmapByte);
				setResult(android.app.Activity.RESULT_OK, intent);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if (item.getItemId() == R.id.action_clip) {
//			// 此处获取剪裁后的bitmap
//			Bitmap bitmap = imageView.clip();
//
//			// 由于Intent传递bitmap不能超过40k,此处使用二进制数组传递
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//			byte[] bitmapByte = baos.toByteArray();
//
//			//Intent intent = new Intent(this, Phone_PreviewActivity.class);
//			intent.putExtra("bitmap", bitmapByte);
//			startActivity(intent);
//		}
//		return super.onOptionsItemSelected(item);
//	}
}
