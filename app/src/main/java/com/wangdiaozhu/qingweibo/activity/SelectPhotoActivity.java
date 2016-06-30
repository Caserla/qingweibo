package com.wangdiaozhu.qingweibo.activity;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wangdiaozhu.qingweibo.R;
import com.wangdiaozhu.qingweibo.adapter.GirdItemAdapter;
import com.wangdiaozhu.qingweibo.adapter.ImageFloderAdapter;
import com.wangdiaozhu.qingweibo.utils.ImageFloder;


public class SelectPhotoActivity extends Activity  {

	private GridView photoGrid;

	private Button photoBtn;

	private TextView titleName;

	private ImageView titleIcon;

	private ProgressDialog mProgressDialog;

	private HashSet<String> mDirPaths = new HashSet<String>();

	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

	int totalCount = 0;

	private File mImgDir=new File("");

	private int mPicsSize;

	private List<String> mImgs=new ArrayList<String>();

	private int mScreenHeight;

	private LinearLayout dirLayout;

	private TextView quxiaoBtn;

	private ListView dirListView;

	private RelativeLayout headLayout;

	private int mScreenWidth;

	private ImageFloderAdapter floderAdapter;

	private GirdItemAdapter girdItemAdapter;

	private PopupWindow popupWindow;

	private View dirview;

	private static final int TAKE_CAMERA_PICTURE = 1000;

	private String path;

	private File dir;

	private String imagename;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo_select_view);
		GirdItemAdapter.mSelectedImage.clear();
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;
		mScreenWidth = outMetrics.widthPixels;
		setView();
		getImages();
		initEvent();
	}

	private void setView() {
		photoGrid=(GridView)findViewById(R.id.gird_photo_list);
		photoBtn=(Button)findViewById(R.id.selected_photo_btn);
		titleName=(TextView)this.findViewById(R.id.selected_photo_name_text);
		quxiaoBtn=(TextView)findViewById(R.id.quxiao_btn);
		titleIcon=(ImageView)findViewById(R.id.selected_photo_icon);
		headLayout=(RelativeLayout)findViewById(R.id.selected_photo_header_layout);
		titleIcon.setBackgroundResource(R.drawable.navigationbar_arrow_down);
	}

	private void getImages(){
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}

		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
		new Thread(new Runnable(){
			@Override
			public void run(){
				String firstImage = null;
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = SelectPhotoActivity.this.getContentResolver();

				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
								new String[] { "image/jpeg", "image/png" },
								MediaStore.Images.Media.DATE_MODIFIED);
				Log.e("TAG", mCursor.getCount() + "");
				while (mCursor.moveToNext()){

					String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
					Log.e("TAG", path);

					if (firstImage == null)
						firstImage = path;

					File parentFile = new File(path).getParentFile();
					if (parentFile == null)
						continue;
					String dirPath = parentFile.getAbsolutePath();
					ImageFloder imageFloder = null;

					if (mDirPaths.contains(dirPath)){
						continue;
					} else{
						mDirPaths.add(dirPath);
						// ��ʼ��imageFloder
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setFirstImagePath(path);
					}
					if(parentFile.list()==null)continue ;
					int picSize = parentFile.list(new FilenameFilter(){
						@Override
						public boolean accept(File dir, String filename){
							if (filename.endsWith(".jpg")
									|| filename.endsWith(".png")
									|| filename.endsWith(".jpeg"))
								return true;
							return false;
						}
					}).length;
					totalCount += picSize;
					imageFloder.setCount(picSize);
					mImageFloders.add(imageFloder);

					if (picSize > mPicsSize){
						mPicsSize = picSize;
						mImgDir = parentFile;
					}
				}
				mCursor.close();


				mDirPaths = null;


				mHandler.sendEmptyMessage(0x110);

			}
		}).start();

	}
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			mProgressDialog.dismiss();
			setDataView();
		}
	};


	public void setDataView(){
		path=Environment.getExternalStorageDirectory() + "/"+"test/photo/";
		dir=new File(path); 
		if(!dir.exists())dir.mkdirs(); 
		if (mImgDir == null){
			Toast.makeText(getApplicationContext(), "一张图片没有扫描到",Toast.LENGTH_SHORT).show();
			return;
		}

		if(mImgDir.exists()){
			mImgs = Arrays.asList(mImgDir.list());
		}
		System.out.println(mImgs);
		System.out.println(mImgDir.getAbsolutePath());
		girdItemAdapter=new GirdItemAdapter(this, mImgs, mImgDir.getAbsolutePath());
		photoGrid.setAdapter(girdItemAdapter);
		girdItemAdapter.setOnPhotoSelectedListener(new GirdItemAdapter.OnPhotoSelectedListener() {
			@Override
			public void takePhoto() {
				imagename=getTimeName(System.currentTimeMillis())+".jpg";
				String state = Environment.getExternalStorageState();  
				if (state.equals(Environment.MEDIA_MOUNTED)) {  
					Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f=new File(dir, imagename);//localTempImgDir��localTempImageFileName���Լ���������� 
					Uri u=Uri.fromFile(f); 
					intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0); 
					intent.putExtra(MediaStore.EXTRA_OUTPUT, u); 
					startActivityForResult(intent, TAKE_CAMERA_PICTURE); 
					Log.e("888888", "-------------0-------------------");
				} else {  
					Log.e("888888", "------------请插入SD卡-------------------");
					Toast.makeText(SelectPhotoActivity.this, "请插入SD卡",Toast.LENGTH_LONG).show();
				}  
			}
			@Override
			public void photoClick(List<String> number) {
				photoBtn.setText("完成("+number.size() + "张)");
			}
		});
	}

	private void initListDirPopupWindw(){
		if(popupWindow==null){
			dirview=LayoutInflater.from(SelectPhotoActivity.this).inflate(R.layout.image_select_dirlist, null);
			dirListView=(ListView)dirview.findViewById(R.id.id_list_dirs);
			popupWindow =new PopupWindow(dirview, LayoutParams.MATCH_PARENT, mScreenHeight*3/5);
			floderAdapter=new ImageFloderAdapter(SelectPhotoActivity.this, mImageFloders);
			dirListView.setAdapter(floderAdapter);
		}
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);

		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		titleIcon.setBackgroundResource(R.drawable.navigationbar_arrow_up);
		popupWindow.showAsDropDown(headLayout,0,0);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				titleIcon.setBackgroundResource(R.drawable.navigationbar_arrow_down);
			}
		});
		dirListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				titleName.setText(mImageFloders.get(position).getName());
				mImgDir = new File(mImageFloders.get(position).getDir());
				mImgs = Arrays.asList(mImgDir.list(new FilenameFilter(){
					@Override
					public boolean accept(File dir, String filename)
					{
						if (filename.endsWith(".jpg") || filename.endsWith(".png")
								|| filename.endsWith(".jpeg"))
							return true;
						return false;
					}
				}));
				for (int i = 0; i < mImageFloders.size(); i++) {
					mImageFloders.get(i).setSelected(false);
				}
				mImageFloders.get(position).setSelected(true);
				floderAdapter.changeData(mImageFloders);
				girdItemAdapter.changeData(mImgs, mImageFloders.get(position).getDir());
				if(popupWindow!=null){
					popupWindow.dismiss();
				}
			}
		});

	}

	private void initEvent(){
		quxiaoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		titleName.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

				initListDirPopupWindw();
			}
		});
		photoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {


				ArrayList<String> images1 = new ArrayList<String>(GirdItemAdapter.mSelectedImage);

				Bundle bundle = new Bundle();
				bundle.putStringArrayList("images", images1);
				Intent intent = new Intent();
				intent.putExtras(bundle);
				SelectPhotoActivity.this.setResult(1, intent);
				finish();
			}
		});
	}
	public static String getTimeName(long time){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date(time);
		return formatter.format(date);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("888888", "-------------1-------------------");
		switch (requestCode) {
		case TAKE_CAMERA_PICTURE:
			Log.e("888888", "-------------2-------------------");
			Toast.makeText(this, path+imagename,Toast.LENGTH_LONG).show();
			break;
		}
	}
}
