package com.wangdiaozhu.qingweibo.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;
import com.wangdiaozhu.qingweibo.Constants.AccessTokenKeeper;
import com.wangdiaozhu.qingweibo.Constants.Constants;
import com.wangdiaozhu.qingweibo.R;
import com.wangdiaozhu.qingweibo.bean.EmotionList;
import com.wangdiaozhu.qingweibo.bean.Emotions;
import com.wangdiaozhu.qingweibo.fragment.ContentFragment;
import com.wangdiaozhu.qingweibo.utils.FileUtils;
import com.wangdiaozhu.qingweibo.utils.UpdateUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {


    private   static  final String TAG_CONTENT = "TAG_CONTENT";
    public static List<Emotions> emotionList;


    private Oauth2AccessToken mAccessToken;
    private User user;
    private long uid;
    private UsersAPI mUsersAPI;
    private StatusesAPI mStatusesAPI;
    private StatusList statuses;
    private User user2;
    private ArrayList<Status> statuses1;
    private Bundle bundle;



    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

                   switch (msg.what){

                       case 1:
                           bundle = new Bundle();
                           bundle.putSerializable("user", user);
                           break;
                       case 2:
                           statuses1 = statuses.statusList;
                           bundle.putSerializable("statues",statuses1);
                             break;
                   }
            FragmentManager manager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction =  manager.beginTransaction();
            ContentFragment fragment = new ContentFragment();
            fragment.setArguments(bundle);
            transaction.replace(R.id.fl_content, fragment, TAG_CONTENT);
            transaction.commit();
        }
    };
    private ArrayList<String> urls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        refreshData();
        initEmotionsList();

    }


     public ContentFragment getContentFragment(){

         FragmentManager manager = getSupportFragmentManager();

       ContentFragment fragment = (ContentFragment) manager.findFragmentByTag(TAG_CONTENT);


         return fragment;
     }


    public void refreshData(){
        mAccessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
        mUsersAPI = new UsersAPI(getApplicationContext(), Constants.APP_KEY, mAccessToken);
        mStatusesAPI = new StatusesAPI(getApplicationContext(), Constants.APP_KEY, mAccessToken);

        uid = Long.parseLong(mAccessToken.getUid());

//    获取当前登录用户的信息
        RequestListener mRequestListener = new RequestListener() {
            @Override
            public void onComplete(String s) {

                user = User.parse(s);
                if (user != null) {
                    Toast.makeText(getApplicationContext(),
                            "获取User信息成功，用户昵称：" + user.screen_name,
                            Toast.LENGTH_LONG).show();
                    Message msg = Message.obtain();
                  msg.what = 1;
                    handler.sendEmptyMessage(msg.what);

                } else {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onWeiboException(WeiboException e) {
                LogUtil.e("ERROR", e.getMessage());
                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                System.out.println(info.toString());

            }
        };
        RequestListener mListener = new RequestListener() {
            @Override
            public void onComplete(String response) {
                if (!TextUtils.isEmpty(response)) {

                    if (response.startsWith("{\"statuses\"")) {
                        // 调用 StatusList#parse 解析字符串成微博列表对象
                        statuses = StatusList.parse(response);
                        if (statuses != null && statuses.total_number > 0) {
//                            Toast.makeText(mActivity,
//                                    "获取微博信息流成功, 条数: " + statuses.statusList.get(1).text,
//                                    Toast.LENGTH_LONG).show();

                            Message msg = Message.obtain();
                           msg.what = 2;
                            handler.sendEmptyMessage(msg.what);

                        }
                    } else if (response.startsWith("{\"created_at\"")) {
                        // 调用 Status#parse 解析字符串成微博对象
                        Status status = Status.parse(response);
                        Toast.makeText(getApplicationContext(),
                                "发送一送微博成功, id = " + status.id,
                                Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {

                ErrorInfo info = ErrorInfo.parse(e.getMessage());
                Toast.makeText(getApplicationContext(), info.toString(), Toast.LENGTH_LONG).show();
            }
        };
        mUsersAPI.show(uid, mRequestListener);
        mStatusesAPI.friendsTimeline(0L, 0L, 20, 1, false, 0, false, mListener);
    }


    /** 初始化表情列表 */
    private void initEmotionsList() {
        String[] emotions_phrase = getResources().getStringArray(
                R.array.emotions_phrase);
        String[] emotions_imagename = getResources().getStringArray(
                R.array.emotions_imagename);
        Emotions emotions;
        emotionList = new ArrayList<Emotions>();
        for (int i = 0; i < 1772; i++) {
            emotions = new Emotions();
            emotions.setImageName(emotions_imagename[i+2]);
            emotions.setPhrase(emotions_phrase[i]);
            emotionList.add(emotions);
        }

    }


}
